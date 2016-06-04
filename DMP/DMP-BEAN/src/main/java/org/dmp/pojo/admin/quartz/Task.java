package org.dmp.pojo.admin.quartz;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Task")
@Table(name = "TD_S_TASK")
public class Task {
	@Id  
	@Column(name = "S_TASK_CODE")
	private String taskCode;
	@Column(name = "S_NAME")
	private String name;
	@Column(name = "D_BEGIN_TIME")
	private Date beginTime;
	@Column(name = "D_END_TIME")
	private Date endTime;
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
