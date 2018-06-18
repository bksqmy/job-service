package com.yesway.job;

import com.chngc.job.commons.ServiceResult;
import com.chngc.job.entity.SysJob;
import com.chngc.job.service.JobService;
import com.yesway.test.*;

public class ITestJobservice extends TestBase{

	private JobService jobService;

	@Override
	protected void init() {
		jobService = context.getBean(JobService.class);
	}
	
	public void testSelectByPrimaryKey() {
		SysJob sysJob = new SysJob();
		ServiceResult<Boolean> result = jobService.startJob(sysJob);
		System.out.println(result);
	}


}
