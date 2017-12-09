package com.chngc.job;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * 对Job进行校验、初始化设置。
 * @version 1.0
 */
public interface JobLoader {
    /**
     * 初步校验Job配置是否有效。
     */
    void parse() throws InvalidateCronException, InvalidateCfgDataException;

    /**
     * 构造Quartz的{@link Trigger}
     * @return Quartz的Trigger对象。
     */
    Trigger buildTrigger();

    /**
     * 构造Quartz的{@link JobDetail}
     * @return Quarts的JobDetail对象。
     */
    JobDetail buildJobDetail();
}