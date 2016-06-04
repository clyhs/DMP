package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportCond;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("ReportMgrCondDao")
@Scope("prototype")
public class ReportMgrCondDao extends HBaseDao<ReportCond>{
	
	/**
	 * 报表配置时将reportcond保存到数据库中
	 * @param reportCondList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int addReportCond(List<ReportCond> reportCondList){
		return super.insert(reportCondList);
	} 
	
	/**
	 * 获取reportid的指定列表
	 * @param reportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportCond> getReportCondList(String reportId){
		String sql = "from ReportCond where reportId = ?";
		return super.select(sql, new Field().addStr(reportId));
	}
	
	/**
	 * 删除指定的reportid下的查询条件信息
	 * @param reportCondList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deleteReportCondList(List<ReportCond> reportCondList){
		return super.delete(reportCondList);
	}

}
