package com.chngc.job.dao;

import com.chngc.job.entity.SysJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @version 1.0
 */
public interface SysJobDao {
    /**
     * 查job配置。
     * @param name
     * @param status
     * @return Job列表
     */
    List<SysJob> find(@Param("name") String name, @Param("status") Integer status);

    /**
     * 更新job状态
     * @param jobId
     * @param jobStatus
     */
    void updateJobStatus(@Param("job_id") Integer jobId, @Param("job_status") Integer jobStatus);

}