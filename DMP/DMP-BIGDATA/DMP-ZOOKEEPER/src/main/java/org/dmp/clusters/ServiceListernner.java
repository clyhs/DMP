package org.dmp.clusters;

public interface ServiceListernner {

	public void onServiceStop(int serverId,int appId,int serviceId);
	public void onServiceStart(int serverId,int appId,int serviceId);
	public void onAppServiceAdd(int serverId,int appId,int serviceId);
	public void onAppServiceDel(int serverId,int appId,int servicId);
	public void onAppServiceLinkAdd(int serverId,int appId,int linkServerId,int linkAppId,int serviceId);
	public void onAppServiceLinkDel(int serverId,int appId,int linkServerId,int linkAppId,int serviceId);
	public void onAppServiceLinkStart(int serverId,int appId,int linkServerId,int linkAppId,int serviceId);
	public void onAppServiceLinkStop(int serverId,int appId,int linkServerId,int linkAppId,int serviceId);
	public void onClientConnectedToService(int serverId,int appId,int serviceId,int count);
}
