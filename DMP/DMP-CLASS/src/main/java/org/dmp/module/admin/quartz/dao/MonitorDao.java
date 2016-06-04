package org.dmp.module.admin.quartz.dao;


import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.core.util.StrUtil;
import org.dmp.module.common.db.JBaseDao;
import org.dmp.pojo.admin.quartz.ScheduleBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/*
 * mysql str_to_date(?, '%Y-%m-%d %k:%i:%s')
 * oracle to_date(?, 'yyyy-mm-dd hh24:mi:ss')
 */
@Repository("MonitorDao")
@Scope("prototype")
public class MonitorDao extends JBaseDao{
	
	public static final String STATUS_WAITING = "等待启动";
    public static final String STATUS_RUNNING = "正在运行";
    public static final String STATUS_BLOCKED = "暂停";
    public static final String STATUS_FINISHED = "已结束";
    public static final String STATUS_DELETED = "已结束";
    public static final String STATUS_ERROR = "执行出错";

    /**
     * 增加监控
     * @param paramScheduleBean
     * @param paramDate
     */
	public void add(ScheduleBean paramScheduleBean, Date paramDate)
	{
		Field oField = new Field();
		StringBuffer sql ;
		if(paramDate == null){
			sql = new StringBuffer(" INSERT INTO td_qrtz_monitor(JOBNAME,JOBGROUP,JOBFILE,JOBSTATUS, ");
			sql.append(" REPEAT_TIMES,PREV_CONTINUED_TIME,CONTINUED_TIME )");
			sql.append("  VALUES(?,?,?,?,?,?,?) ");
			
		}else{
			sql = new StringBuffer(" INSERT INTO td_qrtz_monitor(NEXT_FIRE_TIME,JOBNAME,JOBGROUP,JOBFILE,JOBSTATUS, ");
			sql.append(" REPEAT_TIMES,PREV_CONTINUED_TIME,CONTINUED_TIME )");
			sql.append("  VALUES(str_to_date(?, '%Y-%m-%d %k:%i:%s'),?,?,?,?,?,?,?) ");
			//oField.addDate(new Timestamp(paramDate.getTime()));
			oField.addStr(StrUtil.getDateFormat(paramDate, "yyyy-MM-dd HH:mm:ss"));
		}
		oField.addStr(paramScheduleBean.getJobName());
		oField.addStr(paramScheduleBean.getJobGroup());
		String path = paramScheduleBean.getActionPath() + System.getProperty("file.separator");
		oField.addStr(path + paramScheduleBean.getActionRef() + "." + paramScheduleBean.getFileType());
		oField.addStr(STATUS_WAITING);
		oField.addInt(0);
		oField.addInt(0);
		oField.addInt(0);
		m_oDB.insert(sql.toString(), oField);
	}
	
	 /**
	  * 更新监控状态
	  * @param jobName
	  * @param jobStatus
	  */
	public void updateStatus(String jobName,String jobStatus)
	{
		String sql  = " UPDATE td_qrtz_monitor SET JOBSTATUS = '" + jobStatus +"' WHERE JOBNAME = '" + jobName + "'";
		m_oDB.update(sql);
	} 
	
	/**
	 * 删除监控信息
	 * @param jobName
	 */
	public void delete(String jobName)
	{
		String sql = "DELETE FROM td_qrtz_monitor WHERE JOBNAME='" + jobName + "'";
		m_oDB.delete(sql);
	}
	
	public List<DynaBean> checkJob(String jobName)
	{
		String sql = "select * FROM td_qrtz_monitor WHERE JOBNAME='" + jobName + "'";
		return m_oDB.select(sql);
	}
	
	/**
	 * 修改监控信息
	 * @param paramScheduleBean
	 * @param jobName
	 * @param nextFireTime
	 */
	public void updateMonitor(ScheduleBean paramScheduleBean,String jobName,Date nextFireTime)
	{
		StringBuffer sql;
		Field oField = new Field();
		if(nextFireTime == null){
			sql = new StringBuffer(" UPDATE td_qrtz_monitor SET NEXT_FIRE_TIME = null, JOBNAME=?,JOBFILE=?,JOBSTATUS=?,");
		}else{
			sql = new StringBuffer(" UPDATE td_qrtz_monitor SET NEXT_FIRE_TIME = str_to_date(?, '%Y-%m-%d %k:%i:%s'), JOBNAME=?,JOBFILE=?,JOBSTATUS=?,");
			oField.addStr(StrUtil.getDateFormat(nextFireTime, "yyyy-MM-dd HH:mm:ss"));
		}
		sql.append(" REPEAT_TIMES=?,PREV_CONTINUED_TIME=?,CONTINUED_TIME=? WHERE JOBNAME=?");
		oField.addStr(paramScheduleBean.getJobName());
		String path = paramScheduleBean.getActionPath() + System.getProperty("file.separator");
		oField.addStr(path + paramScheduleBean.getActionRef() + "." + paramScheduleBean.getFileType());
		oField.addStr(STATUS_WAITING);
		oField.addInt(0);
		oField.addInt(0);
		oField.addInt(0);
		oField.addStr(jobName);
		m_oDB.update(sql.toString(), oField);
	}
	
	/**
	 * 获取监控列表
	 * @return
	 */
	public List<DynaBean> getList()
	{
		StringBuffer sql  = new StringBuffer("SELECT * FROM td_qrtz_monitor ORDER BY JOBSTATUS DESC,NEXT_FIRE_TIME ASC");
		return m_oDB.select(sql.toString());
	}
	
