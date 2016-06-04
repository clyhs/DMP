package org.dmp.core.db.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.dmp.core.db.hbase.ref.HbaseTemplate;
import org.dmp.core.db.hbase.ref.RowMapper;
import org.dmp.core.db.hbase.ref.TableCallback;
import org.dmp.core.db.util.Field;
import org.dmp.core.db.util.FieldVo;
import org.dmp.core.util.JsonBuilder;
import org.dmp.core.util.LogTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;




public class DaoImpl implements IDao<DynaBean>, RowMapper<DynaBean>
{
	protected HbaseTemplate hbaseTemplate = null;
	private static HTablePool m_oTablePool = null;
	private boolean m_bIsAutoFlush = false;
	private int m_nWriteBufferSize = 5 * 1024 * 1024;

	/**
	 * 通过rowkey获取对象
	 * 
	 * @param sTableName
	 * @param sColumnFamily
	 * @param sRowKey
	 * @return T
	 */
	public DynaBean getByRowKey(String sTableName, String sColumnFamily,
			String sRowKey)
	{
		return hbaseTemplate.get(sTableName, sRowKey, sColumnFamily, this);
	}

	public DynaBean getByRowKey(String sTableName, String sRowKey)
	{
		return getByRowKey(sTableName, DEFAULT_COLUMN_FAMILY, sRowKey);
	}

	/**
	 * 查询数据库记录
	 * 
	 * @param sTableName
	 * @param sColumnFamily
	 * @param oFilter
	 * @return List<T>
	 */
	public List<DynaBean> select(String sTableName, String sColumnFamily,
			Filter oFilter)
	{
		Scan oScan = new Scan();
		oScan.addFamily(Bytes.toBytes(sColumnFamily));
		if (oFilter != null)
		{
			oScan.setFilter(oFilter);
		}
		return select(sTableName, oScan);
	}

	public List<DynaBean> select(String sTableName)
	{
		return select(sTableName, (Filter) null);
	}

	public List<DynaBean> select(String sTableName, Filter oFilter)
	{
		return select(sTableName, DEFAULT_COLUMN_FAMILY, oFilter);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow)
	{
		return select(sTableName, sStartRow, sStopRow, null);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow, Filter oFilter)
	{
		return select(sTableName, DEFAULT_COLUMN_FAMILY, sStartRow, sStopRow,
				oFilter);
	}

	public List<DynaBean> select(String sTableName, String sColumnFamily,
			String sStartRow, String sStopRow, Filter oFilter)
	{
		Scan oScan = new Scan();
		oScan.addFamily(Bytes.toBytes(sColumnFamily));
		oScan.setStartRow(Bytes.toBytes(sStartRow));
		oScan.setStopRow(Bytes.toBytes(sStopRow));
		oScan.setFilter(oFilter);
		return select(sTableName, oScan);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow, int nStart)
	{
		return select(sTableName, sStartRow, sStopRow, nStart,
				DEFAULT_PAGING_OFFSET);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow, int nStart, int nOffset)
	{
		return select(sTableName, sStartRow, sStopRow, null, nStart, nOffset);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow, Filter oFilter, int nStart)
	{
		return select(sTableName, DEFAULT_COLUMN_FAMILY, sStartRow, sStopRow,
				oFilter, nStart, DEFAULT_PAGING_OFFSET);
	}

	public List<DynaBean> select(String sTableName, String sStartRow,
			String sStopRow, Filter oFilter, int nStart, int nOffset)
	{
		return select(sTableName, DEFAULT_COLUMN_FAMILY, sStartRow, sStopRow,
				oFilter, nStart, nOffset);
	}

	public List<DynaBean> select(String sTableName, String sColumnFamily,
			String sStartRow, String sStopRow, Filter oFilter, int nStart)
	{
		return select(sTableName, sColumnFamily, sStartRow, sStopRow, oFilter,
				nStart, DEFAULT_PAGING_OFFSET);
	}

