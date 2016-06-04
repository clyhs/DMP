package org.dmp.module.admin.reportmgr.dao;


import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportAttr;
import org.dmp.pojo.admin.reportmgr.ReportDict;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportDictDao")
@Scope("prototype")
public class ReportDictDao extends HBaseDao<ReportDict>{

	@SuppressWarnings("unchecked")
	public List<ReportDict> getReportDictList(String reportId){
         return super.select("from ReportDict where name = ?", new Field().addStr(reportId));		
	}
	
	/**
	 * 添加dictionary列表到数据库
	 * @param reportAttrList
	 * @return
	 */
	public int addReportDictList(List<ReportDict> dictionaryList){
		return super.insert(dictionaryList);
	}
	
	@SuppressWarnings("unchecked")
	public int updateReportAttrList(List<ReportAttr> reportAttrList){
		return super.update(reportAttrList);
	}
	@SuppressWarnings("unchecked")
	public Boolean isReportDict(String name,String code){
		List<ReportDict> dictionaryList= super.select("from ReportDict where name = ? and code=?", new Field().addStr(name).addStr(code));
		if(dictionaryList!=null&&dictionaryList.size()>0){
			return Boolean.FALSE;
		}else {
			return Boolean.TRUE;
		}
		
	}
}
