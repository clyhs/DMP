package org.dmp.pojo.admin.common;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name="TD_S_YMTYPE")
public class YMType implements Serializable{

	private static final long serialVersionUID = 8687688047631118742L;

	@Id
	private String ymId;   //年月id
	
	private String ymName;    //年月name


	public String getYmId() {
		return ymId;
	}

	public void setYmId(String ymId) {
		this.ymId = ymId;
	}

	public String getYmName() {
		return ymName;
	}

	public void setYmName(String ymName) {
		this.ymName = ymName;
	}
}
