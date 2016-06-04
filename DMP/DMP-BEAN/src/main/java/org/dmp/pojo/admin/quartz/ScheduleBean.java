package org.dmp.pojo.admin.quartz;

import java.io.Serializable;

public class ScheduleBean implements Serializable{

	private static final long serialVersionUID = 1L;
	  private String triggerName;
	  private String triggerGroup;
	  private int triggerState;
	  private String nextFireTime;
	  private String prevFireTime;
	  private String jobName;
	  private String jobGroup;
	  private String description;
	  private String cronString;
	  private int repeatCount;
	  private long repeatInterval;
	  private String startTime;
	  //private String startDate;
	  private String haveEndDate;
	  private String endDate;
	  private String actionRef;
	  private String version;
	  private String fileType;
	  private String repName;
	  private String userName;
	  private String passWord;
	  private String actionPath;
	  private int cycle;
	  private String cycleNum;
	  private String dayType;
	  private String monthType;
	  private String yearType;
	  private String dayNum;
	  private String weekNum;
	  private String monthNum;
	  
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerGroup() {
		return triggerGroup;
	}
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	public int getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(int triggerState) {
		this.triggerState = triggerState;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCronString() {
		return cronString;
	}
	public void setCronString(String cronString) {
		this.cronString = cronString;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public long getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getHaveEndDate() {
		return haveEndDate;
	}
	public void setHaveEndDate(String haveEndDate) {
		this.haveEndDate = haveEndDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getActionRef() {
		return actionRef;
	}
	public void setActionRef(String actionRef) {
		this.actionRef = actionRef;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getActionPath() {
		return actionPath;
	}
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public String getCycleNum() {
		return cycleNum;
	}
	public void setCycleNum(String cycleNum) {
		this.cycleNum = cycleNum;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public String getMonthType() {
		return monthType;
	}
	public void setMonthType(String monthType) {
		this.monthType = monthType;
	}
	public String getYearType() {
		return yearType;
	}
	public void setYearType(String yearType) {
		this.yearType = yearType;
	}
	public String getDayNum() {
		return dayNum;
	}
	public void setDayNum(String dayNum) {
		this.dayNum = dayNum;
	}
	public String getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}
	public String getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(String monthNum) {
		this.monthNum = monthNum;
	}

	  
}
