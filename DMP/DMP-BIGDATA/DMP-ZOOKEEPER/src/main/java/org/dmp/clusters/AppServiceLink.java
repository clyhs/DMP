package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class AppServiceLink implements BinaryBean {

	
	private int id=ClustersDef.INVALID_ID;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getLinkServerId() {
		return linkServerId;
	}
	public void setLinkServerId(int linkServerId) {
		this.linkServerId = linkServerId;
	}
	public int getLinkAppId() {
		return linkAppId;
	}
	public void setLinkAppId(int linkAppId) {
		this.linkAppId = linkAppId;
	}
	public int getLinkServiceId() {
		return serviceId;
	}
	public void setLinkServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	private int serverId=ClustersDef.INVALID_ID;
	private int appId=ClustersDef.INVALID_ID;
	private int linkServerId=ClustersDef.INVALID_ID;
	private int linkAppId=ClustersDef.INVALID_ID;
	private int serviceId=ClustersDef.INVALID_ID;
	private int wegiht=1;
	
	public int getWegiht() {
		return wegiht;
	}
	public void setWegiht(int wegiht) {
		this.wegiht = wegiht;
	}
	public static final int APP_SERVICE_LINK_BYTES_LEN=28;
	@Override
	public byte[] getBytes() {
		byte[] buffer = new byte[APP_SERVICE_LINK_BYTES_LEN];
		int offset=0;
		
		byte[] tmp = BytesUtils.toBytes(id);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		 tmp = BytesUtils.toBytes(serverId);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=4;
		
		tmp = BytesUtils.toBytes(appId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(linkServerId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(linkAppId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		
		tmp = BytesUtils.toBytes(serviceId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		tmp = BytesUtils.toBytes(wegiht);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset+=4;
		return buffer;
	}
	@Override
	public boolean fromBytes(byte[] bytes) {
		if (null == bytes || bytes.length < APP_SERVICE_LINK_BYTES_LEN) {
			return false;
		}
		
		int offset=0;
		
		id=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		serverId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		appId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		linkServerId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		linkAppId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		serviceId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		
		wegiht=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		return false;
	}
	

}
