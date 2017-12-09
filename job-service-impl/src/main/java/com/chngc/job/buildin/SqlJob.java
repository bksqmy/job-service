package com.chngc.job.buildin;


import com.chngc.job.AbstractJob;
import com.chngc.job.JobResult;
import com.chngc.job.NotImplementedJobException;
import com.chngc.job.entity.SysJob;
import org.quartz.JobExecutionContext;

/**
 * 执行SQL语句的Job。
 * <p>
 * 某些需要定时执行的操作可以通过一个SQL语句实现，可以使用该类型Job。{@link SysJob#getCfgData()}配置格式如下：
 * { sql: "update xxx set col1=xxx, col2=xxx where ....." }
 * 
 * <p>其他类型的Job参考{@link AbstractJob}
 * 
 * @version 1.0
 */
public class SqlJob extends AbstractJob {

    @Override
    public JobResult excecute(SysJob job, Integer logId, JobExecutionContext context)
                                                                                     throws Exception {
        throw new NotImplementedJobException();
    }

}