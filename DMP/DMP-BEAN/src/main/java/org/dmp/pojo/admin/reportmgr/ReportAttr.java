package org.dmp.pojo.admin.reportmgr;

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

@Entity(name="ReportAttr")
@Table(name = "TD_S_REPORT_ATTR")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportAttr {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "S_ID")
	private int id;
	@Column(name = "S_REPORTID")
	private String reportId;
	@Column(name = "S_NAME")
	private String name;
	@Column(name = "S_DATAINDEX")
	private String dataIndex;
	@Column(name = "S_SORTABLE")
	private String sortable;
	@Column(name = "N_WIDTH")
	private int width;
	@Column(name = "N_ORDER")
	private int order;
	@Column(name = "S_TYPE")
	private String type;
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public String getSortable() {
		return sortable;
	}
	public void setSortable(String sortable) {
		this.sortable = sortable;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
