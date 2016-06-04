package org.dmp.pojo.admin.reportmgr;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;



@Entity(name = "ReportDict")
@Table(name = "TD_S_REPORT_DICT")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@IdClass(value=ReportDictKey.class)
public class ReportDict {

	
	@Column(name = "S_NAME")
	@Id
	private String name;
	
	@Column(name = "S_ID")
	@Id
	private String code;
	@Column(name = "S_VALUE")
	private String meaning;

	
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

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

}
