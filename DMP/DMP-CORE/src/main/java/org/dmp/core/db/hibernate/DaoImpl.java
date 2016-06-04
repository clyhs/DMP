package org.dmp.core.db.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.dmp.core.db.util.Field;
import org.dmp.core.db.util.FieldVo;
import org.dmp.core.log.Log;
import org.dmp.core.log.LogFactory;
import org.dmp.core.util.LogTags;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 数据库操作实现
 * 
 * @Author 伍锐凡
 * @Date 2012-3-14
 * @Version 1.0
 * @Remark
 */
public class DaoImpl<T> implements IDao<T> {
	private static final Log m_oLog = LogFactory.Instance(DaoImpl.class);
	protected SessionFactory sessionFactory = null;
	protected Class<T> entityClass = null;
	private int m_nBatchSize = 100;
	

	/**
	 * 通过主键获取对象
	 * 
	 * @param oPKId
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public T getById(Serializable oPKId) {
		return (T) (getSession().get(getEntityClass(), oPKId));
	}

	/**
	 * 查询数据库记录
	 * 
	 * @param sHQL
	 * @return List<T>
	 */
	public List<T> select(String sHQL) {
		return select(sHQL, null);
	}

	public List<T> selectFromSQL(String sSQL) {
		return selectFromSQL(sSQL, null);
	}

	/**
	 * 查询数据库记录
	 * 
	 * @param sHQL
	 * @param oField
	 * @return List<T>
	 */
	public List<T> select(String sHQL, Field oField) {
		return select(sHQL, oField, false);
	}

	public List<T> selectFromSQL(String sSQL, Field oField) {
		return select(sSQL, oField, true);
	}

	@SuppressWarnings("unchecked")
	protected List<T> select(String sHQL, Field oField, boolean bIsSQL) {
		Query oQuery = getQuery(sHQL, oField, bIsSQL);
		oQuery.setCacheable(true);
		return oQuery.list();
	}

	/**
	 * 查询数据库记录-带分页
	 * 
	 * @param sHQL
	 * @param nStart
	 * @return List<T>
	 */
	public List<T> select(String sHQL, int nStart) {
		return select(sHQL, null, nStart, m_nOffset);
	}

	public List<T> selectFromSQL(String sHQL, int nStart) {
		return selectFromSQL(sHQL, null, nStart, m_nOffset);
	}

	/**
	 * 查询数据库记录-带分页
	 * 
	 * @param sHQL
	 * @param nStart
	 * @param nOffset
	 * @return List<T>
	 */
	public List<T> select(String sHQL, int nStart, int nOffset) {
		return select(sHQL, null, nStart, nOffset);
	}

	public List<T> selectFromSQL(String sHQL, int nStart, int nOffset) {
		return selectFromSQL(sHQL, null, nStart, nOffset);
	}

	/**
	 * 查询数据库记录-带分页
	 * 
	 * @param sHQL
	 * @param oField
	 * @param nStart
	 * @return List<T>
	 */
	public List<T> select(String sHQL, Field oField, int nStart) {
		return select(sHQL, oField, nStart, m_nOffset);
	}

	public List<T> selectFromSQL(String sHQL, Field oField, int nStart) {
		return selectFromSQL(sHQL, oField, nStart, m_nOffset);
	}

	/**
	 * 查询数据库记录-带分页
	 * 
	 * @param sHQL
	 * @param oField
	 * @param nStart
	 * @param nOffset
	 * @return List<T>
	 */
	public List<T> select(String sHQL, Field oField, int nStart, int nOffset) {
		return select(sHQL, oField, nStart, nOffset, false);
	}

	public List<T> selectFromSQL(String sSQL, Field oField, int nStart,
			int nOffset) {
		return select(sSQL, oField, nStart, nOffset, true);
	}

	@SuppressWarnings("unchecked")
	protected List<T> select(String sHQL, Field oField, int nStart,
			int nOffset, boolean bIsSQL) {
		Query oQuery = getQuery(sHQL, oField, bIsSQL);
		oQuery.setFirstResult(nStart);
		oQuery.setMaxResults(nOffset);
		return oQuery.list();
	}

