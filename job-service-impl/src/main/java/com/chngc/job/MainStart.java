package com.chngc.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by qumingyang on 2017/12/9.
 */
public class MainStart {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "job-service-config.xml",
                "conf/datasources.xml",
                "conf/service-deploy.xml"
        });
        context.start();

        System.out.println("dubbo-server服务正在监听，按任意键退出");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
