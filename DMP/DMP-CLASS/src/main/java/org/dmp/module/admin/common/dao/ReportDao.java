package org.dmp.module.admin.common.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.Report;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportDao")
@Scope("prototype")
public class ReportDao extends HBaseDao<Report>{

	public Report getReport(String reportId){
		 List<Report> list = this.select(" FROM Report WHERE reportId= ?",new Field().addStr(reportId));
		 if (null != list && list.size()>0){
			return list.get(0); 
		 }
		 return null;
	}
}
