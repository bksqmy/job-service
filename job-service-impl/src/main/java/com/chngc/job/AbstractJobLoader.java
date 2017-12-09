package com.chngc.job;

import com.chngc.job.entity.SysJob;
import com.chngc.job.util.StringUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 
 * @version 1.0
 */
public abstract class AbstractJobLoader implements JobLoader {
    protected final static Logger log           = LoggerFactory.getLogger(AbstractJobLoader.class);

    protected final static String DEFAULT_GROUP = "default";
    protected SysJob job;
    protected JobManager          jobManager;

    public AbstractJobLoader(SysJob job, JobManager manager) {
        this.job = job;
        this.jobManager = manager;
    }

    public final void parse() {
        if (StringUtil.isEmpty(job.getCron(), true))
            throw new InvalidateCronException("Null or empty cron expression");
        this.parseCfgData();
    }

    protected abstract void parseCfgData();

    @Override
    public final Trigger buildTrigger() {
        CronScheduleBuilder cronBuilder = null;
        try {
            cronBuilder = cronSchedule(job.getCron());
        } catch (Exception e) {
            throw new InvalidateCronException("Invalidate cron: " + job.getCron() + ", "
                                              + e.getMessage());
        }

        return newTrigger()
            .withIdentity("[trigger-" + job.getJobType() + "-" + job.getJobId() + "]",
                DEFAULT_GROUP).withSchedule(cronBuilder).build();
    }

    @Override
    public final JobDetail buildJobDetail() {
        JobDetail jd = this.internalBuildJobDetail();
        //将SysJob、JobRepository、ApplicationContext放入JobDetail的DataMap中，这样在Job的execute方法中就能够获取到这些对象
        //JobBase实例不是由spring创建，因此无法为JobBase注入bean对象，相关的bean对象需要通过ApplicationContext来获取
        jd.getJobDataMap().put(AbstractJob.SYS_JOB_KEY, this.job);
        jd.getJobDataMap().put(AbstractJob.REPOSITORY_KEY, this.jobManager.getRepository());
        jd.getJobDataMap().put(AbstractJob.APPLICATION_CONTEXT_KEY,
            this.jobManager.getApplicationContext());
        jd.getJobDataMap().put(AbstractJob.REFERENCE_CONFIG_KEY, this);
        return jd;
    }

    protected abstract JobDetail internalBuildJobDetail();

}