	public List<DynaBean> select(String sTableName, String sColumnFamily,
			String sStartRow, String sStopRow, Filter oFilter, int nStart,
			int nOffset)
	{
		Scan oScan = new Scan();
		oScan.addFamily(Bytes.toBytes(sColumnFamily));
		oScan.setStartRow(Bytes.toBytes(sStartRow));
		oScan.setStopRow(Bytes.toBytes(sStopRow));
		oScan.setFilter(oFilter);
		return select(sTableName, oScan, nStart, nOffset);
	}

	public List<DynaBean> select(String sTableName, Scan oScan)
	{
		return hbaseTemplate.find(sTableName, oScan, this);
	}

	public List<DynaBean> select(String sTableName, Scan oScan, int nStart,
			int nOffset)
	{
		return hbaseTemplate.find(sTableName, oScan, this, nStart, nOffset);
	}

	/**
	 * 增加数据库记录
	 * 
	 * @param sTableName
	 * @param sRowKey
	 * @param sColumnFamily
	 * @param oField
	 * @return long
	 */
	public int insert(String sTableName, String sColumnFamily, String sRowKey,
			Field oField)
	{
		Map<String, Field> aField = new HashMap<String, Field>();
		aField.put(sRowKey, oField);
		return insert(sTableName, sColumnFamily, aField);
	}

	public int insert(String sTableName, String sRowKey, Field oField)
	{
		return insert(sTableName, DEFAULT_COLUMN_FAMILY, sRowKey, oField);
	}

	public int insert(String sTableName, Map<String, Field> aField)
	{
		return insert(sTableName, DEFAULT_COLUMN_FAMILY, aField);
	}

	public int insert(String sTableName, String sColumnFamily,
			Map<String, Field> aField)
	{
		return new Hbase().execute(hbaseTemplate, sTableName, sColumnFamily,
				aField);
	}

	/**
	 * 更新数据库记录
	 * 
	 * @param sTableName
	 * @param sRowKey
	 * @param sColumnFamily
	 * @param oField
	 * @return long
	 */
	public int update(String sTableName, String sColumnFamily, String sRowKey,
			Field oField)
	{
		return insert(sTableName, sRowKey, sColumnFamily, oField);
	}

	public int update(String sTableName, String sRowKey, Field oField)
	{
		return insert(sTableName, sRowKey, DEFAULT_COLUMN_FAMILY, oField);
	}

	public int update(String sTableName, Map<String, Field> aField)
	{
		return insert(sTableName, DEFAULT_COLUMN_FAMILY, aField);
	}

	public int update(String sTableName, String sColumnFamily,
			Map<String, Field> aField)
	{
		return insert(sTableName, sColumnFamily, aField);
	}

	/**
	 * 删除数据库记录
	 * 
	 * @param sTableName
	 * @param sRowKey
	 * @return long
	 */
	public int delete(String sTableName, String sRowKey)
	{
		List<String> aRowKey = new ArrayList<String>();
		aRowKey.add(sRowKey);
		return delete(sTableName, aRowKey);
	}

	public int delete(String sTableName, List<String> aRowKey)
	{
		return new Hbase().execute(hbaseTemplate, sTableName, aRowKey);
	}

	/**
	 * 获取HbaseTemplate
	 * 
	 * @param hbaseTemplate
	 */
	@Autowired
	@Qualifier("hbaseTemplate")
	public void setHbaseTemplate(HbaseTemplate hbaseTemplate)
	{
		this.hbaseTemplate = hbaseTemplate;
		this.hbaseTemplate.setTableFactory(new HTableInterfaceFactory()
		{
			public HTableInterface createHTableInterface(Configuration config,
					byte[] tableName)
			{
				if (m_oTablePool == null)
				{
					m_oTablePool = new HTablePool(config, Integer.MAX_VALUE);
				}

				HTableInterface oTable = m_oTablePool.getTable(tableName);
				// 禁止AUTO FLUSH
				oTable.setAutoFlush(m_bIsAutoFlush);
				try
				{
					// 设置写入的缓冲区大小
					oTable.setWriteBufferSize(m_nWriteBufferSize);
				} catch (IOException e)
				{
				}
				return oTable;
			}

			public void releaseHTableInterface(HTableInterface table)
					throws IOException
			{
				m_oTablePool.closeTablePool(table.getTableName());
			}
		});
	}

