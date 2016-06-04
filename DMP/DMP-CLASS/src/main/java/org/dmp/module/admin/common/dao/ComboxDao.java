package org.dmp.module.admin.common.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.module.common.db.JBaseDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("ComboxDao")
@Scope("prototype")
public class ComboxDao extends JBaseDao{
    /**
     * 获取动态下拉框的值
     * @param sSQL
     * @return
     */
	public List<DynaBean> getComboxBean(String sSQL){
		return m_oDB.select(sSQL);
	}	
}
