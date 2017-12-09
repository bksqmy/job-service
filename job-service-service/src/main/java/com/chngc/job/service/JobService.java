package com.chngc.job.service;


import com.chngc.job.commons.ServiceResult;
import com.chngc.job.entity.SysJob;

public interface JobService {
    String echo();

    /**
     * 停止job
     * @param job
     * @return
     */
    ServiceResult<Boolean> stopJob(SysJob job);

    /**
     * 启动job
     * @param job
     * @return
     */
    ServiceResult<Boolean> startJob(SysJob job);

    /**
     * 自动更新任务，临时方法，后续采用管理界面后使用触发器方式，再停止此任务
     */
    void autoUpdateJobs();

}
