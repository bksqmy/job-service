package com.chngc.job;


import com.chngc.job.entity.SysJob;
import com.chngc.job.entity.SysJobLog;
import com.chngc.job.util.DateUtil;
import com.chngc.job.util.JsonUtil;
import com.chngc.job.util.StringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Job的抽象基类，所有Job都必须继承这个类。
 * <p/>
 * <p/>
 * Job共分以下几种类型：<br />
 * 1. {@link SysJob#TYPE_DUBBO}: 调用Dubbo服务方法的Job，参考{@link }；<br />
 * 2. {@link SysJob#TYPE_SQL}: 执行SQL语句的Job，参考{@link }；<br />
 * 3. {@link SysJob#TYPE_URL}: 调用Url的Job，参考{@link }；<br />
 * 4. {@link SysJob#TYPE_CUSTOM}: 自定义的Job，参考{@link }。<br />
 *
 * @version 1.0
 */
public abstract class AbstractJob implements Job {
    protected final static Logger log = LoggerFactory
            .getLogger(AbstractJob.class);

    public final static String SYS_JOB_KEY = "$SYS_JOB_KEY$";
    public final static String REPOSITORY_KEY = "$SYS_JOB_REPOSITORY_KEY$";
    public final static String APPLICATION_CONTEXT_KEY = "$APPLICATION_CONTEXT_KEY$";
    public final static String REFERENCE_CONFIG_KEY = "$REFERENCE_CONFIG_KEY$";

    /**
     * 并发控制。
     * <p/>
     * 某个SysJob在开始运行时先检查jobId是否已经存在于runningJobs中：<br />
     * &nbsp;&nbsp;如果存在则表明这个SysJob已经有一个实例正在运行，因此退出，不执行Job所需的操作；<br />
     * &nbsp;&nbsp;如果不存在，则表明该SysJob不存在并发运行的实例，因此继续执行Job所需操作；<br />
     * SysJob在执行Job操作时，先将自己的ID放入runningJobs中，运行结束后从runningJobs中删除自己的ID。
     * <p/>
     * 目前不允许任何job并发执行，以后可以加强管理：<br />
     * &nbsp;&nbsp; 1. job是否允许并发执行，通过SysJob配置指定；<br />
     * &nbsp;&nbsp; 2. 可以使用runningJobs对job执行超时进行处理；
     */
    private static Set<String> runningJobs = new HashSet<String>();

    /**
     * Quartz的Job接口方法，不允许子类重载。
     *
     * @param context
     * @throws JobExecutionException
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        long tsEntering = System.currentTimeMillis();
        boolean success = false;

        Object obj = context.getMergedJobDataMap().get(SYS_JOB_KEY);
        if (obj == null || !SysJob.class.equals(obj.getClass())) {
            log.warn("SysJob not found in JobExecutionContext, class:" + this.getClass().getName());
            return;
        }
        SysJob job = (SysJob) obj;

        JobRepository repo = this.getRepository(context);




        //并发控制
        boolean canStart = startRun(job.getJobId());

        //插入日志记录
        SysJobLog jobLog = new SysJobLog();
        jobLog.setJobId(job.getJobId());
        jobLog.setStartTime(new Date());
        jobLog.setEndTime(DateUtil.getDate(1900, 1, 1));
        jobLog.setNextTime(context.getNextFireTime());
        if (!canStart) {
            jobLog.setEndTime(new Date());
            jobLog.setSysStatus(SysJobLog.SYS_STATUS_ERROR);
            jobLog.setMessage("不允许并发执行");
        } else {
            jobLog.setSysStatus(0);
        }
        jobLog.setBizStatus(0);
        try {
            repo.getSysJobLogDao().insert(jobLog);
        } catch (Exception e) {
            log.warn("[job-" + job.getJobId() + "] Insert log error:" + JsonUtil.toJson(jobLog), e);
        }

        if (!canStart) {
            log.info("[job-" + job.getJobId() + "] Exit because of concurrent execution");
            return;
        }

        //执行Job方法
        long tsStartExecuting = System.currentTimeMillis();
        try {
            JobResult result = this.excecute(job, jobLog.getLogId(), context);
            success = true;
            jobLog.setEndTime(new Date());
            jobLog.setSysStatus(SysJobLog.SYS_STATUS_SUCCESS);
            if (result != null) {
                jobLog.setBizStatus(result.getResult());
                jobLog.setMessage(result.getMessage());
            }
        } catch (Exception e) {
            log.error("[job-" + job.getJobId() + "] Excecution error", e);
            jobLog.setEndTime(new Date());//出错了也要记录结束时间
            jobLog.setSysStatus(-1);
            jobLog.setMessage("Job执行时发生异常：" + e.getMessage());
        }
        long tsEndExecuted = System.currentTimeMillis();

        //执行结果更新日志记录
        try {
            jobLog.setMessage(StringUtil.dbSafeString(jobLog.getMessage(), false, 1000));
            if (jobLog.getLogId() != null && jobLog.getLogId() > 0)
                repo.getSysJobLogDao().update(jobLog);
            else
                log.warn("[job-" + job.getJobId() + "] Update log error:" + JsonUtil.toJson(jobLog));
        } catch (Exception e) {
            log.warn("[job-" + job.getJobId() + "] Update log error:" + JsonUtil.toJson(jobLog), e);
        }

        stopRun(job.getJobId());

        long tsExiting = System.currentTimeMillis();
        if (log.isInfoEnabled())
            log.info("[job-" + job.getJobId() + "] Job executed "
                    + (success ? "successfully" : "failed") + ", "
                    + DateUtil.format(new Date(tsEntering), "HH:mm:ss SSS") + " > "
                    + DateUtil.format(new Date(tsStartExecuting), "HH:mm:ss SSS") + " > "
                    + DateUtil.format(new Date(tsEndExecuted), "HH:mm:ss SSS") + " > "
                    + DateUtil.format(new Date(tsExiting), "HH:mm:ss SSS") + ", elapsed:"
                    + (tsExiting - tsEntering) + "ms");
    }

    /**
     * 如果该job已经有一个实例正在运行，则返回false，否则返回true
     *
     * @param jobId
     * @return true表示无并发实例正在执行，可以执行本次job操作；false表示已经有一个job实例正在执行，应当终止本次job操作。
     */
    private static boolean startRun(Integer jobId) {
        if (jobId == null || jobId <= 0) {//无效job id，不进行并发控制
            log.info("[job-" + jobId + "] Invalidate job id, concurrent controll ingnored");
            return true;
        }

        if (runningJobs.size() > 0) {
            log.info("#############：被锁定KEY:START" + runningJobs.size());
            for(String key : runningJobs) {
                log.info(key);
            }
            log.info("#############：被锁定KEY:END");
        }

        String key = String.valueOf(jobId);
        if (runningJobs.contains(key))
            return false;
        synchronized (runningJobs) {
            if (runningJobs.contains(key))
                return false;
            runningJobs.add(key);
        }

        return true;
    }

    private static void stopRun(Integer jobId) {
        if (jobId == null || jobId <= 0)
            return;
        synchronized (runningJobs) {
            runningJobs.remove(String.valueOf(jobId));
        }
    }

    /**
     * 执行Job的逻辑处理。
     *
     * @param job
     * @param logId
     * @param context
     * @return Job执行结果情况
     * @throws Exception
     */
    public abstract JobResult excecute(SysJob job, Integer logId, JobExecutionContext context)
            throws Exception;

    /**
     * 获取Spring的{@link ApplicationContext}对象。
     *
     * @param context
     * @return Spring的上下文。
     */
    public ApplicationContext getApplicationContext(JobExecutionContext context) {
        return (ApplicationContext) context.getMergedJobDataMap().get(APPLICATION_CONTEXT_KEY);
    }

    /**
     * 获取Job的{@link JobRepository}对象。
     *
     * @param context
     * @return Job仓库
     */
    public JobRepository getRepository(JobExecutionContext context) {
        return (JobRepository) context.getMergedJobDataMap().get(REPOSITORY_KEY);
    }

    public JobLoader getJobLoader(JobExecutionContext context) {
        return (JobLoader) context.getMergedJobDataMap().get(REFERENCE_CONFIG_KEY);
    }
}