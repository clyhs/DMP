package org.dmp.module.hbase.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.hbase.IDao;
import org.dmp.core.db.util.Field;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;


/**
 * 系统日志操作持久层
 * @Author 伍锐凡
 * @Date 2012-4-9
 * @Version 1.0
 * @Remark
 */
@Repository("TestDao")
@Scope("prototype")
public class TestDao
{
	@Resource(name = "MUPHbase")
	protected IDao<DynaBean> m_oDB;
	/**
	 * 获取系统日志列表
	 * @param sName
	 * @param sContent
	 * @param nStart
	 * @param nOffset
	 * @return List<SysLog>
	 */
	public List<DynaBean> getTestList(String sName, String sContent,
			int nStart, int nOffset)
	{
		return m_oDB.select("test");
	}
	
	public long addTest(String sRowKey, String sName)
	{
		return m_oDB.insert("test", sRowKey, new Field().addStr("sName", sName));
	}
	
	public long addTest(Map<String, Field> aField)
	{
		return m_oDB.insert("test", aField);
	}
	
}
