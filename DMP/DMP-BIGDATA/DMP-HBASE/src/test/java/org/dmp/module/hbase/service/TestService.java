package org.dmp.module.hbase.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.DynaBean;
import org.dmp.core.db.util.Field;
import org.dmp.module.hbase.dao.TestDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;



/**
 * 系统日志操作逻辑层
 * @Author 伍锐凡
 * @Date 2012-4-9
 * @Version 1.0
 * @Remark
 */
@Service("TestService")
@Scope("prototype")
public class TestService
{
	@Resource(name = "TestDao")
	private TestDao m_oTestDao;
	
	/**
	 * 获取系统日志列表
	 * @param sName
	 * @param sContent
	 * @param nStart
	 * @param nOffset
	 * @return List<Test>
	 */
	public List<DynaBean> getTestList(String sName, String sContent,
			int nStart, int nOffset)
	{
		return m_oTestDao.getTestList(sName, sContent, nStart, nOffset);
	}
	
	public long addTest(String sRowKey, String sName)
	{
		return m_oTestDao.addTest(sRowKey, sName);
	}
	
	public long addTest(Map<String, Field> aField)
	{
		return m_oTestDao.addTest(aField);
	}
	
}
