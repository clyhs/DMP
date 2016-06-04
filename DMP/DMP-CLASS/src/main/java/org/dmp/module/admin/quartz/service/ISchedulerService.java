package org.dmp.module.admin.quartz.service;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.pojo.admin.quartz.ScheduleBean;
import org.quartz.JobDetail;

public interface ISchedulerService {

	/**
	 * 获取调度任务
	 * @return
	 */
	public List<ScheduleBean> getList();
	
	/**
	 * 增加调度
	 * @param paramScheduleBean
	 */
	public void add(ScheduleBean paramScheduleBean);
	
	/**
	 * 更新调度
	 * @param paramScheduleBean
	 * @param jobName
	 */
	public void update(ScheduleBean paramScheduleBean,String jobName);
	
	/**
	 * 从调度中删除
	 * @param paramStrs
	 */
	public void delete(String[] paramStrs);
	
	/**
	 * 暂停调度
	 * @param paramStrs
	 */
	public void pause(String[] paramStrs);
	
	/**
	 * 重新运行
	 * @param paramStrs
	 */
	public void resume(String[] paramStrs);
	
	/**
	 * 运行调度
	 * @param paramStrs
	 */
	public void run(String[] paramStrs);
	
	/**
	 * 从库表中删除该调度
	 * @param paramStrs
	 */
	public void completedelete(String[] paramStrs);
	
	/**
	 * 获取某个调度详细信息
	 * @param jobName
	 * @return
	 */
	public JobDetail getJobDetail(String jobName);
	
	/**
	 * 获取调度信息
	 * @param jobName
	 * @return
	 */
	public ScheduleBean getSchedule(String jobName);
	
	/**
	 * 检测任务的唯一性
	 * @param jobName
	 * @return
	 */
	public List<DynaBean> checkJob(String jobName);
	
}
