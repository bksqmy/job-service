package com.chngc.job.buildin;


import com.chngc.job.AbstractJobLoader;
import com.chngc.job.InvalidateCfgDataException;
import com.chngc.job.JobManager;
import com.chngc.job.entity.SysJob;
import com.chngc.job.util.StringUtil;
import org.quartz.JobDetail;

import static org.quartz.JobBuilder.newJob;

/**
 * 
 * @version 1.0
 * @see SqlJob
 */
public class SqlJobLoader extends AbstractJobLoader {
    private String sql;

    public SqlJobLoader(SysJob job, JobManager manager) {
        super(job, manager);
    }

    @Override
    protected void parseCfgData() {
        this.sql = (String) job.getCfgData("sql");
        if (StringUtil.isEmpty(this.sql, true))
            throw new InvalidateCfgDataException("Null or empty sql");
    }

    @Override
    protected JobDetail internalBuildJobDetail() {
        JobDetail jd = newJob(SqlJob.class).withIdentity(
            "[job-" + job.getJobType() + "-" + job.getJobId() + "]", DEFAULT_GROUP).build();
        if (log.isInfoEnabled())
            log.info("[job-" + job.getJobId() + "][" + job.getJobName() + "] SQL job: " + sql);
        return jd;
    }

}
