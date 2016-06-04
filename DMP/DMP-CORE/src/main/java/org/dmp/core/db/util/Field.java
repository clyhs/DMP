package org.dmp.core.db.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dmp.core.util.Tools;

/**
 * 数据库字段定义
 * 
 * @Author 伍锐凡
 * @Date 2012-3-14
 * @Version 1.0
 * @Remark
 */
public class Field {
	private List<FieldVo> m_aParam = new ArrayList<FieldVo>();

	/**
	 * 增加字符串字段
	 * 
	 * @param sValue
	 * @return Field
	 */
	public Field addStr(String sValue) {
		return addStr(null, sValue);
	}

	public Field addStr(String sKey, String sValue) {
		return addField(sKey, sValue, java.sql.Types.VARCHAR);
	}

	public Field addStrL(String sValue) {
		return addStrL(null, sValue);
	}

	public Field addStrL(String sKey, String sValue) {
		return addField(sKey, sValue, java.sql.Types.CLOB);
	}

	/**
	 * 增加整形字段
	 * 
	 * @param nValue
	 * @return Field
	 */
	public Field addInt(int nValue) {
		return addInt(null, nValue);
	}

	public Field addInt(String sKey, int nValue) {
		return addField(sKey, nValue, java.sql.Types.INTEGER);
	}

	/**
	 * 增加浮点字段
	 * 
	 * @param nValue
	 * @return
	 */
	public Field addFloat(float nValue) {
		return addFloat(null, nValue);
	}

	public Field addFloat(String sKey, float nValue) {
		return addField(sKey, nValue, java.sql.Types.FLOAT);
	}

	/**
	 * 增加双精度字段
	 * 
	 * @param nValue
	 * @return Field
	 */
	public Field addDouble(double nValue) {
		return addDouble(null, nValue);
	}

	public Field addDouble(String sKey, double nValue) {
		return addField(sKey, nValue, java.sql.Types.DOUBLE);
	}

	/**
	 * 增加yyyy-MM-dd HH:mm:ss日期字段
	 * 
	 * @param oDate
	 * @return Field
	 */
	public Field addDateTime(Date oDate) {
		return addDateTime(null, oDate);
	}

	public Field addDateTime(String sKey, Date oDate) {
		return addDateTime(sKey, Tools.getDate(oDate, "yyyy-MM-dd HH:mm:ss"));
	}

	public Field addDateTime(String sDate) {
		return addDateTime(null, sDate);
	}

	public Field addDateTime(String sKey, String sDate) {
		return addField(sKey, sDate, java.sql.Types.TIMESTAMP);
	}

	/**
	 * 增加yyyy-MM-dd日期字段
	 * 
	 * @param oDate
	 * @return Field
	 */
	public Field addDate(Date oDate) {
		return addDate(null, oDate);
	}

	public Field addDate(String sKey, Date oDate) {
		return addDate(sKey, Tools.getDate(oDate, "yyyy-MM-dd"));
	}

	public Field addDate(String sDate) {
		return addDate(null, sDate);
	}

	public Field addDate(String sKey, String sDate) {
		return addField(sKey, sDate, java.sql.Types.DATE);
	}

	/**
	 * 增加HH:mm:ss日期字段
	 * 
	 * @param oDate
	 * @return Field
	 */
	public Field addTime(Date oDate) {
		return addTime(null, oDate);
	}

	public Field addTime(String sKey, Date oDate) {
		return addTime(sKey, Tools.getDate(oDate, "HH:mm:ss"));
	}

	public Field addTime(String sDate) {
		return addTime(null, sDate);
	}

	public Field addTime(String sKey, String sDate) {
		return addField(sKey, sDate, java.sql.Types.TIME);
	}

	/**
	 * 获取所有字段
	 * 
	 * @return List<FieldVo>
	 */
	public List<FieldVo> getFields() {
		return m_aParam;
	}

	/**
	 * 增加值对象
	 * 
	 * @param sKey
	 * @param oValue
	 * @param nType
	 * @return Field
	 */
	private Field addField(String sKey, Object oValue, int nType) {
		m_aParam.add(new FieldVo(sKey, oValue, nType));
		return this;
	}

}
