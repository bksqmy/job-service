CREATE TABLE `sys_job` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Job ID',
  `job_name` varchar(30) NOT NULL DEFAULT '' COMMENT 'Job名称',
  `job_type` varchar(10) NOT NULL DEFAULT '' COMMENT 'Job类型。',
  `job_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Job状态（-1删除，0禁用，1启用）。',
  `cfg_data` text COMMENT 'Job配置数据（JSON格式的字符串）。',
  `cron` varchar(100) NOT NULL DEFAULT '' COMMENT 'CRON表达式，Job的调度时间设置。',
  `update_user` varchar(20) NOT NULL DEFAULT '' COMMENT '最后更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='JOB配置';

CREATE TABLE `sys_job_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Job日志ID',
  `job_id` int(11) NOT NULL COMMENT 'Job ID',
  `start_time` datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '开始时间',
  `end_time` datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '结束时间',
  `next_time` datetime NOT NULL DEFAULT '1900-01-01 00:00:00' COMMENT '下一次运行时间。',
  `sys_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Job执行结果：系统状态。',
  `biz_status` int(11) NOT NULL DEFAULT '0' COMMENT 'Job执行结果：业务状态。',
  `message` varchar(1000),
  PRIMARY KEY (`log_id`),
  KEY `ix_job_log_jobid` (`job_id`),
  KEY `ix_job_log_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1434 DEFAULT CHARSET=utf8 COMMENT='Job日志';


## 没有管理界面前，使用自更新，需要设置状态为5，每30S执行一次
INSERT INTO `chngc_job`.`sys_job` (
  `job_name`,
  `job_type`,
  `job_status`,
  `cfg_data`,
  `cron`,
  `update_user`,
  `update_time`
)
VALUES
  (
    '任务自更新',
    'dubbo',
    '1',
    '{\"interface\":\"com.chngc.job.service.JobService\",\"method\":\"autoUpdateJobs\",\"url\":\"\",\"timeout\":\"\"} ',
    '*/30 * * * * ?',
    'test',
    '2015-10-31 22:45:30'
  );

