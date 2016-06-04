package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportAttrMgrDao")
@Scope("prototype")
public class ReportAttrMgrDao extends HBaseDao<ReportAttr>{

	@SuppressWarnings("unchecked")
	public List<ReportAttr> getReportAttrList(String reportId){
         return super.select("from ReportAttr where reportId = ?", new Field().addStr(reportId));		
	}
	
	/**
	 * 添加reportattr列表到数据库
	 * @param reportAttrList
	 * @return
	 */
	public int addReportAttr(List<ReportAttr> reportAttrList){
		return super.insert(reportAttrList);
	}
	
	@SuppressWarnings("unchecked")
	public int updateReportAttrList(List<ReportAttr> reportAttrList){
		return super.update(reportAttrList);
	}
}
