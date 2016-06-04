package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.Report;
import org.dmp.pojo.admin.reportmgr.ReportButton;
import org.dmp.pojo.admin.reportmgr.ReportCondition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportMgrDao")
@Scope("prototype")
public class ReportMgrDao extends HBaseDao<Report>{

	@SuppressWarnings("unchecked")
	public List<Report> getReportList(String reportId, String reportName){
		
		String oraclesql = "from Report where reportId  like ? and reportName like ?"+
		             "order by nlssort(reportId,'NLS_SORT=SCHINESE_PINYIN_M')";
		String mysqlsql = "from Report where reportId  like ? and reportName like ?"+
	             " order by convert( reportId , 'gbk' ) DESC ";
		return super.select(mysqlsql, new Field().addStr("%"+reportId+"%").addStr("%"+reportName+"%"));
	}
	
	public Report getReport(String reportId){
		return (Report) super.getById(reportId);
	}
	
	/**
	 * 获取所有toolbar查询组件的列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportCondition> getConditionGroup(){
		String sql = "from ReportCondition ";
		return super.select(sql);
	}
	
	/**
	 * 获取动态配置的toolbar上的行为事件(query,export,chart)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportButton> getButtonGroup(){
		String sql = "FROM ReportButton";
		return super.select(sql);
	}
	
	/**
	 * 添加report到数据库中
	 * @param report
	 * @return
	 */
	public int addReport(Report report){
		return super.insert(report);
	}
	
	public int updateRport(Report report){
		return super.update(report);
	}
}
