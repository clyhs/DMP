package org.dmp.module.admin.quartz.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.admin.quartz.dao.MonitorDao;
import org.dmp.module.admin.quartz.kettle.KettleEngineImpl;
import org.quartz.JobDataMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("MonitorService")
@Scope("prototype")
public class MonitorService implements IMonitorService{
	
	
	@Resource(name= "SchedulerService")
	private ISchedulerService schedulerService;
	
	
	
	@Resource(name= "MonitorDao")
	private MonitorDao monitorDao;
	/**
	 * 查看某个JOB的监控
	 * @param jobName
	 * @return
	 */
	public List getMonitorJob(String jobName)
	{
		JobDataMap jobDataMap = schedulerService.getJobDetail(jobName).getJobDataMap();
		KettleEngineImpl kettleEngine = new KettleEngineImpl();
		String username = jobDataMap.getString("passWord");
		return kettleEngine.getLogData(jobDataMap.getString("repName"), jobDataMap.getString("userName"), jobDataMap.getString("passWord"),
				jobDataMap.getString("actionPath"), jobDataMap.getString("actionRef"), jobDataMap.getString("fileType"));
	}
	/**
	 * 获取监控列表
	 * @return
	 */
	public List getMonitors()
	{
		return monitorDao.getList();
	}
	
	/**
	 * 获取某个监控信息
	 * @param jobName
	 * @return
	 */
	public DynaBean getMonitor(String jobName)
	{
		return monitorDao.getMonitor(jobName);
	}
	
	
	public void updateMonitorTime(Date prevFireTime,Date nextFireTime,Date startTime,String jobName)
	{
		monitorDao.updateMonitorTime(prevFireTime, nextFireTime, startTime, jobName);
	}
	
	public void updateMonitorTime(int paramInt,Date prevFireTime,Date nextFireTime,float prevContTime,
			Date endTime,String jobName)
	{
		monitorDao.updateMonitorTime(paramInt, prevFireTime, nextFireTime, prevContTime, endTime, jobName);
	}
	
	public void updateErroMsg(String erroMsg,String jobName)
	{
		DynaBean db = monitorDao.getMonitor(jobName);
		monitorDao.updateErroMsg(erroMsg, jobName);
	}
}
