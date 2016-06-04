package org.dmp.core.db.util;

/**
 * 数据库字段值对象定义
 * @Author 伍锐凡
 * @Date 2012-3-14
 * @Version 1.0
 * @Remark 
 */
public class FieldVo
{
	public String m_sKey;
	public Object m_oValue;
	public int m_nType;
	
	public FieldVo(String sKey, Object oValue, int nType)
	{
		m_sKey = sKey;
		m_oValue = oValue;
		m_nType = nType;
	}
}