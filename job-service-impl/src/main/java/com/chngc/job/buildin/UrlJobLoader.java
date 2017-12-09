package com.chngc.job.buildin;


import com.chngc.job.AbstractJobLoader;
import com.chngc.job.InvalidateCfgDataException;
import com.chngc.job.JobManager;
import com.chngc.job.entity.SysJob;
import com.chngc.job.util.StringUtil;
import org.quartz.JobDetail;

import static org.quartz.JobBuilder.newJob;

/**
 * @version 1.0
 * @see UrlJob
 */
public class UrlJobLoader extends AbstractJobLoader {
    private String url;

    public UrlJobLoader(SysJob job, JobManager manager) {
        super(job, manager);
    }

    @Override
    protected void parseCfgData() {
        this.url = (String) super.job.getCfgData("url");
        if (StringUtil.isEmpty(this.url, true))
            throw new InvalidateCfgDataException("Null or empty url");
    }

    @Override
    protected JobDetail internalBuildJobDetail() {
        JobDetail jd = newJob(UrlJob.class).withIdentity(
            "[job-" + job.getJobType() + "-" + job.getJobId() + "]", DEFAULT_GROUP).build();
        if (log.isInfoEnabled())
            log.info("[job-" + job.getJobId() + "][" + job.getJobName() + "] URL job: " + url);
        return jd;
    }

    public String getUrl() {
        return this.url;
    }

}
