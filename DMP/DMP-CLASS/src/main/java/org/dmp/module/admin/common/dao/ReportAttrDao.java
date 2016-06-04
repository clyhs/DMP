package org.dmp.module.admin.common.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;



@Repository("ReportAttrDao")
@Scope("prototype")
public class ReportAttrDao extends HBaseDao<ReportAttr>{
	
	public List<ReportAttr> getReportAttr(String reportId){
		return this.select(" FROM ReportAttr WHERE reportId= ? ORDER BY order",new Field().addStr(reportId));
	}
}
