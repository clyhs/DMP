package org.dmp.clusters;

import org.dmp.util.BytesUtils;

public class Application implements BinaryBean {



		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		private int id=ClustersDef.INVALID_ID;
		private String name="";
		private int type=0;
		private String path="";
		public String getPath() {
			return path;
		}
		private int pathLenth=0;
		public void setPath(String path) {
			this.path = path;
			pathLenth=BytesUtils.toBytes(path).length;
		}
		private int serverId=ClustersDef.INVALID_ID;
		public int getServerId() {
			return serverId;
		}
		public void setServerId(int serverId) {
			this.serverId = serverId;
		}
		
		private String bootClass="";
		
		
		
		public String getBootClass() {
			return bootClass;
		}
		public void setBootClass(String bootClass) {
			this.bootClass = bootClass;
		}
		public static int APP_BYTES_LEN=224;
		@Override
		public byte[] getBytes() {
			byte[] buffer = new byte[APP_BYTES_LEN+4+pathLenth];
			int offset=0;
			
			byte[] tmp = BytesUtils.toBytes(id);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=4;
			
			tmp = BytesUtils.toBytes(name);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=32;
			
			tmp = BytesUtils.toBytes(type);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=4;
			
			tmp = BytesUtils.toBytes(serverId);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=4;
			
			tmp = BytesUtils.toBytes(bootClass);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=180;
			
			tmp = BytesUtils.toBytes(pathLenth);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=4;
			
			tmp = BytesUtils.toBytes(path);
			System.arraycopy(tmp, 0, buffer, offset, tmp.length);
			offset+=pathLenth;
			return buffer;
		}

		@Override
		public boolean fromBytes(byte[] bytes) {
			if (null == bytes || bytes.length < APP_BYTES_LEN) {
				return false;
			}
			
			int offset=0;
			id=BytesUtils.toInt(bytes,offset,4);
			offset+=4;
			
			name=BytesUtils.toString(bytes, offset, 32, true);
			offset+=32;
			
			type=BytesUtils.toInt(bytes,offset,4);
			offset+=4;
			
			serverId=BytesUtils.toInt(bytes,offset,4);
			offset+=4;
			
			bootClass=BytesUtils.toString(bytes, offset, 180, true);
			offset+=180;
			
			if(bytes.length>=APP_BYTES_LEN+4){
				pathLenth=BytesUtils.toInt(bytes,offset,4);
				offset+=4;
				
				path=BytesUtils.toString(bytes, offset, pathLenth, true);
				offset+=pathLenth;
			}
			
			return true;
		}
	
}
