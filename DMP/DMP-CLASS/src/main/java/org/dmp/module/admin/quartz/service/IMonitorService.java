package org.dmp.module.admin.quartz.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

public interface IMonitorService {

	public List getMonitorJob(String jobName);
	/**
	 * 获取监控列表
	 * @return
	 */
	public List getMonitors();
	
	/**
	 * 获取某个监控信息
	 * @param jobName
	 * @return
	 */
	public DynaBean getMonitor(String jobName);
	
	
	public void updateMonitorTime(Date prevFireTime,Date nextFireTime,Date startTime,String jobName);
	
	public void updateMonitorTime(int paramInt,Date prevFireTime,Date nextFireTime,float prevContTime,
			Date endTime,String jobName);
	
	public void updateErroMsg(String erroMsg,String jobName);
}
