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

@Entity(name="ReportCond")
@Table(name = "TD_S_REPORT_COND")
public class ReportCond {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_ID")
	private int id;
	@Column(name = "S_REPORTID")
	private String reportId;
	@Column(name = "N_LAYOUT")
	private int layout;
	@Column(name = "D_UPDATE_TIME")
	private Date updateTime;
	@Column(name = "S_UPDATE_STAFF_ID")
	private String updateStaffId;
	@Column(name = "S_CONDITION_NAME")
	private String conditionName;
	@Column(name = "S_COMPONENT_TYPE")
	private String componentType;
	@Column(name = "S_COMPONENT_ID")
	private String componentId;
	@Column(name = "S_FORMAT")
	private String format;
	@Column(name = "S_COMBOX_FIELD")
	private String comboxField;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public int getLayout() {
		return layout;
	}
	public void setLayout(int layout) {
		this.layout = layout;
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
	public String getConditionName() {
		return conditionName;
	}
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
	public String getComboxField() {
		return comboxField;
	}
	public void setComboxField(String comboxField) {
		this.comboxField = comboxField;
	}

}
