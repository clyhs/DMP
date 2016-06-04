package org.dmp.module.admin.reportmgr.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dmp.core.db.util.Field;
import org.dmp.module.common.db.HBaseDao;
import org.dmp.pojo.admin.reportmgr.ReportQuota;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("ReportQuotaDao")
@Scope("prototype")
public class ReportQuotaDao extends HBaseDao<ReportQuota>{
	
	/**
	 * 返回指定报表的关键指标的列表,tableconstruct表示是否从表结构获取指标信息
	 * @param reportTable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportQuota> getreportQuotalist(String reportTable, Boolean tableconstruct){
		String sql = " from ReportQuota where reportTable= ?";
		List<ReportQuota> reportQuotaList = super.select(sql,new Field().addStr(reportTable));
		if(reportQuotaList.size() == 0 && tableconstruct) {
			sql = "SELECT TABLE_NAME as s_reporttable, COLUMN_NAME as s_columnname " +
					" FROM USER_TAB_COLUMNS  where table_name = ? AND DATA_TYPE = 'NUMBER'  ";
			List<HashMap<String,String>> reportQuotaObj = super.getSqlQuery(sql, new Field().addStr(reportTable))
			    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			Iterator<HashMap<String,String>> it = reportQuotaObj.iterator();
			while(it.hasNext()){
				HashMap<String,String> obj = it.next();
				ReportQuota reportQuota= new ReportQuota();
				reportQuota.setColumnName(obj.get("S_COLUMNNAME"));
				reportQuota.setReportTable(obj.get("S_REPORTTABLE"));
				reportQuotaList.add(reportQuota);
			}
		}
		return reportQuotaList ;
	}
	
	/**
	 * 添加一条指标记录
	 * @param sql
	 * @return
	 */
	/*public int insertQuota(String sql, Field field){
		return m_oDB.insert(sql);
	}*/
	
	/**
	 * 更新一条报表指标配置记录
	 * @param sql
	 * @return
	 */
	/*public int updateQuota(String sql, Field field){
		return m_oDB.update(sql);
	}*/
}