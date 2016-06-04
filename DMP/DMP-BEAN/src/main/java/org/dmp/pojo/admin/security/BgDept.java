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
 * 后台部门的POJO
 */
@Entity
@Table(name = "TD_S_DEPT")
public class BgDept implements Serializable
{
	private static final long serialVersionUID = 8586026786869153731L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_BGDEPTID")
	private Integer nBgDeptId;
	@Column(name = "N_PID")
	private Integer nPId;	
	@Column(name = "S_NAME")
	private String sName;
	@Column(name = "S_PATH")
	private String sPath;
	@Column(name = "S_REMARK")
	private String sRemark;
	
	public Integer getBgDeptId()
	{
		return nBgDeptId;
	}
	
	public void setBgDeptId(Integer nBgDeptId)
	{
		this.nBgDeptId = nBgDeptId;
	}
	
	public Integer getPId()
	{
		return nPId;
	}
	
	public void setPId(Integer nPId)
	{
		this.nPId = nPId;
	}
	
	public String getName()
	{
		return sName;
	}
	
	public void setName(String sName)
	{
		this.sName = sName;
	}
	
	public String getPath()
	{
		return sPath;
	}
	
	public void setPath(String sPath)
	{
		this.sPath = sPath;
	}
	
	public String getRemark()
	{
		return sRemark;
	}
	
	public void setRemark(String sRemark)
	{
		this.sRemark = sRemark;
	}
	
}
