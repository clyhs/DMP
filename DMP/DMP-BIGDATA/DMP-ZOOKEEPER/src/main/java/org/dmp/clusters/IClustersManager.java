package org.dmp.clusters;

import java.util.List;
import java.util.Map;

public interface IClustersManager {
	//服务器管理
	
	public int addOrUpdateServer(Server server);
	public int removeServer(int id);
	public List<Server> getServers();
	public Server getServer(int id);
	public Server findServer(String serverName);
	
	public int addOrUpdateApp(Application app);
	public int removeApp(int serverId,int appId);
	public List<Application> getApps(int serverId);
	public Application getApp(int serverId,int appId);
	public List<Application> getApps();
	public Application findApp(String serverName,String appName);
	
	
	public int addOrUpdateService(Service service);
	public int removeService(int id);
	public List<Service> getServices();
	public Service getService(int id);
	public Service findService(String serviceName);
	
	public int addAppService(ApplicationService appService);
	public int removeAppService(int serverId,int appId,int appsId);
	public List<ApplicationService> getAppServices(int serverId,int appId);
	public ApplicationService getAppService(int serverId,int appId,int serviceId);
	public ApplicationService findAppService(String serverName,String appName,String serviceName);
	
	public int addAppServiceLink(AppServiceLink asl);
	public int removeAppServiceLink(int serverId,int appId,int serviceId,int id);
	public List<AppServiceLink> getAppServiceLinks(int serverId,int appId);
	public AppServiceLink getAppServiceLink(int serverId,int appId,int linkServerId,int linkAppId,int linkServiceId);
	public List<AppServiceLink> getAppServiceLinks();
	
	public int addOrUpdateConfig(int serverId,int appId,String key,byte[] value);
	public int removeConfig(int serverId,int appId,String key);
	public Map<String,byte[]> getConfigs(int serverId,int appId);
	public byte [] getConfig(int serverId,int appId,String key);
	
	public boolean isServiceStarted(int serverId,int appId,int serviceId);
	public int startService(int serverId,int appId,int serviceId);
	public int stopService(int serverId,int appId,int serviceId);
	
	
	public boolean canConnectToServer(int serverId,int appId,int serviceId);
	public int addServiceConnection(int serverId,int appId,int serviceId,int clientServerId,int clientAppId,int connectionId);
	public int removeServiceConnectionCount(int serverId,int appId,int serviceId,int clientServerId,int clientAppId,int connectionId);
	public int getServiceConnectionCount(int serverId,int appId,int serviceId);
}
