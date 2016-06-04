package org.dmp.pojo.admin.reportmgr;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="ReportEvent")
@Table(name = "TD_S_REPORT_EVENT")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_ID")
	private int id;
	@Column(name = "S_REPORTID")
	private String reportId;
	@Column(name = "S_EVENT")
	private String event;
	@Column(name = "S_EVENT_NAME")
	private String eventName;
	@Column(name = "S_CHART_URL")
	private String chartUrl;
	@Column(name = "D_UPDATE_TIME")
	private Date updateTime;
	@Column(name = "S_UPDATE_STAFF_ID")
	private String updateStaffId;
	@Column(name = "N_LAYOUT")
	private int layout;
	public int getLayout() {
		return layout;
	}
	public void setLayout(int layout) {
		this.layout = layout;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getChartUrl() {
		return chartUrl;
	}
	public void setChartUrl(String chartUrl) {
		this.chartUrl = chartUrl;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateStaffId() {
		return updateStaffId;
	}
	public void setUpdateStaffId(String updateStaffId) {
		this.updateStaffId = updateStaffId;
	}
	
}
