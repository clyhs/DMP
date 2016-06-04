package org.dmp.clusters;


public class ClustersDirUtils {

	
	
	
	
	public static final String getRootServerDir(){
		return ClustersDef.ROOT_SERVER_DIR;
	}
	
	public static final String getRootServiceDir(){
		return ClustersDef.ROOT_SERVICE_DIR;
	}
	public static final String getRootAppServiceLink(int serverId,int appId){
		return String.format(ClustersDef.ROOT_APP_SERVICE_LINK_DIR, serverId,appId);
	}
	
	public static final String getRootAppServiceLink(int serverId,int appId,int serviceId){
		return String.format(ClustersDef.ROOT_APP_SERVICE_LINK_DIR+"/%010d", serverId,appId,serviceId);
	}
	
	public static final String getRootAppConfigDir(int serverId,int appId){
		return String.format(ClustersDef.ROOT_APP_CONF_DIR, serverId,appId);
	}
	
	public static final String getServerDir(int serverId){
		return getRootServerDir()+"/"+String.format("%010d", serverId);
	}
	
	public static final String getServerDir(String serverId){
		return getRootServerDir()+"/"+String.format("%010d", Integer.valueOf(serverId));
	}
	
	public static String getAppDir(int serverId,int appId){
		return getServerDir(serverId)+"/"+String.format("%010d", appId);
	}
	
	public static String getAppDir(String serverId,String appId){
		return getServerDir(serverId)+"/"+String.format("%010d", Integer.valueOf(appId));
	}
	public static final String getServiceDir(int sid){
		return getRootServiceDir()+"/"+String.format("%010d", sid);
	}
	
	public static final String getServiceDir(String sid){
		return getRootServiceDir()+"/"+String.format("%010d", Integer.valueOf(sid));
	}
	
	public static final String getIdStr(String str){
		return str.substring(str.lastIndexOf("/")+1);
	}
	
	public static final int getIdFromIdStr(String str){
		return Integer.valueOf(str);
	}
	
	public static final String getAppServiceDir(int serverId,int appId){
		return String.format(ClustersDef.ROOT_APP_SERVICE_DIR, serverId,appId);
	}
	
	public static final String getAppServiceDir(int serverId,int appId,int sid){
		return String.format(ClustersDef.APP_SERVICE_DIR, serverId,appId,sid);
	}
	
	public static final String getAppServiceLinkDir(int serverId,int appId,int serviceId,int linkId){
		return String.format(ClustersDef.APP_SERVICE_LINK_DIR, serverId,appId,serviceId,linkId);
	}
	
	public static final String getAppConfigDir(int serverId,int appId,String key){
		return String.format(ClustersDef.APP_CONF_DIR, serverId,appId,key);
	}
	public static final String getAppServiceStateDir(int serverId,int appId,int serviceId){
		return String.format(ClustersDef.APP_SERVICE_STATE_DIR, serverId,appId,serviceId);
	}
	
	public static final String getRootAppServiceConnectionDir(int serverId,int appId,int serviceId){
		return String.format(ClustersDef.ROOT_APP_SERVICE_CONNECTION_DIR, serverId,appId,serviceId);
	}
	
	public static final String getAppServiceConnectionDir(int serverId,int appId,int serviceId,int clientServerId,int clientAppId,int connectionId){
		return String.format(ClustersDef.APP_SERVICE_CONNECTION_DIR, serverId,appId,serviceId,clientServerId,clientAppId,connectionId);
	}
	
}
