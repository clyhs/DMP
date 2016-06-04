package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportButton;
import org.dmp.pojo.admin.reportmgr.ReportCondition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


@Repository("ReportConditionDao")
@Scope("prototype")
public class ReportConditionDao extends HBaseDao<ReportCondition>{

	@SuppressWarnings("unchecked")
	public List<ReportCondition> getReportConditionList(String componentId, String componentName){
		
		
		String sql = "from ReportCondition where componentId  like ? and conditionName like ?"+
		             "order by id";
		return super.select(sql, new Field().addStr("%"+componentId+"%").addStr("%"+componentName+"%"));
	}
	
	public ReportCondition getReportCondition(int reportId){
		return (ReportCondition) super.getById(reportId);
	}
	
	/**
	 * 
	 * @Description 通过控件ID查询是否存在这样ID的控件
	 * @author zhuy
	 * @version 1.0 2014年7月28日 下午2:27:21
	 */
	public ReportCondition getReportConditionByCId(String componentId){
		String hql=" from ReportCondition where componentId=?";
		List<ReportCondition> list=super.select(hql, new Field().addStr(componentId));
		ReportCondition report=null;
		if(list.size()>0){
			report=new ReportCondition();
		}
		return report;
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
	 * 添加ReportCondition到数据库中
	 * @param report
	 * @return
	 */
	public int addReportCondition(ReportCondition report){
		return super.insert(report);
	}
	
	public int updateRport(ReportCondition report){
		return super.update(report);
	}

	/**
	 * 
	 * @Description 通过控件名称查询是否存在这样的名称的控件
	 * @author zhuy
	 * @version 1.0 2014年7月28日 下午2:27:21
	 */
	public ReportCondition getReportConditionByConditionName(
			String conditionName) {
		String hql=" from ReportCondition where conditionName=?";
		List<ReportCondition> list=super.select(hql, new Field().addStr(conditionName));
		ReportCondition report=null;
		if(list.size()>0){
			report=new ReportCondition();
		}
		return report;
	}
}
