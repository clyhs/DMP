package org.dmp.module.common.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class MetaData {

	private String totalProperty = "total";
	private String root =  "records";
	private String id = "id";
	private List<ReportFieldDto> fields = new ArrayList<ReportFieldDto>();
	public String getTotalProperty() {
		return totalProperty;
	}
	public void setTotalProperty(String totalProperty) {
		this.totalProperty = totalProperty;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<ReportFieldDto> getFields() {
		return fields;
	}
	public void setFields(List<ReportFieldDto> fields) {
		this.fields = fields;
	}
	
	
}
