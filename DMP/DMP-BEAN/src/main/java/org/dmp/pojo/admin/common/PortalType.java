package org.dmp.pojo.admin.common;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "TD_S_PORTALTYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class PortalType implements Serializable{
	
	private static final long serialVersionUID = 1466251001270964270L;

	@Id
	@Column(name = "S_PORTALID")
	private Integer portalId;

	@Column(name = "S_PORTALNAME")
	private String portalName;
	
	@Column(name = "S_PORTALGROUP")
	private String portalGroup;

    public String getPortalGroup() {
		return portalGroup;
	}

	public void setPortalGroup(String portalGroup) {
		this.portalGroup = portalGroup;
	}

	public Integer getPortalId() {
        return portalId;
    }

    public void setPortalId(Integer portalId) {
        this.portalId = portalId;
    }

    public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}
}
