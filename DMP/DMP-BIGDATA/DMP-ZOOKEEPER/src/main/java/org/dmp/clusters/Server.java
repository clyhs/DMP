package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class Server implements BinaryBean {

	private String serverName="";

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getId() {
		return id;
	}

	/**
	 * 如果是新增，请不要调用次方法
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	private String serverIp="";
	private int id=ClustersDef.INVALID_ID;
	public static int SERVER_BYTES_LEN = 68;

	@Override
	public byte[] getBytes() {
		byte[] buffer = new byte[SERVER_BYTES_LEN];
		byte[] tmp = BytesUtils.toBytes(id);
		System.arraycopy(tmp, 0, buffer, 0, tmp.length);
		tmp = BytesUtils.toBytes(serverName);
		System.arraycopy(tmp, 0, buffer, 4, tmp.length);
		tmp = BytesUtils.toBytes(serverIp);
		System.arraycopy(tmp, 0, buffer, 36, tmp.length);
		return buffer;
	}

	@Override
	public boolean fromBytes(byte[] bytes) {
		if (null == bytes || bytes.length < SERVER_BYTES_LEN) {
			return false;
		}
		
		int offset=0;
		id=BytesUtils.toInt(bytes,offset,4);
		offset+=4;
		serverName=BytesUtils.toString(bytes, offset, 32, true);
		offset+=32;
		serverIp=BytesUtils.toString(bytes, offset, 32, true);
		offset+=32;
		return true;
	}

}
