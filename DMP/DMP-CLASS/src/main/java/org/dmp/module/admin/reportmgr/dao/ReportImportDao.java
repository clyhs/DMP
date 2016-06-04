package org.dmp.module.admin.reportmgr.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("ReportImportDao")
@Scope("prototype")
public class ReportImportDao extends JBaseDao{

    /**
	* 查询表属性
	* @param sSQL
	* @return
	*/
	public List getTableProperty(String sSQL){
		return m_oDB.select(sSQL);
	}
	/**
	 * 插入
	 * @param sSQL
	 * @return
	 */
	public int doInsert(String sSQL){
		return m_oDB.insert(sSQL);
	}
	/**
	 * 获取表下拉框数据
	 * @param sSQL
	 * @return
	 */
	public List<DynaBean> getTableNameBean(String sSQL){
		return m_oDB.select(sSQL);
	}
	/**
	 * 获取表头信息
	 * @param sSQL
	 * @return
	 */
	public List<DynaBean> getTableHeader(String sSQL){
		return m_oDB.select(sSQL);
	}
	
	/**
	 * 获取表主键
	 */
	public List<DynaBean> getTablePK(String sSQL){
		return m_oDB.select(sSQL);
	}
	
}