	public DynaBean mapRow(Result oResult, int nRowNum) throws Exception
	{
		DynaBean oDynaBean = new LazyDynaBean();
		for (KeyValue oKeyValue : oResult.raw())
		{
			if(!new String(oKeyValue.getFamily()).equals(DEFAULT_COLUMN_FAMILY)) continue;
			oDynaBean.set(new String(oKeyValue.getQualifier()), new String(oKeyValue.getValue()));
		}
		return oDynaBean;
	}

}

/**
 * HBASE简单封装
 * 
 * @Author 伍锐凡
 * @Date 2012-3-14
 * @Version 1.0
 * @Remark
 */
class Hbase
{
	private static final Logger m_oLog = LoggerFactory.getLogger(Hbase.class);
	private int m_nTotal = 0;
	// 禁止WAL日志
	private boolean m_bIsWriteToWAL = false;

	public int execute(HbaseTemplate hbaseTemplate, String sTableName,
			final List<String> aRowKey)
	{
		m_nTotal = 0;
		hbaseTemplate.execute(sTableName, new TableCallback<Object>()
		{
			public Object doInTable(HTableInterface oTable) throws Throwable
			{
				List<Delete> aDelete = new ArrayList<Delete>();
				if (aRowKey != null && aRowKey.size() > 0)
				{
					for (String sRowKey : aRowKey)
					{
						Delete oDelete = new Delete(sRowKey.getBytes());
						oDelete.setWriteToWAL(m_bIsWriteToWAL);
						Hbase.this.m_nTotal++;
					}
					try
					{
						oTable.delete(aDelete);
					} catch (Exception e)
					{
						Hbase.this.m_nTotal = -1;
						m_oLog.error(new JsonBuilder().append(LogTags.ERROR,e.toString()).append("cause",e.getCause()).toString(),e);
					}
				}
				return null;
			}
		});
		return m_nTotal;
	}

	public int execute(HbaseTemplate hbaseTemplate, String sTableName,
			final String sColumnFamily, final Map<String, Field> aField)
	{
		m_nTotal = 0;

		hbaseTemplate.execute(sTableName, new TableCallback<Object>()
		{
			public Object doInTable(HTableInterface oTable) throws Throwable
			{
				if (aField != null && aField.size() > 0)
				{
					List<Put> aPut = new ArrayList<Put>();
					for (String sRowKey : aField.keySet())
					{
						Field oField = aField.get(sRowKey);
						if (oField != null && oField.getFields() != null
								&& oField.getFields().size() > 0)
						{
							Put oPut = new Put(Bytes.toBytes(sRowKey));
							oPut.setWriteToWAL(m_bIsWriteToWAL);
							List<FieldVo> aField = oField.getFields();
							for (FieldVo oFieldVo : aField)
							{
								oPut.add(Bytes.toBytes(sColumnFamily), Bytes
										.toBytes(oFieldVo.m_sKey), Bytes
										.toBytes(oFieldVo.m_oValue.toString()));
							}
							aPut.add(oPut);
						}
					}
					try
					{
						oTable.put(aPut);
						Hbase.this.m_nTotal++;
					} catch (Exception e)
					{
						Hbase.this.m_nTotal = -1;
						m_oLog.error(new JsonBuilder().append(LogTags.ERROR,e.toString()).append("cause",e.getCause()).toString(),e);
					}
				}
				return null;
			}
		});

		return m_nTotal;
	}

}
