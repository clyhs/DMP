package org.dmp.module.common.db;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.jdbc.IDao;
public abstract class JBaseDao
{
	@Resource(name = "MUPJDBC")
	protected IDao<DynaBean> m_oDB;
	
	protected IDao<DynaBean> getDB()
	{
		return m_oDB;
	}
}
