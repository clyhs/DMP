package org.dmp.pojo.admin.reportmgr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name="ReportQuota")
@Table(name = "TD_S_REPORT_QUOTA")
public class ReportQuota {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_ID")
	private int sId;
	
	@Column(name = "S_REPORTTABLE")
	private String reportTable;
	
	@Column(name = "S_COLUMNNAME")
	private String columnName;
	
	@Column(name = "S_CONFIGTYPE")
	private String configType = "none";
	
	@Column(name = "N_MINVALUE")
	private long minValue;
	
	@Column(name = "N_MAXVALUE")
	private long maxValue ;

	public ReportQuota(){
		
	}
	
	public ReportQuota(int sId, String reportTable, String columnName,
			String configType, long minValue, long maxValue) {
		super();
		this.sId = sId;
		this.reportTable = reportTable;
		this.columnName = columnName;
		this.configType = configType;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public int getsId() {
		return sId;
	}

	public void setsId(int sId) {
		this.sId = sId;
	}

	public String getReportTable() {
		return reportTable;
	}

	public void setReportTable(String reportTable) {
		this.reportTable = reportTable;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public long getMinValue() {
		return minValue;
	}

	public void setMinValue(long minValue) {
		this.minValue = minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}
	
}