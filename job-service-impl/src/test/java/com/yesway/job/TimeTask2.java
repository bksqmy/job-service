package com.yesway.job;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * schedule(TimerTask task, Date time) 特定時間執行
 */
public class TimeTask2 {

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            new Timer("timer - " + i).schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " run ");
                }
            }, new Date(System.currentTimeMillis() + 2000));
        }
    }
}
