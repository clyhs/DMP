package org.dmp.module.admin.quartz.kettle;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.dmp.module.admin.quartz.service.IMonitorService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class QuartzExecute extends QuartzJobBean{
	
	private ApplicationContext cxt;
	
	public ApplicationContext getApplicationContext() {
		return cxt;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.cxt = applicationContext;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext exeContext)
			throws JobExecutionException {
		
		
		
		JobDetail jobDetail =  exeContext.getJobDetail();
		JobDataMap jobDataMap =  jobDetail.getJobDataMap();
		String jobName = jobDetail.getName();
		String actionRef = jobDataMap.getString("actionRef");
		String actionPath = jobDataMap.getString("actionPath");
		String fileType = jobDataMap.getString("fileType");
		String repName = jobDataMap.getString("repName");
		String userName = jobDataMap.getString("userName");
		String password = jobDataMap.getString("passWord");
		IKettleEngine kettEngin = new KettleEngineImpl();
		IMonitorService monitorService = null;
		try
		{
			System.out.println(cxt.getBean("MonitorService").toString());
			monitorService = (IMonitorService) cxt.getBean("MonitorService");
			Date startdate = new Date();
			monitorService.updateMonitorTime(exeContext.getPreviousFireTime(), exeContext.getNextFireTime(),startdate, jobDetail.getName());
			kettEngin.execute(repName, userName, password, actionPath, actionRef, fileType);
			Date endDate = new Date();
			float f  = (endDate.getTime()-startdate.getTime())/1000L;
			monitorService.updateMonitorTime(exeContext.getScheduler().getTriggerState(exeContext.getJobDetail().getName(), "DEFAULT"),
					exeContext.getPreviousFireTime(), exeContext.getNextFireTime(), f, endDate, jobDetail.getName());

		}
		catch(Exception ex)
		{
			System.out.println(ex.toString()+"====");
			System.out.println(ex.toString());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String erroMsg = sw.toString();
			ex.printStackTrace();
			monitorService.updateErroMsg(erroMsg, jobName);
		}
		
		
	}

	
}
