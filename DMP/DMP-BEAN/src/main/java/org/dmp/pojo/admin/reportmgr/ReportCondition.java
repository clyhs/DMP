package org.dmp.pojo.admin.reportmgr;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="ReportCondition")
@Table(name = "TD_S_REPORT_CONDITION")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportCondition implements Serializable{

	private static final long serialVersionUID = 3447534377741579347L;
	
	@Id
	@Column(name = "N_ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	private Integer id;
	@Column(name = "S_FORMAT")
	private String format;
	@Column(name = "S_CONDITION_NAME")
	private String conditionName;
	@Column(name = "S_COMPONENT_TYPE")
	private String componentType;
	@Column(name = "S_COMPONENT_ID")
	private String componentId;
	@Column(name = "N_LAYOUT")
	private long layout ;
	@Column(name = "S_COMBOX_FIELD")
	private String comboxField;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getConditionName() {
		return conditionName;
	}
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public long getLayout() {
		return layout;
	}
	public void setLayout(long layout) {
		this.layout = layout;
	}
	public String getComboxField() {
		return comboxField;
	}
	public void setComboxField(String comboxField) {
		this.comboxField = comboxField;
	}
	
}
