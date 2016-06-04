package org.dmp.core.db.hbase;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.dmp.core.db.util.Field;



public interface IDao<T>
{
	public static final int DEFAULT_PAGING_OFFSET = 100;
	public static final String DEFAULT_COLUMN_FAMILY = "cf";
	public static final byte [] DEFAULT_COLUMN_FAMILY_BYTES=Bytes.toBytes(DEFAULT_COLUMN_FAMILY);
	public static final int DEFAULT_TS=1;
	
	//根据rowkey获取对象
	public T getByRowKey(String sTableName, String sColumnFamily, String sRowKey);
	public T getByRowKey(String sTableName, String sRowKey);
	
	//普通查询
	public List<T> select(String sTableName);
	public List<T> select(String sTableName, Filter oFilter);
	public List<T> select(String sTableName, String sColumnFamily, Filter oFilter);
	
	//根据rowkey范围查询
	public List<T> select(String sTableName, String sStartRow, String sStopRow);
	public List<T> select(String sTableName, String sStartRow, String sStopRow, Filter oFilter);
	public List<T> select(String sTableName, String sColumnFamily, String sStartRow, String sStopRow, Filter oFilter);
	
	//分页查询
	public List<T> select(String sTableName, String sStartRow, String sStopRow, int nStart);
	public List<T> select(String sTableName, String sStartRow, String sStopRow, int nStart, int nOffset);
	public List<T> select(String sTableName, String sStartRow, String sStopRow, Filter oFilter, int nStart);
	public List<T> select(String sTableName, String sStartRow, String sStopRow, Filter oFilter, int nStart, int nOffset);
	public List<T> select(String sTableName, String sColumnFamily, String sStartRow, String sStopRow, Filter oFilter, int nStart);
	public List<T> select(String sTableName, String sColumnFamily, String sStartRow, String sStopRow, Filter oFilter, int nStart, int nOffset);
	
	public List<T> select(String sTableName, Scan oScan);	
	public List<T> select(String sTableName, Scan oScan, int nStart, int nOffset);
	
	public int insert(String sTableName, String sRowKey, Field oField);
	public int insert(String sTableName, String sColumnFamily, String sRowKey, Field oField);
	public int insert(String sTableName, Map<String, Field> aField);
	public int insert(String sTableName, String sColumnFamily, Map<String, Field> aField);
	
	public int update(String sTableName, String sRowKey, Field oField);	
	public int update(String sTableName, String sColumnFamily, String sRowKey, Field oField);
	public int update(String sTableName, Map<String, Field> aField);
	public int update(String sTableName, String sColumnFamily, Map<String, Field> aField);
	
	public int delete(String sTableName, String sRowKey);
	public int delete(String sTableName, List<String> aRowKey);
}
