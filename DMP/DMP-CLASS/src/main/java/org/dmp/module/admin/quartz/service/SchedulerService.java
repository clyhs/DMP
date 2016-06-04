package org.dmp.module.admin.quartz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.quartz.dao.MonitorDao;
import org.dmp.module.admin.quartz.kettle.KettleEngineImpl;
import org.dmp.module.admin.quartz.kettle.QuartzExecute;
import org.dmp.pojo.admin.quartz.ScheduleBean;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;



@Service("SchedulerService")
@Scope("prototype")
public class SchedulerService implements ISchedulerService{

	private final static Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	
	private Scheduler  scheduler;
	
	
	public Scheduler getScheduler() {
		return scheduler;
	}

	@Autowired
	@Qualifier("scheduler")
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Resource(name= "MonitorDao")
	private MonitorDao monitorDao;
	
	public List<ScheduleBean> getList()
	{
		ArrayList<ScheduleBean>  list = new ArrayList<ScheduleBean>();
		try{
			String[] str = scheduler.getJobGroupNames();
			for(int i = 0;i<str.length;i++)
			{
				String[] names =  scheduler.getJobNames(str[i]);
				
				for(int j=0;j<names.length;j++)
				{
					JobDetail jobDetail =  scheduler.getJobDetail(names[j], str[i]);
					JobDataMap jobDataMap = jobDetail.getJobDataMap();
					Trigger[] triggers = scheduler.getTriggersOfJob(names[j], str[i]);
					Trigger trigger = null;
					if(triggers.length>0)
					{
						for(int k =0;k<triggers.length;k++)
						{
							trigger = triggers[k];
							ScheduleBean scheduleBean = new ScheduleBean();
							scheduleBean.setJobGroup(trigger.getJobGroup());
							scheduleBean.setJobName(trigger.getJobName());
							scheduleBean.setTriggerGroup(trigger.getGroup());
							scheduleBean.setTriggerName(trigger.getName());
							scheduleBean.setActionPath(jobDataMap.getString("actionPath"));
							scheduleBean.setActionRef(jobDataMap.getString("actionRef"));
							scheduleBean.setFileType(jobDataMap.getString("fileType"));
							scheduleBean.setVersion(jobDataMap.getString("version"));
							scheduleBean.setRepName(jobDataMap.getString("repName"));
							scheduleBean.setDescription(jobDetail.getDescription());
							scheduleBean.setTriggerState(scheduler.getTriggerState(trigger.getName(), trigger.getGroup()));
							scheduleBean.setNextFireTime(StrUtil.getDateFormat(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
							scheduleBean.setPrevFireTime(StrUtil.getDateFormat(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss"));
							scheduleBean.setEndDate(StrUtil.getDateFormat(trigger.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
							
							if ((trigger instanceof CronTrigger))
				            {
								scheduleBean.setCronString(((CronTrigger)trigger).getCronExpression());
				            }
				            else if ((trigger instanceof SimpleTrigger))
				            {
				            	scheduleBean.setRepeatInterval(((SimpleTrigger)trigger).getRepeatInterval());
				            	scheduleBean.setRepeatCount(((SimpleTrigger)trigger).getRepeatCount());
				            }
							list.add(scheduleBean);
						}
						
					}
					else
					{
						ScheduleBean scheduleBean = new ScheduleBean();
						scheduleBean.setJobGroup(jobDetail.getGroup());
						scheduleBean.setJobName(jobDetail.getName());
						scheduleBean.setActionPath(jobDataMap.getString("actionPath"));
						scheduleBean.setActionRef(jobDataMap.getString("actionRef"));
						scheduleBean.setFileType(jobDataMap.getString("fileType"));
						scheduleBean.setVersion(jobDataMap.getString("version"));
						scheduleBean.setRepName(jobDataMap.getString("repName"));
						scheduleBean.setDescription(jobDetail.getDescription());
						scheduleBean.setTriggerState(-1);
						list.add(scheduleBean);
					}
				}
			}
			
		}catch(Exception ex)
		{
			logger.info("SchedulerService.getList()" +ex.toString());
		}
		
		return list;
	}
	
	public void add(ScheduleBean paramScheduleBean)
	{
		JobDataMap jobDataMap = null;
	    JobDetail jobDetail = new JobDetail(paramScheduleBean.getJobName(), paramScheduleBean.getJobGroup(),QuartzExecute.class);
	    jobDetail.setDurability(true);
	    if (null != paramScheduleBean.getDescription())
	    {
	    	jobDetail.setDescription(paramScheduleBean.getDescription());
	    }
	    jobDataMap = jobDetail.getJobDataMap();
    	jobDataMap.put("actionRef", paramScheduleBean.getActionRef());
    	jobDataMap.put("actionPath", paramScheduleBean.getActionPath());
	    jobDataMap.put("repeat-time-millisecs", paramScheduleBean.getRepeatInterval());
	    jobDataMap.put("joGroup", paramScheduleBean.getJobGroup());
	    jobDataMap.put("repeat-count", paramScheduleBean.getRepeatCount());
	    jobDataMap.put("requestedMimeType", "text/xml");
	    jobDataMap.put("description", paramScheduleBean.getDescription());
	    jobDataMap.put("jobName", paramScheduleBean.getJobName());
	    jobDataMap.put("version", paramScheduleBean.getVersion());
	    jobDataMap.put("fileType", paramScheduleBean.getFileType());
	    jobDataMap.put("repName", paramScheduleBean.getRepName());
	    jobDataMap.put("version", paramScheduleBean.getVersion());
	    jobDataMap.put("userName", paramScheduleBean.getUserName());
	    jobDataMap.put("passWord", paramScheduleBean.getPassWord());
	    jobDataMap.put("startTime", paramScheduleBean.getStartTime());
	    jobDataMap.put("haveEndDate", paramScheduleBean.getHaveEndDate());
	    jobDataMap.put("endDate", paramScheduleBean.getEndDate());
	    jobDataMap.put("cycle", paramScheduleBean.getCycle());
	    jobDataMap.put("cycleNum", paramScheduleBean.getCycleNum());
	    jobDataMap.put("dayType", paramScheduleBean.getDayType());
	    jobDataMap.put("monthType", paramScheduleBean.getMonthType());
	    jobDataMap.put("yearType", paramScheduleBean.getYearType());
	    jobDataMap.put("dayNum", paramScheduleBean.getDayNum());
	    jobDataMap.put("weekNum", paramScheduleBean.getWeekNum());
	    jobDataMap.put("monthNum", paramScheduleBean.getMonthNum());
	    jobDataMap.put("processId", SchedulerService.class.getName());
	    try
	    {
	    	Date tests = StrUtil.StringToDate(paramScheduleBean.getStartTime(), "yyyy-MM-dd HH:mm:ss");
	        Date date = paramScheduleBean.getEndDate() == null ? null : StrUtil.StringToDate(paramScheduleBean.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
	        if ((paramScheduleBean.getCronString() == null) || ("".equals(paramScheduleBean.getCronString()))){
	    	    SimpleTrigger st = new SimpleTrigger(paramScheduleBean.getTriggerName(), paramScheduleBean.getTriggerGroup(),
	    			  paramScheduleBean.getJobName(), paramScheduleBean.getJobGroup(), StrUtil.StringToDate(paramScheduleBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"), date, paramScheduleBean.getRepeatCount(), paramScheduleBean.getRepeatInterval());
	    	    scheduler.scheduleJob(jobDetail, st);
	        }else{
	    	    CronTrigger ct = new CronTrigger(paramScheduleBean.getTriggerName(), paramScheduleBean.getTriggerGroup(), 
	    			  paramScheduleBean.getJobName(), paramScheduleBean.getJobGroup(), StrUtil.StringToDate(paramScheduleBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"), date, paramScheduleBean.getCronString(), TimeZone.getDefault());
	    	    scheduler.scheduleJob(jobDetail, ct);
	        }
	        monitorDao.add(paramScheduleBean, date);
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.add" +ex.toString());
	    }
	}
	
	public void update(ScheduleBean paramScheduleBean,String jobName)
	{
		try
		{
			JobDetail jobDetail = scheduler.getJobDetail(jobName, "DEFAULT");
			jobDetail.setName(paramScheduleBean.getJobName());
			jobDetail.setDurability(true);
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			if (null != paramScheduleBean.getDescription())
		    {
		    	jobDetail.setDescription(paramScheduleBean.getDescription());
		    }
			jobDataMap.put("actionRef", paramScheduleBean.getActionRef());
			jobDataMap.put("actionPath", paramScheduleBean.getActionPath());
		    jobDataMap.put("repeat-time-millisecs", paramScheduleBean.getRepeatInterval());
		    jobDataMap.put("joGroup", paramScheduleBean.getJobGroup());
		    jobDataMap.put("repeat-count", paramScheduleBean.getRepeatCount());
		    jobDataMap.put("requestedMimeType", "text/xml");
		    jobDataMap.put("description", paramScheduleBean.getDescription());
		    jobDataMap.put("jobName", paramScheduleBean.getJobName());
		    jobDataMap.put("version", paramScheduleBean.getVersion());
		    jobDataMap.put("fileType", paramScheduleBean.getFileType());
		    jobDataMap.put("repName", paramScheduleBean.getRepName());
		    jobDataMap.put("version", paramScheduleBean.getVersion());
		    jobDataMap.put("userName", paramScheduleBean.getUserName());
		    jobDataMap.put("passWord", paramScheduleBean.getPassWord());
		    jobDataMap.put("startTime", paramScheduleBean.getStartTime());
		    jobDataMap.put("haveEndDate", paramScheduleBean.getHaveEndDate());
		    jobDataMap.put("endDate", paramScheduleBean.getEndDate());
		    jobDataMap.put("cycle", paramScheduleBean.getCycle());
		    jobDataMap.put("cycleNum", paramScheduleBean.getCycleNum());
		    jobDataMap.put("dayType", paramScheduleBean.getDayType());
		    jobDataMap.put("monthType", paramScheduleBean.getMonthType());
		    jobDataMap.put("yearType", paramScheduleBean.getYearType());
		    jobDataMap.put("dayNum", paramScheduleBean.getDayNum());
		    jobDataMap.put("weekNum", paramScheduleBean.getWeekNum());
		    jobDataMap.put("monthNum", paramScheduleBean.getMonthNum());
		    jobDataMap.put("processId", SchedulerService.class.getName());
		    scheduler.deleteJob(jobName, "DEFAULT");
		    Date nextFireTime = paramScheduleBean.getEndDate() == null ? null : StrUtil.StringToDate(paramScheduleBean.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
	        if ((paramScheduleBean.getCronString() == null) || ("".equals(paramScheduleBean.getCronString()))){
	    	    SimpleTrigger st = new SimpleTrigger(paramScheduleBean.getTriggerName(), paramScheduleBean.getTriggerGroup(),
	    			  paramScheduleBean.getJobName(), paramScheduleBean.getJobGroup(), StrUtil.StringToDate(paramScheduleBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"), nextFireTime, paramScheduleBean.getRepeatCount(), paramScheduleBean.getRepeatInterval());
	    	    scheduler.scheduleJob(jobDetail, st);
	        }else{
	    	    CronTrigger ct = new CronTrigger(paramScheduleBean.getTriggerName(), paramScheduleBean.getTriggerGroup(), 
	    			  paramScheduleBean.getJobName(), paramScheduleBean.getJobGroup(), StrUtil.StringToDate(paramScheduleBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"), nextFireTime, paramScheduleBean.getCronString(), TimeZone.getDefault());
	    	    scheduler.scheduleJob(jobDetail, ct);
	       }
	       monitorDao.updateMonitor(paramScheduleBean, jobName, nextFireTime);
		}
		catch(Exception ex)
		{
			logger.info("SchedulerService.update" +ex.toString());
		}
		
	}
	
	public void delete(String[] paramStrs)
	{
		try
	    {
	      for (int i = 0; i < paramStrs.length; i++)
	      {
	    	scheduler.unscheduleJob(paramStrs[i], "DEFAULT");
	    	monitorDao.updateStatus(paramStrs[i],monitorDao.STATUS_FINISHED);
	      }
	      return;
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.delete" +ex.toString());
	    }
	}
	
	public void pause(String[] paramStrs)
	{
		try
	    {
	      for (int i = 0; i < paramStrs.length; i++)
	      {
	    	  ScheduleBean scheduler = this.getSchedule(paramStrs[i]);
	    	  if(scheduler.getFileType().equals("ktr"))
	    	  {
	    		  KettleEngineImpl.stopTrans(scheduler.getJobName());
	    	  }else if(scheduler.getFileType().equals("kjb"))
	    	  {
	    		  KettleEngineImpl.stopJob(scheduler.getJobName());
	    	  }
	      }
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.pause" +ex.toString());
	    }
	}
	
	public void resume(String[] paramStrs)
	{
		try
	    {
	      for (int i = 0; i < paramStrs.length; i++)
	      {
	    	scheduler.resumeJob(paramStrs[i], "DEFAULT");
	        if ((scheduler.getTriggerState(paramStrs[i] , "DEFAULT")) == 0){
	        	//MonitorUtil.a(paramArrayOfString[i], "等待启动");
	        	monitorDao.updateStatus(paramStrs[i],monitorDao.STATUS_WAITING);
	        }
	          
	      }
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.resume" +ex.toString());
	    }
	}
	
	public void run(String[] paramStrs)
	{
		try
	    {
	      for (int i = 0; i < paramStrs.length; i++){
	    	  scheduler.triggerJob(paramStrs[i], "DEFAULT");
	      }
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.run" +ex.toString());
	    }
	}
	
	public void completedelete(String[] paramStrs)
	{
		try
	    {
	      for (int i = 0; i < paramStrs.length; i++)
	      {
	    	 scheduler.deleteJob(paramStrs[i], "DEFAULT");
	    	 monitorDao.delete(paramStrs[i]);
	      }
	    }
	    catch (Exception ex)
	    {
	    	logger.info("SchedulerService.completedelete" +ex.toString());
	    }
	}
	
	public JobDetail getJobDetail(String jobName)
	{
		try
		{
			return scheduler.getJobDetail(jobName, "DEFAULT");
		}
		catch(Exception ex)
		{
			logger.info("SchedulerService.getJobDetail" +ex.toString());
		}
		return null;
	}
	
	public ScheduleBean getSchedule(String jobName)
	{
		ScheduleBean scheduleBean = new ScheduleBean();
		JobDetail jobDetail = this.getJobDetail(jobName);
		if(jobDetail!=null)
		{
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			scheduleBean.setActionPath(jobDataMap.getString("actionPath"));
			scheduleBean.setActionRef(jobDataMap.getString("actionRef"));
			scheduleBean.setCronString(jobDataMap.getString("cronString"));
			scheduleBean.setCycle(jobDataMap.get("cycle") == null ? 0 : jobDataMap.getInt("cycle"));
			scheduleBean.setCycleNum(jobDataMap.getString("cycleNum"));
			scheduleBean.setDayNum(jobDataMap.getString("dayNum"));
			scheduleBean.setDayType(jobDataMap.getString("dayType"));
			scheduleBean.setDescription(jobDataMap.getString("description"));
			scheduleBean.setEndDate(jobDataMap.getString("endDate"));
			scheduleBean.setFileType(jobDataMap.getString("fileType"));
			scheduleBean.setHaveEndDate(jobDataMap.getString("haveEndDate"));
			scheduleBean.setJobGroup(jobDataMap.getString("jobGroup"));
			scheduleBean.setJobName(jobDataMap.getString("jobName"));
			scheduleBean.setMonthNum(jobDataMap.getString("monthNum"));
			scheduleBean.setMonthType(jobDataMap.getString("monthType"));
			scheduleBean.setNextFireTime(jobDataMap.getString("nextFireTime"));
		    scheduleBean.setPassWord(jobDataMap.getString("passWord"));
		    scheduleBean.setPrevFireTime(jobDataMap.getString("prevFireTime"));
		    scheduleBean.setRepeatCount(jobDataMap.get("repeatCount") == null ? 0 : jobDataMap.getInt("repeatCount"));
		    scheduleBean.setRepeatInterval(jobDataMap.get("repeatInterval") == null ? 0L : jobDataMap.getLong("repeatInterval"));
		    scheduleBean.setRepName(jobDataMap.getString("repName"));
		    scheduleBean.setStartTime(jobDataMap.getString("startTime"));
		    scheduleBean.setTriggerGroup(jobDataMap.getString("triggerGroup"));
		    scheduleBean.setTriggerName(jobDataMap.getString("triggerName"));
		    scheduleBean.setTriggerState(jobDataMap.get("triggerState") == null ? 0 : jobDataMap.getInt("triggerState"));
		    scheduleBean.setUserName(jobDataMap.getString("userName"));
		    scheduleBean.setVersion(jobDataMap.getString("version"));
		    scheduleBean.setWeekNum(jobDataMap.getString("weekNum"));
		    scheduleBean.setYearType(jobDataMap.getString("yearType"));
		}
		return scheduleBean;
	}
    
	/**
	 * 检测任务的唯一性
	 * @param jobName
	 * @return
	 */
	public List<DynaBean> checkJob(String jobName){
		return monitorDao.checkJob(jobName);
	}
}
