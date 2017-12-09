package com.chngc.job;

import com.chngc.job.dao.SysJobDao;
import com.chngc.job.dao.SysJobLogDao;
import com.chngc.job.entity.SysJob;
import com.chngc.job.util.StringUtil;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Job仓库。
 * @version 1.0
 */
public class JobRepository {
    private SysJobDao sysJobDao;
    private SysJobLogDao sysJobLogDao;

    public List<SysJob> findSysJobDao(String name, Integer status) {
        Assert.notNull(this.sysJobDao, "Property 'sysJobDao' is required!");
        if (StringUtil.isEmpty(name))
            name = null;
        return this.sysJobDao.find(name, status);
    }

    public void updateJobStatus(Integer jobId, Integer jobStatus) {
        Assert.notNull(this.sysJobDao, "Property 'sysJobDao' is required!");

        if (jobId == null)
            return;

        if (jobStatus == null)
            return;

        this.sysJobDao.updateJobStatus(jobId, jobStatus);
    }


    public void setSysJobDao(SysJobDao value) {
        this.sysJobDao = value;
    }

    public void setSysJobLogDao(SysJobLogDao value) {
        this.sysJobLogDao = value;
    }

    public SysJobLogDao getSysJobLogDao() {
        return this.sysJobLogDao;
    }
}