	/**
	 * 根据jobname更新监控时间以及对应状态
	 * @param prevFireTime
	 * @param nextFireTime
	 * @param startTime
	 * @param jobName
	 */
	public void updateMonitorTime(Date prevFireTime,Date nextFireTime,Date startTime,String jobName)
	{
		StringBuffer sql = new StringBuffer("");
		Field oField = new Field();
		if(nextFireTime != null)
		{
			sql.append("UPDATE td_qrtz_monitor SET PREV_FIRE_TIME=str_to_date(?, '%Y-%m-%d %k:%i:%s'),NEXT_FIRE_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),START_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),END_TIME = null, errmsg= null,JOBSTATUS=? WHERE JOBNAME=?");
			oField.addStr(StrUtil.getDateFormat(prevFireTime, "yyyy-MM-dd HH:mm:ss"));
			oField.addStr(StrUtil.getDateFormat(nextFireTime, "yyyy-MM-dd HH:mm:ss"));
			oField.addStr(StrUtil.getDateFormat(startTime, "yyyy-MM-dd HH:mm:ss"));
			oField.addStr(this.STATUS_RUNNING);
			oField.addStr(jobName);
			
		}
		else
		{
			sql.append("UPDATE td_qrtz_monitor SET START_TIME=str_to_date(?, '%Y-%m-%d %k:%i:%s'),END_TIME=null,JOBSTATUS=?, errmsg= null WHERE JOBNAME=?");
			oField.addStr(StrUtil.getDateFormat(startTime, "yyyy-MM-dd HH:mm:ss"));
			oField.addStr(this.STATUS_RUNNING);
			oField.addStr(jobName);
		}
		
		m_oDB.update(sql.toString(),oField);
	}
	
	/**
	 * 更新监控时间以及对应状态
	 * @param paramInt
	 * @param prevFireTime
	 * @param nextFireTime
	 * @param prevContTime
	 * @param endTime
	 * @param jobName
	 */
	public void updateMonitorTime(int paramInt,Date prevFireTime,Date nextFireTime,float prevContTime,
			Date endTime,String jobName)
	{
		StringBuffer sql = new StringBuffer("");
		Field oField = new Field();
		if(nextFireTime != null)
		{
			sql.append("UPDATE td_qrtz_monitor SET PREV_FIRE_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),");
			oField.addStr(StrUtil.getDateFormat(prevFireTime, "yyyy-MM-dd HH:mm:ss"));
			switch(paramInt)
			{
			case 1:
				sql.append(" NEXT_FIRE_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'), END_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s')," +
						"JOBSTATUS= ,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
				oField.addStr(StrUtil.getDateFormat(nextFireTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(this.STATUS_BLOCKED);
				break;
			case 0:
				sql.append(" NEXT_FIRE_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'), END_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),JOBSTATUS=?,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
				oField.addStr(StrUtil.getDateFormat(nextFireTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(this.STATUS_WAITING);
				break;
			case 2:
				sql.append(" NEXT_FIRE_TIME=null,END_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),JOBSTATUS=?,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
				oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(this.STATUS_FINISHED);
				break;
			case -1:
				sql.append(" NEXT_FIRE_TIME=null,END_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),JOBSTATUS=?,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
				oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(this.STATUS_FINISHED);
				break;
			default:
				sql.append(" NEXT_FIRE_TIME=null,END_TIME= str_to_date(?, '%Y-%m-%d %k:%i:%s'),JOBSTATUS=?,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
				oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
				oField.addStr(this.STATUS_FINISHED);
			}
			oField.addFloat(prevContTime);
			oField.addStr(jobName);
				
		}
		else
		{
			sql.append("UPDATE td_qrtz_monitor SET END_TIME=str_to_date(?, '%Y-%m-%d %k:%i:%s'),JOBSTATUS=?,PREV_CONTINUED_TIME=?,REPEAT_TIMES=REPEAT_TIMES+1 WHERE JOBNAME=?");
			oField.addStr(StrUtil.getDateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
			switch(paramInt)
			{
			case 1:
				oField.addStr(this.STATUS_BLOCKED);
				break;
			case 0:
				oField.addStr(this.STATUS_WAITING);
				break;
			case 2:
				oField.addStr(this.STATUS_FINISHED);
				break;
			case -1:
				oField.addStr(this.STATUS_FINISHED);
				break;
			default:
				oField.addStr(this.STATUS_FINISHED);
			}
			oField.addFloat(prevContTime);
			oField.addStr(jobName);
		}
		
		m_oDB.update(sql.toString(),oField);
	}
	
	/**
	 * 根据Jobname获取监控信息
	 * @param jobName
	 * @return
	 */
	public DynaBean getMonitor(String jobName)
	{
		StringBuffer sql = new StringBuffer(" SELECT * FROM td_qrtz_monitor WHERE JOBNAME= '");
		sql.append(jobName+"'");
		List<DynaBean> list  = m_oDB.select(sql.toString());
		if(null != list)
		{
			return list.get(0);
		}
		
		return null;
		
	}
	
	/**
	 * 更新指定监控信息的错误日志
	 * @param erroMsg
	 * @param jobName
	 */
	public void updateErroMsg(String erroMsg,String jobName)
	{
		StringBuffer sql = new StringBuffer("UPDATE td_qrtz_monitor SET ERRMSG=? ,JOBSTATUS='执行出错' WHERE JOBNAME='");
		sql.append(jobName+"'");
		Field oField = new Field();
		oField.addStr(erroMsg);
		m_oDB.update(sql.toString(),oField);
	}

	public static void main(String[] args) {
		MonitorDao monitorDao = new MonitorDao();
		monitorDao.updateMonitorTime(new Date(),new Date(),new Date(),"test");
	}
}
