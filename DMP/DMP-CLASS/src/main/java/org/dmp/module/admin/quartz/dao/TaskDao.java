package org.dmp.module.admin.quartz.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.quartz.Task;
import org.dmp.pojo.admin.reportmgr.Report;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;



@Repository("TaskDao")
@Scope("prototype")
public class TaskDao extends HBaseDao<Report>{
	@SuppressWarnings("unchecked")
	public List<Task> getTaskList(String name,int start, int limit){
		String sql = "from Task where name like '%"+ name +"%' order by taskCode asc";
		if(start ==0 && limit == 0){
			return super.select(sql);
		}else{
			return super.select(sql,start, limit);	
		}
		
	}
	
	/**
	 * 通过taskCode获取数据
	 * @param taskCode
	 * @return
	 */
	public Task getTask(String taskCode)
	{
		List<Task> task = super.select("FROM Task WHERE taskCode = ?", new Field().addStr(taskCode));
		return (task != null && task.size() > 0) ? task.get(0) : null;
	}
	/**
	 * 添加
	 * @param oTask
	 * @return
	 */
	public int addTask(Task oTask)
	{
		return super.insert(oTask);
	}
	
    /**
     * 修改
     * @param oTask
     * @return
     */
	public int editTask(Task oTask)
	{
		return super.update(oTask);
	}
	/**
	 * 删除
	 * @param sTaskIdList
	 * @return
	 */
	public int delTaskList(String sTaskIdList)
	{
		return super.delete("DELETE Task WHERE taskCode IN ("+sTaskIdList+")");
	}
}
