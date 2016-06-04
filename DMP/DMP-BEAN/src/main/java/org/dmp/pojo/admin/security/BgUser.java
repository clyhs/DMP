package org.dmp.pojo.admin.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "TD_S_USER")
public class BgUser implements Serializable
{
	private static final long serialVersionUID = 3421717982944164843L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_BGUSERID")
	private Integer nBgUserId;
	@Column(name = "S_ACCOUNT")
	private String sAccount;
	@Column(name = "S_PASSWORD")
	private String sPassword;
	@Column(name = "S_NAME")
	private String sName;
	@Column(name = "N_BGDEPTID")
	private Integer nBgDeptId;
	@Column(name = "S_DEPTNAME")
	private String sDeptName;
	@Column(name = "S_BGROLEID")
	private String sBgRoleId;
	@Column(name = "S_BGROLE")
	private String sBgRole;
	@Column(name = "S_BGPOSTID")
	private String sBgPostId;
	@Column(name = "S_BGPOST")
	private String sBgPost;
	@Column(name = "N_TYPE")
	private Integer nType = 1;
	@Column(name = "N_SEX")
	private Integer nSex = 3;
	@Column(name = "S_QQ")
	private String sQQ;
	@Column(name = "S_EMAIL")
	private String sEmail;
	@Column(name = "S_MOBILE")
	private String sMobile;
	@Column(name = "S_TEL")
	private String sTel;
	@Column(name = "S_FAX")
	private String sFax;
	@Column(name = "S_ZIP")
	private String sZip;
	@Column(name = "S_ADDRESS")
	private String sAddress;
	@Column(name = "S_BRIEF")
	private String sBrief;
	@Column(name = "N_ISVALID")
	private Integer nIsValid = 1;
	@Column(name = "D_CREATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dCreateDate = new Date();
	
	public Integer getBgUserId()
	{
		return nBgUserId;
	}
	public void setBgUserId(Integer nBgUserId)
	{
		this.nBgUserId = nBgUserId;
	}
	public String getAccount()
	{
		return sAccount;
	}
	public void setAccount(String sAccount)
	{
		this.sAccount = sAccount;
	}
	public String getPassword()
	{
		return sPassword;
	}
	public void setPassword(String sPassword)
	{
		this.sPassword = sPassword;
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
	public String getBgRoleId()
	{
		return sBgRoleId;
	}
	public void setBgRoleId(String sBgRoleId)
	{
		this.sBgRoleId = sBgRoleId;
	}
	public String getBgRole()
	{
		return sBgRole;
	}
	public void setBgRole(String sBgRole)
	{
		this.sBgRole = sBgRole;
	}
	public String getBgPostId()
	{
		return sBgPostId;
	}
	public void setBgPostId(String sBgPostId)
	{
		this.sBgPostId = sBgPostId;
	}
	public String getBgPost()
	{
		return sBgPost;
	}
	public void setBgPost(String sBgPost)
	{
		this.sBgPost = sBgPost;
	}
	public Integer getType()
	{
		return nType;
	}
	public void setType(Integer nType)
	{
		this.nType = nType;
	}
	public Integer getSex()
	{
		return nSex;
	}
	public void setSex(Integer nSex)
	{
		this.nSex = nSex;
	}
	public String getQQ()
	{
		return sQQ;
	}
	public void setQQ(String sQQ)
	{
		this.sQQ = sQQ;
	}
	public String getEmail()
	{
		return sEmail;
	}
	public void setEmail(String sEmail)
	{
		this.sEmail = sEmail;
	}
	public String getMobile()
	{
		return sMobile;
	}
	public void setMobile(String sMobile)
	{
		this.sMobile = sMobile;
	}
	public String getTel()
	{
		return sTel;
	}
	public void setTel(String sTel)
	{
		this.sTel = sTel;
	}
	public String getFax()
	{
		return sFax;
	}
	public void setFax(String sFax)
	{
		this.sFax = sFax;
	}
	public String getZip()
	{
		return sZip;
	}
	public void setZip(String sZip)
	{
		this.sZip = sZip;
	}
	public String getAddress()
	{
		return sAddress;
	}
	public void setAddress(String sAddress)
	{
		this.sAddress = sAddress;
	}
	public String getBrief()
	{
		return sBrief;
	}
	public void setBrief(String sBrief)
	{
		this.sBrief = sBrief;
	}
	public Integer getIsValid()
	{
		return nIsValid;
	}
	public void setIsValid(Integer nIsValid)
	{
		this.nIsValid = nIsValid;
	}
	public Date getCreateDate()
	{
		return dCreateDate;
	}
	public void setCreateDate(Date dCreateDate)
	{
		this.dCreateDate = dCreateDate;
	}	
}
