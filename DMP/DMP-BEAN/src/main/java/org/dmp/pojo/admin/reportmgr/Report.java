package org.dmp.pojo.admin.reportmgr;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="Report")
@Table(name = "TD_S_REPORT")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Report {
	@Id  
	@Column(name = "S_REPORTID")
	private String reportId;
	@Column(name = "S_REPORTNAME")
	private String reportName;
	@Column(name = "S_UPDATESTAFF")
	private String updateStaff;
	@Column(name = "S_REPORTTABLE")
	private String reportTable;
	@Column(name = "S_SQL")
	private String sql;
	@Column(name = "S_TOTALSQL")
	private String totalSql;
	@Column(name = "D_UPDATETIME")
	private Date updateTime;
	@Column(name = "S_CHARTSQL")
	private String chartSql;
    @Column(name = "S_SUMSQL")
    private String sumSql;

    public String getSumSql() {
        return sumSql;
    }

    public void setSumSql(String sumSql) {
        this.sumSql = sumSql;
    }

    public String getChartSql() {
		return chartSql;
	}
	public void setChartSql(String chartSql) {
		this.chartSql = chartSql;
	}
	public String getTotalSql() {
		return totalSql;
	}
	public void setTotalSql(String totalSql) {
		this.totalSql = totalSql;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getUpdateStaff() {
		return updateStaff;
	}
	
	public String getReportTable() {
		return reportTable;
	}

	public void setReportTable(String reportTable) {
		this.reportTable = reportTable;
	}

	public void setUpdateStaff(String updateStaff) {
		this.updateStaff = updateStaff;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
