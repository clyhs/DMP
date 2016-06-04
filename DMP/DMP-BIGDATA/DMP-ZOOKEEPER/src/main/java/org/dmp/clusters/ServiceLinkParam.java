package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class ServiceLinkParam implements BinaryBean {

	private int serverId = ClustersDef.INVALID_ID;
	private int appId = ClustersDef.INVALID_ID;
	private int serviceId = ClustersDef.INVALID_ID;

	private int poolMaxActive = 5;
	private int poolMaxIdle=5;
	private int poolMaxWait = 5*1000;
	//被空闲对象回收器回收前在池中保持空闲状态的最小时间毫秒数 
	private int poolMinEvictableIdleTimeMillis=30*1000;
	private int poolMinIdle=0;
	//在空闲连接回收器线程运行期间休眠的时间毫秒数. 如果设置为非正数,则不运行空闲连接回收器线程 
	private int poolTimeBetweenEvictionRunsMillis=60*1000;
	
	
	public static final int BYTES_LEN = 24;

	@Override
	public byte[] getBytes() {
		byte[] buffer = new byte[BYTES_LEN];
		int offset = 0;

		byte[] tmp = BytesUtils.toBytes(serverId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset += 4;
		tmp = BytesUtils.toBytes(appId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset += 4;
		tmp = BytesUtils.toBytes(serviceId);
		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
		offset += 4;
//		tmp = BytesUtils.toBytes(poolMinActive);
//		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
//		offset += 4;
//		tmp = BytesUtils.toBytes(maxActive5);
//		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
//		offset += 4;
//		tmp = BytesUtils.toBytes(liveTime);
//		System.arraycopy(tmp, 0, buffer, offset, tmp.length);
//		offset += 4;
		return buffer;
	}

	@Override
	public boolean fromBytes(byte[] bytes) {
		if (null == bytes || bytes.length < BYTES_LEN) {
			return false;
		}
		
		int offset=0;
		serverId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		appId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		serviceId=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
//		poolMinActive=BytesUtils.toInt(bytes,offset,4);
//		offset+=4;
//		maxActive5=BytesUtils.toInt(bytes,offset,4);
//		offset+=4;
//		liveTime=BytesUtils.toInt(bytes,offset,4);
//		offset+=4;
		return true;
	}

	

}
