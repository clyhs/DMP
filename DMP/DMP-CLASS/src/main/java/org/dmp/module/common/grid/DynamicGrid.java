package org.dmp.module.common.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author huyh
 *
 * @param <T>
 */
public class DynamicGrid {

	private Long total = 0L;
	private boolean success = true;
	private Map<String, Object> aParam = new HashMap<String, Object>();
	private List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
	private List<ReportHeaderDto> columns= new ArrayList<ReportHeaderDto>();
	private MetaData metaData;
	
	
	public void setParam(String sKey, Object oValue)
	{
		aParam.put(sKey, oValue);
	}
	
	public Map<String, Object> getParam()
	{
		return aParam;
	}
	
	public MetaData getMetaData() {
		return metaData;
	}
	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<Map<String,Object>> getRecords() {
		return records;
	}
	public void setRecords(List<Map<String,Object>> records) {
		this.records = records;
	}
	public List<ReportHeaderDto> getColumns() {
		return columns;
	}
	public void setColumns(List<ReportHeaderDto> columns) {
		this.columns = columns;
	}
	
	
}
