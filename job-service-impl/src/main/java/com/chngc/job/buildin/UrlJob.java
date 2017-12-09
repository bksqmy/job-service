package com.chngc.job.buildin;


import com.chngc.job.AbstractJob;
import com.chngc.job.JobResult;
import com.chngc.job.entity.SysJob;
import com.chngc.job.net.HttpClientManager;
import org.quartz.JobExecutionContext;

/**
 * 调用URL的Job。
 * <p>
 * Job执行时调用一个URL地址，URL地址配置在{@link SysJob#getCfgData()}中，格式如下：{ url:"http://www.ehaier.com/api/xxxx" }<br />
 * 某些PHP实现的后台作业程序可以使用这种方式。
 * 
 * <p>其他类型的Job参考{@link AbstractJob}
 * 
 * @version 1.0
 */
public class UrlJob extends AbstractJob {

    @Override
    public JobResult excecute(SysJob job, Integer logId, JobExecutionContext context)
                                                                                     throws Exception {
        //使用HTTP Client或者URL调用
        UrlJobLoader loader = (UrlJobLoader) super.getJobLoader(context);

        try {
            String getStr = HttpClientManager.httpGet(loader.getUrl());
            log.info(getStr);
        } catch (Exception e) {
            log.error("调用URL job发生异常：" + e.getMessage(), e);
        }

        return null;
    }




}