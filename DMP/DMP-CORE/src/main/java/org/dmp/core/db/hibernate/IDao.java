package org.dmp.core.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.dmp.core.db.util.Field;

/**
 * 数据库HIBERNATE接口
 * 
 * @Author 伍锐凡
 * @Date 2012-3-17
 * @Version 1.0
 * @Remark
 */
public interface IDao<T> {
	T getById(Serializable oPKId);

	List<T> select(String sHQL);

	List<T> select(String sHQL, Field oField);

	List<T> select(String sHQL, int nStart);

	List<T> select(String sHQL, int nStart, int nOffset);

	List<T> select(String sHQL, Field oField, int nStart);

	List<T> select(String sHQL, Field oField, int nStart, int nOffset);

	List<T> selectFromSQL(String sSQL);

	List<T> selectFromSQL(String sSQL, Field oField);

	List<T> selectFromSQL(String sSQL, int nStart);

	List<T> selectFromSQL(String sSQL, int nStart, int nOffset);

	List<T> selectFromSQL(String sSQL, Field oField, int nStart);

	List<T> selectFromSQL(String sSQL, Field oField, int nStart, int nOffset);

	long getTotal(String sHQL);

	long getTotal(String sHQL, Field oField);

	int insert(String sHQL);

	int insert(String sHQL, Field oField);

	int insert(String sHQL, List<Field> aField);

	int insert(T oObject);

	int insert(List<T> aObject);

	int update(String sHQL);

	int update(String sHQL, Field oField);

	int update(String sHQL, List<Field> aField);

	int update(T oObject);

	int update(List<T> aObject);

	int delete(String sHQL);

	int delete(String sHQL, Field oField);

	int delete(String sHQL, List<Field> aField);

	int delete(T oObject);

	int delete(List<T> aObject);

	final int m_nOffset = 100;
}
