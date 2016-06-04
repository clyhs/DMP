package org.dmp.core.db.jdbc;

import java.util.List;

import org.dmp.core.db.util.Field;

/**
 * 数据库JDBC接口
 * 
 * @Author 伍锐凡
 * @Date 2012-3-17
 * @Version 1.0
 * @Remark
 */
public interface IDao<T> {
	List<T> select(String sSQL);

	List<T> select(String sSQL, Field oField);

	List<T> select(String sSQL, int nStart);

	List<T> select(String sSQL, int nStart, int nOffset);

	List<T> select(String sSQL, Field oField, int nStart);

	List<T> select(String sSQL, Field oField, int nStart, int nOffset);
	
	List<T> select(String sSQL, Field oField, int nStart, int nOffset ,String type);

	int insert(String sSQL);

	int insert(String sSQL, Field oField);

	int insert(String sSQL, List<Field> aField);

	int update(String sSQL);

	int update(String sSQL, Field oField);

	int update(String sSQL, List<Field> aField);

	int delete(String sSQL);

	int delete(String sSQL, Field oField);

	int delete(String sSQL, List<Field> aField);

	final int m_nOffset = 100;
}
