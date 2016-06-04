package org.dmp.pojo.admin.navtree;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "TD_S_TREE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class BgTree implements Serializable
{
	private static final long serialVersionUID = -5009397069207638823L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SerialGenerator")
	@TableGenerator(name = "SerialGenerator", table = "TD_S_SERIAL", 
			pkColumnName = "S_SERIALNAME", pkColumnValue = "COMMON", 
			valueColumnName = "N_SERIALID", allocationSize = 1)
	@Column(name = "N_BGTREEID")
	private Integer nBgTreeId;	
	@Column(name = "N_PID")
	private Integer nPId;	
	@Column(name = "S_PATH")
	private String sPath;
	@Column(name = "S_NAME")
	private String sName;
	@Column(name = "S_URL")
	private String sUrl;
	@Column(name = "S_ICON")
	private String sIcon;
	@Column(name = "N_POS")
	private Integer nPos = 1;	
	@Column(name = "S_REMARK")
	private String sRemark;
	@Column(name = "N_ISVALID")
	private Integer nIsValid = 1;
	
	public Integer getBgTreeId()
	{
		return nBgTreeId;
	}
	
	public void setBgTreeId(Integer nBgTreeId)
	{
		this.nBgTreeId = nBgTreeId;
	}
	
	public Integer getPId()
	{
		return nPId;
	}
	
	public void setPId(Integer nPId)
	{
		this.nPId = nPId;
	}
	
	public String getPath()
	{
		return sPath;
	}
	
	public void setPath(String sPath)
	{
		this.sPath = sPath;
	}
	
	public String getName()
	{
		return sName;
	}
	
	public void setName(String sName)
	{
		this.sName = sName;
	}
	
	public String getUrl()
	{
		return sUrl;
	}
	
	public void setUrl(String sUrl)
	{
		this.sUrl = sUrl;
	}
	
	public String getIcon()
	{
		return sIcon;
	}
	
	public void setIcon(String sIcon)
	{
		this.sIcon = sIcon;
	}
	
	public Integer getPos()
	{
		return nPos;
	}
	
	public void setPos(Integer nPos)
	{
		this.nPos = nPos;
	}
	
	public String getRemark()
	{
		return sRemark;
	}
	
	public void setRemark(String sRemark)
	{
		this.sRemark = sRemark;
	}
	
	public Integer getIsValid()
	{
		return nIsValid;
	}
	
	public void setIsValid(Integer nIsValid)
	{
		this.nIsValid = nIsValid;
	}	
}
