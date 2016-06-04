package org.dmp.clusters;

import org.dmp.conf.BaseConstants;

public class Cluster {
	static{
		IClustersManager clustersManager=new ClustersManagerImpl();
		Server server=clustersManager.findServer(BaseConstants.APP_SERVER_NAME);
		if(null!=server){
			serverId=server.getId();
		}
		
		Application app = clustersManager.findApp(BaseConstants.APP_SERVER_NAME, BaseConstants.APP_APP_NAME);
		if(app!=null){
			appId=app.getId();
		}
	}
	public final static String serverName=BaseConstants.APP_SERVER_NAME;
	public final static String appName=BaseConstants.APP_APP_NAME;
	private static int serverId;
	public static int getServerId() {
		return serverId;
	}
	public static int getAppId() {
		return appId;
	}
	private static int appId;
}
