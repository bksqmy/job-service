package com.chngc.job.custom.demo;

import com.chngc.job.AbstractJob;
import com.chngc.job.JobResult;
import com.chngc.job.entity.SysJob;
import org.quartz.JobExecutionContext;


/**
 * Demo
 * @version 1.0
 */
public class CustomJobDemo1 extends AbstractJob {

    @Override
    public JobResult excecute(SysJob job, Integer logId, JobExecutionContext context)
            throws Exception {
        log.info(">>> CustomJob1 executed");
        return null;
    }

}