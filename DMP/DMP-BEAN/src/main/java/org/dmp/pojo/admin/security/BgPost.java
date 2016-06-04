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
 * 后台职位的POJO
 * @Author 
 */
@Entity
@Table(name = "TD_S_POST")
public class BgPost implements Serializable
{
	private static final long serialVersionUID = -5690402100993996413L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_BGPOSTID")
	private Integer nBgPostId;
	@Column(name = "S_NAME")
	private String sName;
	@Column(name = "N_BGDEPTID")
	private Integer nBgDeptId;
	@Column(name = "S_DEPTNAME")
	private String sDeptName;
	@Column(name = "S_REMARK")
	private String sRemark;
	
	public Integer getBgPostId()
	{
		return nBgPostId;
	}
	
	public void setBgPostId(Integer nBgPostId)
	{
		this.nBgPostId = nBgPostId;
	}
	
	public String getName()
	{
		return sName;
	}
	
	public void setName(String sName)
	{
		this.sName = sName;
	}
	
	public Integer getBgDeptId()
	{
		return nBgDeptId;
	}
	
	public void setBgDeptId(Integer nBgDeptId)
	{
		this.nBgDeptId = nBgDeptId;
	}
	
	public String getDeptName()
	{
		return sDeptName;
	}
	
	public void setDeptName(String sDeptName)
	{
		this.sDeptName = sDeptName;
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
