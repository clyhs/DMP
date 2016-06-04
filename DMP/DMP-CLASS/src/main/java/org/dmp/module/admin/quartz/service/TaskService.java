package org.dmp.module.admin.quartz.service;

import java.util.List;
import javax.annotation.Resource;

import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.quartz.dao.TaskDao;
import org.dmp.pojo.admin.quartz.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("TaskService")
@Scope("prototype")
public class TaskService {

	@Resource(name = "TaskDao")
	private TaskDao taskDao;
	
	public List<Task> getTaskList(String name,int start, int limit){
		return taskDao.getTaskList(name,start,limit);
	}
	
	/**
	 * 通过taskCode获取数据
	 * @param taskCode
	 * @return
	 */
	public Task getTask(String taskCode)
	{
		return taskDao.getTask(taskCode);
	}
	
	/**
	 * 添加task
	 * @param taskCode
	 * @param name
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public int addTask(String taskCode,String name,String beginTime,String endTime) throws Exception
    {
		Task oTask = new Task();
		oTask.setTaskCode(taskCode);
		oTask.setBeginTime(StrUtil.StringToDate(beginTime, "yyyy-MM-dd HH:mm:ss"));
		oTask.setEndTime(StrUtil.StringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
		oTask.setName(name);
		int nTotal = taskDao.addTask(oTask);
		return nTotal;
    }
	
	
	/**
	 * 修改
	 * @param taskCode
	 * @param name
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public int editTask(String taskCode,String name,String beginTime,String endTime)throws Exception
	{
		Task oTask = new Task();
		oTask.setTaskCode(taskCode);
		oTask.setBeginTime(StrUtil.StringToDate(beginTime, "yyyy-MM-dd HH:mm:ss"));
		oTask.setEndTime(StrUtil.StringToDate(endTime, "yyyy-MM-dd HH:mm:ss"));
		oTask.setName(name);
		int nTotal = taskDao.editTask(oTask);
		return nTotal;
	}
	
	/**
	 * 删除
	 * @param sTaskIdList
	 * @return
	 */
	public int delTask(String sTaskIdList)
	{
		int nTotal = taskDao.delTaskList(sTaskIdList);
		return nTotal;
	}
}