	/**
	 * 获取数据库记录总数
	 * 
	 * @param sHQL
	 * @return Long
	 */
	public long getTotal(String sHQL) {
		return getTotal(sHQL, null);
	}

	/**
	 * 获取数据库记录总数
	 * 
	 * @param sHQL
	 * @param oField
	 * @return Long
	 */
	public long getTotal(String sHQL, Field oField) {
		Query oQuery = getQuery("SELECT COUNT(*) " + sHQL, oField, false);
		return oQuery == null ? 0L : ((Long) oQuery.uniqueResult()).intValue();
	}

	/**
	 * 增加数据库记录
	 * 
	 * @param oObject
	 * @return int
	 */
	public int insert(T oObject) {
		int nTotal = -1;
		try {
			nTotal = getSession().save(oObject) == null ? -1 : 1;
		} catch (Exception e) {
			m_oLog.append(LogTags.ERROR, e.toString())
					.append("cause", e.getCause()).error();
		}
		return nTotal;
	}

	/**
	 * 批量增加数据库记录
	 * 
	 * @param aObject
	 * @return int
	 */
	public int insert(List<T> aObject) {
		int nTotal = 0;
		for (T oObj : aObject) {
			if (insert(oObj) != -1)
				nTotal++;
			submitBatch(nTotal);
		}
		return nTotal == 0 ? -1 : nTotal;
	}

	/**
	 * 增加数据库记录
	 * 
	 * @param sHQL
	 * @return int
	 */
	public int insert(String sHQL) {
		return exec(sHQL, null);
	}

	/**
	 * 增加数据库记录
	 * 
	 * @param sHQL
	 * @param oField
	 * @return int
	 */
	public int insert(String sHQL, Field oField) {
		return exec(sHQL, oField);
	}

	/**
	 * 批量增加数据库记录
	 * 
	 * @param sHQL
	 * @param aField
	 * @return int
	 */
	public int insert(String sHQL, List<Field> aField) {
		return execBatch(sHQL, aField);
	}

	/**
	 * 更新数据库记录
	 * 
	 * @param oObject
	 * @return int
	 */
	public int update(T oObject) {
		int nTotal = -1;
		try {
			getSession().update(oObject);
			nTotal = 1;
		} catch (Exception e) {
			m_oLog.append(LogTags.ERROR, e.toString())
					.append("cause", e.getCause()).error();
		}
		return nTotal;
	}

	/**
	 * 批量更新数据库记录
	 * 
	 * @param aObject
	 * @return int
	 */
	public int update(List<T> aObject) {
		int nTotal = 0;
		for (T oObj : aObject) {
			if (update(oObj) != -1)
				nTotal++;
			submitBatch(nTotal);
		}
		return nTotal == 0 ? -1 : nTotal;
	}

	/**
	 * 更新数据库记录
	 * 
	 * @param sHQL
	 * @return int
	 */
	public int update(String sHQL) {
		return exec(sHQL, null);
	}

	/**
	 * 更新数据库记录
	 * 
	 * @param sHQL
	 * @param oField
	 * @return int
	 */
	public int update(String sHQL, Field oField) {
		return exec(sHQL, oField);
	}

	/**
	 * 批量更新数据库记录
	 * 
	 * @param sHQL
	 * @param aField
	 * @return int
	 */
	public int update(String sHQL, List<Field> aField) {
		return execBatch(sHQL, aField);
	}

	/**
	 * 删除数据库记录
	 * 
	 * @param oObject
	 * @return int
	 */
	public int delete(T oObject) {
		int nTotal = -1;
		try {
			getSession().delete(oObject);
			nTotal = 1;
		} catch (Exception e) {
			m_oLog.append(LogTags.ERROR, e.toString())
					.append("cause", e.getCause()).error();
		}
		return nTotal;
	}

	/**
	 * 删除数据库记录
	 * 
	 * @param aObject
	 * @return int
	 */
	public int delete(List<T> aObject) {
		int nTotal = 0;
		for (T oObj : aObject) {
			if (delete(oObj) != -1)
				nTotal++;
			submitBatch(nTotal);
		}
		return nTotal == 0 ? -1 : nTotal;
	}

