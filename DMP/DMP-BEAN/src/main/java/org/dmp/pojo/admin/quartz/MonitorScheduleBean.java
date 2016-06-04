package org.dmp.pojo.admin.quartz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="td_qrtz_monitor")
public class MonitorScheduleBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer Id;

	@Column(name = "JOBNAME")
	private String jobName;
	
	@Column(name = "JOBGROUP")
	private String jobGroup;
	
	@Column(name = "JOBFILE")
	private String jobFile;
	
	@Column(name = "JOBSTATUS")
	private String jobStatus;
	
	@Column(name = "NEXT_FIRE_TIME")
	private String nextFireTime;
	
	@Column(name = "PREV_FIRE_TIME")
	private String prevFireTime;
	
	@Column(name = "REPEAT_TIMES")
	private int repeatTime;
	
	@Column(name = "PREV_CONTINUED_TIME")
	private int prevConTime;
	
	@Column(name = "START_TIME")
	private String startTime;
	
	@Column(name = "END_TIME")
	private String endTime;
	
	@Column(name = "CONTINUED_TIME")
	private int conTime;
	
	@Column(name = "errmsg")
	private String errmsg;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobFile() {
		return jobFile;
	}
	public void setJobFile(String jobFile) {
		this.jobFile = jobFile;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getPrevFireTime() {
		return prevFireTime;
	}
	public void setPrevFireTime(String prevFireTime) {
		this.prevFireTime = prevFireTime;
	}
	public int getRepeatTime() {
		return repeatTime;
	}
	public void setRepeatTime(int repeatTime) {
		this.repeatTime = repeatTime;
	}
	public int getPrevConTime() {
		return prevConTime;
	}
	public void setPrevConTime(int prevConTime) {
		this.prevConTime = prevConTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getConTime() {
		return conTime;
	}
	public void setConTime(int conTime) {
		this.conTime = conTime;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	
}
