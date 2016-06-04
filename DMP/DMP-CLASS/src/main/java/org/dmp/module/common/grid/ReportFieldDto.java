package org.dmp.module.common.grid;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportFieldDto {

	private String name;
	
	private String type;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static void main(String args[]){
		Map<String, Object> aParam = new LinkedHashMap<String, Object>();
		aParam.put("startTime", "1");
		aParam.put("endTime1", "2");
		aParam.put("endTime2", "3");
		aParam.put("endTime3", "4");
		
		Iterator it = aParam.keySet().iterator();
		String key,value;
		while(it.hasNext()){
			key =(String)it.next();  
			value = aParam.get(key).toString();
			System.out.println(""+key);
		}
	}
}
