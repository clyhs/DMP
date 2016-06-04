package org.dmp.module.common.form;

import java.util.HashMap;
import java.util.Map;

import org.dmp.core.util.JsonUtil;

public class FormResponse
{
	private Boolean bSuccess = Boolean.FALSE;
	private String sMsg = "";
	private Map<String, Object> aParam = new HashMap<String, Object>();
	
	public Boolean getSuccess()
	{
		return bSuccess;
	}
	
	public void setSuccess(Boolean bSuccess)
	{
		this.bSuccess = bSuccess;
	}
	
	public String getMsg()
	{
		return sMsg;
	}
	
	public void setMsg(String sMsg)
	{
		this.sMsg = sMsg;
	}
	
	public void setParam(String sKey, Object oValue)
	{
		aParam.put(sKey, oValue);
	}
	
	public Map<String, Object> getParam()
	{
		return aParam;
	}
	
	public String toString()
	{
		return new JsonUtil<FormResponse>().encode(this);
	}
}