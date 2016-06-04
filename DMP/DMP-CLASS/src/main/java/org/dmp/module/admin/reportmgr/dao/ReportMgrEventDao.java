package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportMgrEventDao")
@Scope("prototype")
public class ReportMgrEventDao extends HBaseDao<ReportEvent>{
	
	@SuppressWarnings("unchecked")
	public int addReportEventList(List<ReportEvent> reportEventList){
		return super.insert(reportEventList);
	} 
	
	/**
	 * 根据reportid来获取reportevent数据
	 * @param reportId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportEvent> getReportEventList(String reportId){
		String sql = "from ReportEvent where reportId=?";
		return super.select(sql, new Field().addStr(reportId));
	}
	
	/**
	 * 删除指定reportid下的button按钮信息
	 * @param reportEventList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deleteReportEventList(List<ReportEvent> reportEventList){
		return super.delete(reportEventList);
	}

}
