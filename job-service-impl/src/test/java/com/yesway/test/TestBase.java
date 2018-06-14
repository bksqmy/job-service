package com.yesway.test;

import com.google.gson.GsonBuilder;
import junit.framework.TestCase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

public abstract class TestBase extends TestCase {
    protected static ClassPathXmlApplicationContext context = null;

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.xml");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }
    protected static Logger log = LogManager.getLogger(TestBase.class);

    public void setUp() {
        log.info("===========================================================================");
        log.info(">>>>>>>>>>>>>>>>>>>>>>> " + super.getName() + "():");
        if (context == null) {
            context = new ClassPathXmlApplicationContext(
                    new String[]{"classpath:test-deploy.xml"});
            context.start();
        }
        init();
    }

    protected abstract void init();

    /**
     * 打印测试对象
     * @param obj
     */
    public void print(Object obj) {
        this.print(obj, true);
    }

    public void print(Object obj, boolean pretty) {
        GsonBuilder gb = new GsonBuilder();
        if (pretty)
            gb.setPrettyPrinting();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(gb.create().toJson(obj));
    }
}