	/**
	 * 删除数据库记录
	 * 
	 * @param sHQL
	 * @return int
	 */
	public int delete(String sHQL) {
		return exec(sHQL, null);
	}

	/**
	 * 删除数据库记录
	 * 
	 * @param sHQL
	 * @param oField
	 * @return int
	 */
	public int delete(String sHQL, Field oField) {
		return exec(sHQL, oField);
	}

	/**
	 * 批量删除数据库记录
	 * 
	 * @param sHQL
	 * @param aField
	 * @return int
	 */
	public int delete(String sHQL, List<Field> aField) {
		return execBatch(sHQL, aField);
	}

	/**
	 * 执行更新、删除操作
	 * 
	 * @param sHQL
	 * @param oField
	 * @return int
	 */
	private int exec(String sHQL, Field oField) {
		Query oQuery = getQuery(sHQL, oField, false);
		return oQuery.executeUpdate();
	}

	private int execBatch(String sHQL, List<Field> aField) {
		int nTotal = 0;
		if (aField != null && aField.size() > -0) {
			for (Field oField : aField) {
				if (oField != null) {
					nTotal += exec(sHQL, oField);
					submitBatch(nTotal);
				}
			}
		}
		return nTotal;
	}

	/**
	 * 获取Hibernate的数据操作工厂
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	@Qualifier("sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取泛型参数类型
	 * 
	 * @return Class<T>
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityClass;
	}

	/**
	 * 获取数据库的操作Session
	 * 
	 * @return Session
	 */
	protected Session getSession() {
		
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 获取查询的Query
	 * 
	 * @param sHQL
	 * @param oField
	 * @param bIsSQL
	 * @return Query
	 */
	protected Query getQuery(String sHQL, Field oField, boolean bIsSQL) {
		Query oQuery = bIsSQL ? getSession().createSQLQuery(sHQL).addEntity(
				getEntityClass()) : getSession().createQuery(sHQL);
		setParameters(oQuery, oField);
		return oQuery;
	}

	/**
	 * 添加支持原生sql
	 * 
	 * @param sHQL
	 * @param oField
	 * @return
	 */
	protected Query getSqlQuery(String sHQL, Field oField) {
		Query oQuery = getSession().createSQLQuery(sHQL);
		setParameters(oQuery, oField);
		return oQuery;
	}

	/**
	 * 设置SQL参数
	 * 
	 * @param oQuery
	 * @param oField
	 */
	protected void setParameters(Query oQuery, Field oField) {
		if (oField != null && oField.getFields().size() > 0) {
			List<FieldVo> aParam = oField.getFields();
			for (int i = 0, nLen = aParam.size(); i < nLen; i++) {
				FieldVo oFieldVo = aParam.get(i);
				switch (oFieldVo.m_nType) {
				case Types.INTEGER:
					oQuery.setInteger(i,
							Integer.parseInt(oFieldVo.m_oValue.toString()));
					break;
				case Types.FLOAT:
					oQuery.setFloat(i,
							Float.parseFloat(oFieldVo.m_oValue.toString()));
					break;
				case Types.DOUBLE:
					oQuery.setDouble(i,
							Double.parseDouble(oFieldVo.m_oValue.toString()));
					break;
				case Types.TIMESTAMP:
					oQuery.setTimestamp(i,
							Timestamp.valueOf(oFieldVo.m_oValue.toString()));
					break;
				case Types.DATE:
					oQuery.setDate(i,
							java.sql.Date.valueOf(oFieldVo.m_oValue.toString()));
					break;
				case Types.TIME:
					oQuery.setTime(i,
							java.sql.Time.valueOf(oFieldVo.m_oValue.toString()));
					break;
				default:
					oQuery.setParameter(i, oFieldVo.m_oValue.toString());
					break;
				}
			}
		}
	}

	/**
	 * 批量提交
	 * 
	 * @param nTotal
	 *            return void
	 */
	protected void submitBatch(int nTotal) {
		if (nTotal > 0 && nTotal % m_nBatchSize == 0) {
			getSession().flush();
			getSession().clear();
		}
	}

}
