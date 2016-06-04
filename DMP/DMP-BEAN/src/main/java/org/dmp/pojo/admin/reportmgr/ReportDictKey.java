package org.dmp.pojo.admin.reportmgr;

import java.io.Serializable;


public class ReportDictKey  implements Serializable{

	private String name;
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ReportDictKey) {
			ReportDictKey key = (ReportDictKey) o;
			if (this.name.equals(key.name) && this.code.equals(key.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
