package org.dmp.module.common.grid;

public class ReportHeaderDto {
	
	public String header;
    public String dataIndex;
    public boolean sortable;
    public int width;
    public String align = "center";
    
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
    
}
