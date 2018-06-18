package com.yesway.job;

import java.util.Timer;
import java.util.TimerTask;

/**
 * schedule(TimerTask task, long delay) 延迟 delay 毫秒 执行
 */
public class TimeTask {

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            new Timer("timer - " + i).schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " run ");
                }
            }, 1000);
        }
    }
}
