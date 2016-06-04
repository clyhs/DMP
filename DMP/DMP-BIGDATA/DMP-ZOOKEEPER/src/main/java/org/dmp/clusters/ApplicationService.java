package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class ApplicationService implements BinaryBean {


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	private int id=ClustersDef.INVALID_ID;
	private int appId=ClustersDef.INVALID_ID;
	private int serverId=ClustersDef.INVALID_ID;
	private int serviceId=ClustersDef.INVALID_ID;
	private int port=0;
	private int state=0;
	private int maxClient=5;
	
	public int getMaxClient() {
		return maxClient;
	}
	public void setMaxClient(int maxClient) {
		this.maxClient = maxClient;
	}
	public static final int APP_SERVICE_BYTES_LEN=28;
	@Override
	public byte[] getBytes() {
		byte[] buffer = new byte[APP_SERVICE_BYTES_LEN];
		int offset=0;
		
		byte[] tmp = BytesUtils.toBytes(id);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(appId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
	    tmp = BytesUtils.toBytes(serverId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(serviceId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
	    tmp = BytesUtils.toBytes(port);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(state);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(maxClient);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		return buffer;
	}
	@Override
	public boolean fromBytes(byte[] bytes) {
		if (null == bytes || bytes.length < APP_SERVICE_BYTES_LEN) {
			return false;
		}
		
		int offset=0;
		id=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		appId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		serverId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		serviceId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		port=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		state=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		maxClient=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		return true;
	}
	
	

}
