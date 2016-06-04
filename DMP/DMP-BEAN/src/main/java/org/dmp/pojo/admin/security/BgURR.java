package org.dmp.pojo.admin.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 后台角色与用户关联 的POJO
 */
@Entity
@Table(name = "TD_S_URR")
public class BgURR implements Serializable
{
	private static final long serialVersionUID = -5796929412829909592L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_BURRID")
	private Integer nBgURRId;
	@Column(name = "N_BGROLEID")
	private Integer nBgRoleId;
	@Column(name = "N_BGUSERID")
	private Integer nBgUserId;
	
	public Integer getBgURRId()
	{
		return nBgURRId;
	}
	
	public void setBgURRId(Integer nBgURRId)
	{
		this.nBgURRId = nBgURRId;
	}
	
	public Integer getBgRoleId()
	{
		return nBgRoleId;
	}
	
	public void setBgRoleId(Integer nBgRoleId)
	{
		this.nBgRoleId = nBgRoleId;
	}
	
	public Integer getBgUserId()
	{
		return nBgUserId;
	}
	
	public void setBgUserId(Integer nBgUserId)
	{
		this.nBgUserId = nBgUserId;
	}
}
