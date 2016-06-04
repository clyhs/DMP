package org.dmp.module.admin.common.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.common.PortalType;
import org.dmp.pojo.admin.reportmgr.ReportCond;
import org.dmp.pojo.admin.reportmgr.ReportEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;



@Repository("DynamicReportDao")
@Scope("prototype")
public class DynamicReportDao extends HBaseDao<PortalType>{

	public List<ReportCond> getToolbarData(String reportId){
		return super.select("from ReportCond where reportId = ? order by layout desc", new Field().addStr(reportId));
	}
	
	public List<ReportEvent> getEventData(String reportId){
		return super.select("from ReportEvent where reportId = ? order by layout desc", new Field().addStr(reportId));
	}
	
}
