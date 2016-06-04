package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class Service {
	
	public class Type{
		public static final int SYSTEM=0;
		public static final int THRIFT=1;
	}
	
	
	
	public Service(String name){
		this.serviceName=name;
	}
	/**
	 * 必须是英文字母 最大长度为32位
	 * @param serviceName
	 */
	public Service(){	
		
	}
	

	private String serviceName;
	private int type=0;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


	private String thriftClass;
	public String getThriftClass() {
		return thriftClass;
	}
	public void setThriftClass(String thriftClass) {
		this.thriftClass = thriftClass;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public static final int SERVICE_BYTES_LEN=396;
		
	public byte [] getBytes(){
		byte[] buffer = new byte[SERVICE_BYTES_LEN];
		int offset=0;
		
		byte[] tmp = BytesUtils.toBytes(id);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(serviceName);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=32;
		
		
		tmp = BytesUtils.toBytes(serviceClass);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=180;
		
		
		tmp = BytesUtils.toBytes(thriftClass);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=180;
		
		
		return buffer;
	}
	
	public boolean fromBytes(byte [] bytes){
		if (null == bytes || bytes.length < SERVICE_BYTES_LEN) {
			return false;
		}
		
		int offset=0;
		id=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		serviceName=BytesUtils.toString(bytes, offset, 32, true);
		offset+=32;
		
		serviceClass=BytesUtils.toString(bytes, offset, 180, true);
		offset+=180;
		
		thriftClass=BytesUtils.toString(bytes, offset, 180, true);
		offset+=180;
		
		
		
		return true;
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return serviceName;
	}
	private int id=ClustersDef.INVALID_ID;

	private String serviceClass="";//ServiceWrapper impl class
	
	



	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
