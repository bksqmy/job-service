package com.yesway.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by qumingyang on 2018/6/18.
 */
public class QuartzTime {

    public static void main(String[] args) throws Exception {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        // 开始
        scheduler.start();
        // job 唯一标识 test.test-1
        JobKey jobKey = new JobKey("test" , "test-1");
        JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity(jobKey).build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("test" , "test")
                // 延迟一秒执行
                .startAt(new Date(System.currentTimeMillis() + 1000))
                // 每隔一秒执行 并一直重复
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail , trigger);

        Thread.sleep(10000);
        // 删除job
        scheduler.deleteJob(jobKey);
    }
}
