package com.yesway.job;

import java.util.Timer;
import java.util.TimerTask;

/**
 * schedule(TimerTask task, long delay, long period) 延迟 delay 执行并每隔period 执行一次
 */
public class TimeTask3 {

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            new Timer("timer - " + i).schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " run ");
                }
            }, 2000 , 3000);
        }
    }
}
