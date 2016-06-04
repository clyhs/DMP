package org.dmp.pojo.admin.reportmgr;

import javax.persistence.Column;
import javax.persistence.Id;

public class ReportRelationship {

	@Id
	@Column(name="N_RID")
	private int rid;
	
	@Column(name="S_REPORTID")
	private String reportId;
	
	@Column(name="N_ATTRID")
	private int attrId;

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public int getAttrId() {
		return attrId;
	}

	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}
	
	
}
