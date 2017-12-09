package com.chngc.job.impl;


import com.chngc.job.JobManager;
import com.chngc.job.commons.ServiceResult;
import com.chngc.job.entity.SysJob;
import com.chngc.job.service.JobService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class JobServiceImpl implements JobService {

    @Autowired
    private JobManager jobManager;

    private Logger log = LogManager.getLogger(JobServiceImpl.class);

    @Override
    public String echo() {
        log.info("It works!");
        return "It works!";
    }

    @Override
    public ServiceResult<Boolean> stopJob(SysJob job) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            this.jobManager.stopJob(job);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResult(Boolean.FALSE);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceResult<Boolean> startJob(SysJob job) {
        ServiceResult<Boolean> result = new ServiceResult<Boolean>();
        try {
            this.jobManager.startJob(job);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResult(Boolean.FALSE);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @Override
    public void autoUpdateJobs() {

        try {
            List<SysJob> changeedSysJobs = jobManager.getRepository().findSysJobDao(null, SysJob.STATUS_CHANGEED);

            if (changeedSysJobs == null || changeedSysJobs.size() == 0) {
                return;
            }

            for (SysJob sysJob : changeedSysJobs) {
                jobManager.updateJob(sysJob);
                log.info("updateJob succeeded, the job name is :" + sysJob.getJobName());
                jobManager.getRepository().updateJobStatus(sysJob.getJobId(), SysJob.STATUS_ENABLED);
            }
        } catch (Exception e) {
            log.error("UpdateJob error :" + e.getMessage());
        }


    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public void setJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }
}
