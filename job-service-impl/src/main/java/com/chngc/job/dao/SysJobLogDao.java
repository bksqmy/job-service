package com.chngc.job.dao;


import com.chngc.job.entity.SysJobLog;

/**
 * 
 * @version 1.0
 */
public interface SysJobLogDao {

    /**
     * 插入之后的job id通过{@link SysJobLog#getLogId()}获取。
     * @param log
     * @return 受影响的行数
     */
    Integer insert(SysJobLog log);

    /**
     * 返回受影响行数。
     * @param log
     * @return 受影响的行数
     */
    Integer update(SysJobLog log);

}