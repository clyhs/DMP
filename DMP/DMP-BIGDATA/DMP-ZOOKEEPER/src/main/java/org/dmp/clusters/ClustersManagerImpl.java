package org.dmp.clusters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.KeeperException;
import org.dmp.core.log.Log;
import org.dmp.core.log.LogFactory;
import org.dmp.util.StrUtil;
import org.dmp.zookeeper.ZKClient;
import org.dmp.zookeeper.exception.ZkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ClustersManagerImpl implements IClustersManager {

	private final Log LOG = LogFactory.Instance(ClustersManagerImpl.class);
	private final Logger log = LoggerFactory.getLogger(ClustersManagerImpl.class);
	private final ReentrantLock lock = new ReentrantLock();
	ZKClient zkClient = ZKClient.getInstance();

	private String createSeqDir(String dir) {
		// if (!zkClient.exists(dir)) {
		try {
			return zkClient.createPersistentSequential(dir);
		} catch (IllegalArgumentException e) {
			log.error(String.format("create sedir %s error ", dir), e);
		} catch (ZkException e) {
			log.error(String.format("create sedir %s error ", dir), e);
		} catch (RuntimeException e) {
			log.error(String.format("create sedir %s error ", dir), e);
		} catch (KeeperException e) {
			log.error(String.format("create sedir %s error ", dir), e);
		} catch (InterruptedException e) {
			log.error(String.format("create sedir %s error ", dir), e);
		}
		// }
		return null;
	}

	private void createTmpDir(String dir) {
		// if (!zkClient.exists(dir)) {
		try {
			zkClient.createEphemeral(dir);
		} catch (IllegalArgumentException e) {
			log.error(String.format("create tmpdir %s error ", dir), e);
		} catch (ZkException e) {
			log.error(String.format("create tmpdir %s error ", dir), e);
		} catch (RuntimeException e) {
			log.error(String.format("create tmpdir %s error ", dir), e);
		} catch (KeeperException e) {
			log.error(String.format("create tmpdir %s error ", dir), e);
		} catch (InterruptedException e) {
			log.error(String.format("create tmpdir %s error ", dir), e);
		}
		// }
	}

	private void createDir(String dir) {
		// if (!zkClient.exists(dir)) {
		try {
			zkClient.createPersistent(dir);
		} catch (IllegalArgumentException e) {
			log.error(String.format("create dir %s error ", dir), e);
		} catch (ZkException e) {
			log.error(String.format("create dir %s error ", dir), e);
		} catch (RuntimeException e) {
			log.error(String.format("create dir %s error ", dir), e);
		} catch (KeeperException e) {
			log.error(String.format("create dir %s error ", dir), e);
		} catch (InterruptedException e) {
			log.error(String.format("create dir %s error ", dir), e);
		}
		// }
	}

	@Override
	public synchronized int addOrUpdateServer(Server server) {

		if (server.getId() == ClustersDef.INVALID_ID) {
			List<Server> list = getServers();
			for (Server s : list) {
				if (s.getServerName().trim().equals(server.getServerName().trim())) {
					log.info(String.format("add server(%s) failed server name exist", server.getServerName()));
					return ClustersDef.ERR_SERVER_EXIST;
				}
			}

			String serverIdStr = createSeqDir(ClustersDirUtils.getRootServerDir() + "/");
			if (null == serverIdStr) {
				log.info(String.format("add server(%s) failed", server.getServerName()));
				return ClustersDef.ERR_ADD_SERVER_FAILD;
			}

			int serverId = StrUtil.toInt(ClustersDirUtils.getIdStr(serverIdStr), ClustersDef.INVALID_ID);
			if (serverId == ClustersDef.INVALID_ID) {
				log.info(String.format("add server(%s) failed parse serverIdStr(%s) to int failed", server.getServerName(), serverIdStr));
				return ClustersDef.ERR_ADD_SERVER_FAILD;
			}
			server.setId(serverId);

		}

		String serverDir = ClustersDirUtils.getServerDir(server.getId());

		if (!zkClient.exists(serverDir)) {
			log.info(String.format("add server(%s) failed parse serverIdStr(%s) to int failed", server.getServerName(), serverDir));
			return ClustersDef.ERR_SERVER_NOT_EXIST;
		}

		zkClient.writeData(serverDir, server.getBytes(), -1);
		log.info(String.format("add server(%d,%s,%s) success ", server.getId(), server.getServerName(), server.getServerIp()));
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeServer(int id) {
		String serverDir = ClustersDirUtils.getServerDir(id);
		if (!zkClient.exists(serverDir)) {
			log.info(String.format("delete server(%d) failed the server not exist", id));
			return ClustersDef.ERR_SERVER_NOT_EXIST;
		}
		zkClient.deleteRecursive(serverDir);
		log.info(String.format("delete server(%d) success", id));
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized List<Server> getServers() {
		String serverDir = ClustersDirUtils.getRootServerDir();
		if (!zkClient.exists(serverDir)) {
			return null;
		}
		List<String> children = zkClient.getChildren(serverDir);
		List<Server> serverList = new ArrayList<Server>();
		for (String child : children) {
			byte[] buffer = zkClient.readData(ClustersDirUtils.getServerDir(child), true);
			if (null != buffer) {
				Server server = new Server();
				server.fromBytes(buffer);
				serverList.add(server);
			}
		}

		return serverList;
	}

	@Override
	public synchronized Server getServer(int id) {
		byte[] buffer = zkClient.readData(ClustersDirUtils.getServerDir(id), true);
		if (buffer == null) {
			return null;
		}
		Server server = new Server();
		server.fromBytes(buffer);
		return server;
	}

	@Override
	public synchronized int addOrUpdateApp(Application app) {
		if (app.getServerId() == ClustersDef.INVALID_ID) {
			return ClustersDef.ERR_INVALID_ID;
		}
		String serverDir = ClustersDirUtils.getServerDir(app.getServerId());

		if (!zkClient.exists(serverDir)) {
			log.info(String.format("add app failed the server(%d) not exist", app.getServerId()));
			return ClustersDef.ERR_SERVER_NOT_EXIST;
		}

		if (app.getId() == ClustersDef.INVALID_ID) {
			List<Application> appList = getApps(app.getServerId());
			for (Application a : appList) {
				if (app.getName().trim().equals(a.getName().trim())) {
					log.info(String.format("add app failed the app name(%s) exist", app.getName()));
					return ClustersDef.ERR_APP_EXIST;
				}
			}
			String appIdStr = createSeqDir(serverDir + "/");
			int appId = StrUtil.toInt(ClustersDirUtils.getIdStr(appIdStr), ClustersDef.INVALID_ID);
			if (appId == ClustersDef.INVALID_ID) {
				log.info(String.format("add app(%d,%d,%s) failed parse appIdStr(%s) to int failed", -1, app.getServerId(), app.getName(), appIdStr));
				return ClustersDef.ERR_ADD_APP_FAILD;
			}
			app.setId(appId);

			String appsDir = ClustersDirUtils.getAppServiceDir(app.getServerId(), appId);
			createDir(appsDir);
			createDir(ClustersDirUtils.getRootAppServiceLink(app.getServerId(), appId));
			createDir(ClustersDirUtils.getRootAppConfigDir(app.getServerId(), appId));
		}

		String appDir = ClustersDirUtils.getAppDir(app.getServerId(), app.getId());
		if (!zkClient.exists(appDir)) {
			log.info(String.format("set app(%d,%d,%s) property failed the app not exist", app.getId(), app.getServerId(), app.getName()));
			return ClustersDef.ERR_APP_NOT_EXIST;
		}

		zkClient.writeData(appDir, app.getBytes(), -1);
		log.info(String.format("add app(%d,%d,%s) and set app property success", app.getId(), app.getServerId(), app.getName()));
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeApp(int serverId, int appId) {
		LOG.append("serverId", appId).append("appId", appId);
		String appDir = ClustersDirUtils.getAppDir(serverId, appId);
		if (!zkClient.exists(appDir)) {
			LOG.appendMessage("delete app failed the app not exist").info();
			return ClustersDef.ERR_APP_NOT_EXIST;
		}
		zkClient.deleteRecursive(appDir);
		LOG.appendMessage(" app remove success").info();
		return 0;
	}

	@Override
	public synchronized List<Application> getApps(int serverId) {

		String serverDir = ClustersDirUtils.getServerDir(serverId);
		if (!zkClient.exists(serverDir)) {
			return null;
		}
		List<String> children = zkClient.getChildren(serverDir);
		List<Application> appList = new ArrayList<Application>();
		for (String child : children) {
			byte[] buffer = zkClient.readData(serverDir + "/" + child, true);
			if (buffer != null) {
				Application app = new Application();
				app.fromBytes(buffer);
				appList.add(app);
			}
		}
		return appList;
	}

	@Override
	public synchronized Application getApp(int serverId, int appId) {
		byte[] buffer = zkClient.readData(ClustersDirUtils.getAppDir(serverId, appId), true);
		if (buffer != null) {
			Application app = new Application();
			app.fromBytes(buffer);
			return app;
		}
		return null;
	}

	@Override
	public synchronized List<Application> getApps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized int addOrUpdateService(Service service) {
		if (null == service) {
			return ClustersDef.ERR_INVALID_PARAM;
		}
		LOG.append("serviceName", service.getServiceName());

		if (service.getId() == ClustersDef.INVALID_ID) {
			List<Service> serviceList = getServices();
			for (Service s : serviceList) {
				if (s.getServiceName().trim().equals(service.getServiceName().trim())) {
					LOG.appendMessage("add service failed the serviceName exist").info();
					return ClustersDef.ERR_SERVICE_EXIST;
				}
			}

			String serviceIdStr = createSeqDir(ClustersDirUtils.getRootServiceDir() + "/");
			if (null == serviceIdStr) {
				LOG.appendMessage("add service failed the serviceName exist").info();
				return ClustersDef.ERR_ADD_SERVICE_FAILD;
			}
			int sid = StrUtil.toInt(ClustersDirUtils.getIdStr(serviceIdStr), ClustersDef.INVALID_ID);
			if (sid == ClustersDef.INVALID_ID) {
				LOG.appendMessage("add service failed the serviceIdStr invalid").append("serviceIdStr", serviceIdStr).info();
				return ClustersDef.ERR_ADD_SERVICE_FAILD;
			}
			service.setId(sid);
		}
		LOG.append("serviceId", service.getId());
		String serviceDir = ClustersDirUtils.getServiceDir(service.getId());
		if (!zkClient.exists(serviceDir)) {
			LOG.appendMessage("set service param faild,the service not exist").info();
			return ClustersDef.ERR_SERVICE_NOT_EXIST;
		}
		zkClient.writeData(serviceDir, service.getBytes(), -1);
		LOG.appendMessage("add service and set service param success!").info();
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeService(int id) {
		LOG.append("serviceId", id);
		String serviceDir = ClustersDirUtils.getServiceDir(id);
		if (!zkClient.exists(serviceDir)) {
			LOG.appendMessage("remove service failed ,the service nost exist").info();
			return ClustersDef.ERR_SERVICE_NOT_EXIST;
		}
		zkClient.deleteRecursive(serviceDir);
		LOG.appendMessage("remove service success!");
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized List<Service> getServices() {
		List<String> children = zkClient.getChildren(ClustersDirUtils.getRootServiceDir());
		List<Service> serviceList = new ArrayList<Service>();
		for (String child : children) {
			byte[] buffer = zkClient.readData(ClustersDirUtils.getServiceDir(child), true);
			if (null != buffer) {
				Service s = new Service();
				if (s.fromBytes(buffer)) {
					serviceList.add(s);
				}

			}
		}
		return serviceList;
	}

	@Override
	public synchronized Service getService(int id) {
		byte[] buffer = zkClient.readData(ClustersDirUtils.getServiceDir(id), true);
		if (null != buffer) {
			Service s = new Service();
			if (s.fromBytes(buffer)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public synchronized int addAppServiceLink(AppServiceLink asl) {
		LOG.append("serverId", asl.getServerId()).append("appId", asl.getAppId()).append("linkServerId", asl.getLinkServerId()).append("linkAppId", asl.getLinkAppId()).append("linkServiceId", asl.getLinkServiceId());
		String appDir = ClustersDirUtils.getAppDir(asl.getServerId(), asl.getAppId());
		if (!zkClient.exists(appDir)) {
			LOG.appendMessage("add app service link failed ,the app not exist!").info();
			return ClustersDef.ERR_APP_NOT_EXIST;
		}
		String appsDir = ClustersDirUtils.getAppServiceDir(asl.getLinkServerId(), asl.getLinkAppId(), asl.getLinkServiceId());
		if (!zkClient.exists(appsDir)) {
			LOG.appendMessage("add app service link failed ,the app service not exist").info();
			return ClustersDef.ERR_APP_SERVICE_NOT_EXIST;
		}

		List<AppServiceLink> list = getAppServiceLinks(asl.getServerId(), asl.getAppId());
		for (AppServiceLink link : list) {
			if (asl.getLinkServerId() == link.getLinkServerId() && asl.getLinkAppId() == link.getLinkAppId() && asl.getLinkServiceId() == link.getLinkServiceId()) {
				LOG.appendMessage("add app service link failed ,the link exist").info();
				return ClustersDef.ERR_APP_SERVICE_LINK_EXIST;
			}
		}

		String linkIdStr = createSeqDir(ClustersDirUtils.getRootAppServiceLink(asl.getServerId(), asl.getAppId(), asl.getLinkServiceId()) + "/");
		if (null == linkIdStr) {
			LOG.appendMessage("add app service link failed ,create seq dir failed").info();
			return ClustersDef.ERR_APP_SERVICE_LINK_EXIST;
		}

		int linkId = StrUtil.toInt(ClustersDirUtils.getIdStr(linkIdStr), ClustersDef.INVALID_ID);
		if (linkId == ClustersDef.INVALID_ID) {
			return ClustersDef.ERR_APP_SERVICE_LINK_EXIST;
		}
		asl.setId(linkId);

		zkClient.writeData(ClustersDirUtils.getAppServiceLinkDir(asl.getServerId(), asl.getAppId(), asl.getLinkServiceId(), asl.getId()), asl.getBytes(), -1);

		LOG.append("id", linkId).appendMessage("add app service link success").info();

		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeAppServiceLink(int serverId, int appId, int serviceId, int id) {
		LOG.append("serverId", serverId).append("appId", appId).append("id", id).append("serviceId", serviceId);
		String linkPath = ClustersDirUtils.getAppServiceLinkDir(serverId, appId, serviceId, id);
		if (!zkClient.exists(linkPath)) {
			LOG.appendMessage("remove app service link failed ,the link not exist").info();
			return ClustersDef.ERR_APP_SERVICE_LINK_NOT_EXIST;
		}

		zkClient.deleteRecursive(linkPath);
		LOG.appendMessage("removed link");
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized List<AppServiceLink> getAppServiceLinks(int serverId, int appId) {

		String appDir = ClustersDirUtils.getAppDir(serverId, appId);
		if (!zkClient.exists(appDir)) {
			return null;
		}
		String rootLinkDir = ClustersDirUtils.getRootAppServiceLink(serverId, appId);
		List<AppServiceLink> linkList = new ArrayList<AppServiceLink>();
		List<String> list = zkClient.getChildren(rootLinkDir);
		for (String str : list) {
			List<String> serviceList = zkClient.getChildren(rootLinkDir + "/" + str);
			for (String service : serviceList) {
				byte[] buffer = zkClient.readData(rootLinkDir + "/" + str + "/" + service, true);
				if (null != buffer) {
					AppServiceLink as = new AppServiceLink();
					as.fromBytes(buffer);
					linkList.add(as);
				}
			}

		}
		return linkList;
	}

	@Override
	public AppServiceLink getAppServiceLink(int serverId, int appId, int linkServerId, int linkAppId, int linkServiceId) {
		List<AppServiceLink> list = getAppServiceLinks(serverId, appId);
		for (AppServiceLink link : list) {
			if (serverId == link.getServerId() && appId == link.getAppId() && linkServerId == link.getLinkServerId() && linkAppId == link.getLinkAppId() && linkServiceId == link.getLinkServiceId()) {
				return link;
			}
		}
		return null;
	}

	@Override
	public synchronized List<AppServiceLink> getAppServiceLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized int addAppService(ApplicationService appService) {
		LOG.append("appId", appService.getAppId()).append("serverId", appService.getServerId()).append("serviceId", appService.getServiceId());
		String appDir = ClustersDirUtils.getAppDir(appService.getServerId(), appService.getAppId());
		if (!zkClient.exists(appDir)) {
			LOG.appendMessage("add app service failed,the app not exist").info();
			return ClustersDef.ERR_APP_NOT_EXIST;
		}
		String serviceDir = ClustersDirUtils.getServiceDir(appService.getServiceId());
		if (!zkClient.exists(serviceDir)) {
			LOG.appendMessage("add app service failed ,the service not exit").info();
			return ClustersDef.ERR_SERVICE_NOT_EXIST;
		}
		String appsDir = ClustersDirUtils.getAppServiceDir(appService.getServerId(), appService.getAppId(), appService.getServiceId());
		if (zkClient.exists(appsDir)) {
			zkClient.writeData(appsDir, appService.getBytes(), -1);
			LOG.append("opt", "update");
		} else {
			createDir(appsDir);
			LOG.append("opt", "add");
		}
		zkClient.writeData(appsDir, appService.getBytes(), -1);

		LOG.appendMessage("add or update app service success").info();
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeAppService(int serverId, int appId, int sid) {

		LOG.append("serverId", serverId).append("appId", appId).append("serviceId", sid);
		String appsDir = ClustersDirUtils.getAppServiceDir(serverId, appId, sid);
		if (!zkClient.exists(appsDir)) {
			LOG.appendMessage("remove app service failed,the app service not exist").info();
			return ClustersDef.ERR_APP_SERVICE_NOT_EXIST;
		}
		zkClient.deleteRecursive(appsDir);
		LOG.appendMessage("removed app service");
		return 0;
	}

	@Override
	public synchronized List<ApplicationService> getAppServices(int serverId, int appId) {
		String appsDir = ClustersDirUtils.getAppServiceDir(serverId, appId);
		if (!zkClient.exists(appsDir)) {
			return null;
		}
		List<String> list = zkClient.getChildren(appsDir);
		List<ApplicationService> appsList = new ArrayList<ApplicationService>();
		for (String str : list) {
			byte[] buffer = zkClient.readData(appsDir + "/" + str, true);
			if (null != buffer) {
				ApplicationService as = new ApplicationService();
				as.fromBytes(buffer);
				appsList.add(as);
			}
		}
		return appsList;
	}

	@Override
	public synchronized int addOrUpdateConfig(int serverId, int appId, String key, byte[] value) {
		LOG.append("serverId", serverId).append("appId", appId).append("key", key).append("value", String.valueOf(value));
		if (!zkClient.exists(ClustersDirUtils.getAppDir(serverId, appId))) {
			LOG.appendMessage("add or update config failed,the app not exist!").info();
			return ClustersDef.ERR_APP_NOT_EXIST;
		}
		createDir(ClustersDirUtils.getAppConfigDir(serverId, appId, key));
		zkClient.writeData(ClustersDirUtils.getAppConfigDir(serverId, appId, key), value, -1);
		LOG.appendMessage("add or update config ").info();
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized int removeConfig(int serverId, int appId, String key) {
		LOG.append("serverId", serverId).append("appId", appId).append("key", key);
		if (!zkClient.exists(ClustersDirUtils.getAppDir(serverId, appId))) {
			LOG.appendMessage("remove config failed,the app not exist!").info();
			return ClustersDef.ERR_APP_NOT_EXIST;
		}

		zkClient.deleteRecursive(ClustersDirUtils.getAppConfigDir(serverId, appId, key));
		LOG.appendMessage("removed config ").info();
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public synchronized Map<String, byte[]> getConfigs(int serverId, int appId) {
		if (!zkClient.exists(ClustersDirUtils.getRootAppConfigDir(serverId, appId))) {
			return null;
		}
		List<String> list = zkClient.getChildren(ClustersDirUtils.getRootAppConfigDir(serverId, appId));
		Map<String, byte[]> valMap = new HashMap<String, byte[]>();
		for (String str : list) {
			byte[] buffer = zkClient.readData(ClustersDirUtils.getAppConfigDir(serverId, appId, str), true);
			valMap.put(str, buffer);
		}
		return valMap;
	}

	@Override
	public synchronized byte[] getConfig(int serverId, int appId, String key) {
		if (!zkClient.exists(ClustersDirUtils.getRootAppConfigDir(serverId, appId))) {
			return null;
		}
		return zkClient.readData(ClustersDirUtils.getAppConfigDir(serverId, appId, key), true);
	}

	@Override
	public synchronized Server findServer(String serverName) {
		String serverDir = ClustersDirUtils.getRootServerDir();
		if (!zkClient.exists(serverDir)) {
			return null;
		}
		List<String> children = zkClient.getChildren(serverDir);
		for (String child : children) {
			byte[] buffer = zkClient.readData(ClustersDirUtils.getServerDir(child), true);
			if (null != buffer) {
				Server server = new Server();
				server.fromBytes(buffer);
				if (server.getServerName().trim().equals(serverName.trim())) {
					return server;
				}
			}
		}
		return null;
	}

	@Override
	public synchronized Application findApp(String serverName, String appName) {

		Server server = findServer(serverName);
		if (null != server) {
			String serverDir = ClustersDirUtils.getServerDir(server.getId());
			if (!zkClient.exists(serverDir)) {
				return null;
			}
			List<String> children = zkClient.getChildren(serverDir);
			for (String child : children) {
				byte[] buffer = zkClient.readData(serverDir + "/" + child, true);
				if (buffer != null) {
					Application app = new Application();
					app.fromBytes(buffer);
					if (app.getName().trim().equals(appName.trim())) {
						return app;
					}
				}
			}
		}

		return null;
	}

	@Override
	public synchronized ApplicationService findAppService(String serverName, String appName, String serviceName) {
		Server server = findServer(serverName);
		if (null == server) {
			return null;
		}
		Application app = findApp(serverName, appName);
		if (null == app) {
			return null;
		}

		Service service = findService(serviceName);
		if (null == service) {
			return null;
		}

		ApplicationService as = getAppService(server.getId(), app.getId(), service.getId());
		return as;
	}

	@Override
	public synchronized Service findService(String serviceName) {
		List<String> children = zkClient.getChildren(ClustersDirUtils.getRootServiceDir());
		for (String child : children) {
			byte[] buffer = zkClient.readData(ClustersDirUtils.getServiceDir(child), true);
			if (null != buffer) {
				Service s = new Service();
				if (s.fromBytes(buffer)) {
					if (s.getServiceName().trim().equals(serviceName.trim())) {
						return s;
					}
				}

			}
		}
		return null;
	}

	@Override
	public synchronized ApplicationService getAppService(int serverId, int appId, int serviceId) {
		String appsDir = ClustersDirUtils.getAppServiceDir(serverId, appId, serviceId);
		if (!zkClient.exists(appsDir)) {
			return null;
		}

		byte[] buffer = zkClient.readData(appsDir, true);
		if (null != buffer) {
			ApplicationService as = new ApplicationService();
			if (as.fromBytes(buffer)) {
				return as;
			}
		}
		return null;
	}

	@Override
	public synchronized boolean isServiceStarted(int serverId, int appId, int serviceId) {
		if (zkClient.exists(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId))) {
			return true;
		}
		return false;
	}

	@Override
	public synchronized int startService(int serverId, int appId, int serviceId) {
		if (!zkClient.exists(ClustersDirUtils.getAppServiceDir(serverId, appId, serviceId))) {
			return ClustersDef.ERR_APP_SERVICE_NOT_EXIST;
		}
		this.createTmpDir(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId));
		return ClustersDef.ERR_SUCESS;
	}

	@Override
	public int stopService(int serverId, int appId, int serviceId) {
		if (zkClient.exists(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId))) {
			zkClient.deleteRecursive(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId));
		}
		return 0;
	}

	@Override
	public boolean canConnectToServer(int serverId, int appId, int serviceId) {
		if (!zkClient.exists(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId))) {
			return false;
		}

		if (!zkClient.exists(ClustersDirUtils.getRootAppServiceConnectionDir(serverId, appId, serviceId))) {
			return true;
		}
		ApplicationService appService = this.getAppService(serverId, appId, serviceId);
		if (null == appService) {
			return false;
		}
		List<String> strList = zkClient.getChildren(ClustersDirUtils.getRootAppServiceConnectionDir(serverId, appId, serviceId));
		if (null == strList || strList.size() < appService.getMaxClient()) {
			return true;
		}
		return false;
	}

	@Override
	public int addServiceConnection(int serverId, int appId, int serviceId, int clientServerId, int clientAppId, int connectionId) {
		if (!zkClient.exists(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId))) {
			return -1;
		}

		this.createTmpDir(ClustersDirUtils.getAppServiceConnectionDir(serverId, appId, serviceId, clientServerId, clientAppId, connectionId));
		return 0;
	}

	@Override
	public int removeServiceConnectionCount(int serverId, int appId, int serviceId, int clientServerId, int clientAppId, int connectionId) {
		if (!zkClient.exists(ClustersDirUtils.getAppServiceConnectionDir(serverId, appId, serviceId, clientServerId, clientAppId, connectionId))) {
			LOG.append("serverId", serverId).append("appId", appId).append("linkServerId", clientServerId).append("linkAppId", clientAppId).append("serviceId", serviceId).append("id", connectionId).appendMessage("connection not exist").error();
			return -1;
		}
		zkClient.deleteRecursive(ClustersDirUtils.getAppServiceConnectionDir(serverId, appId, serviceId, clientServerId, clientAppId, connectionId));
		return 0;
	}

	@Override
	public int getServiceConnectionCount(int serverId, int appId, int serviceId) {
		if (!zkClient.exists(ClustersDirUtils.getAppServiceStateDir(serverId, appId, serviceId))) {
			return 0;
		}

		if (!zkClient.exists(ClustersDirUtils.getRootAppServiceConnectionDir(serverId, appId, serviceId))) {
			return 0;
		}
		ApplicationService appService = this.getAppService(serverId, appId, serviceId);
		if (null == appService) {
			return 0;
		}
		List<String> strList = zkClient.getChildren(ClustersDirUtils.getRootAppServiceConnectionDir(serverId, appId, serviceId));
		if (null == strList || strList.size() < appService.getMaxClient()) {
			return strList.size();
		}
		return 0;
	}

	public int addOrUpdate(String name, Map<String, String> map) {

		int rst = 0;
		try {

			// 根据名字匹配
			if (name.equals("Server")) {
				Server s = new Server();
				s.setServerIp(map.get("serverip"));
				s.setServerName(map.get("servername"));
				rst = this.addOrUpdateServer(s);

			} else if (name.equals("Service")) {
				org.dmp.clusters.Service sv = new org.dmp.clusters.Service();
				sv.setServiceName(map.get("servicename"));
				sv.setServiceClass(map.get("serviceclass"));
				sv.setThriftClass(map.get("thriftclass"));
				rst = this.addOrUpdateService(sv);

			} else if (name.equals("Application")) {
				Application app = new Application();
				Server s = this.findServer(map.get("servername"));
				app.setServerId(s.getId());
				
				app.setName(map.get("appname"));
				app.setBootClass(map.get("bootclass"));
				app.setType(Integer.parseInt(map.get("type")));
				rst = this.addOrUpdateApp(app);

			} else if (name.equals("ApplicationService")) {
				Application app = this.findApp(map.get("servername"), map.get("appname"));
				Service s = this.findService(map.get("servicename"));
				Server sv = this.findServer(map.get("servername"));
				

				ApplicationService as = new ApplicationService();
				as.setAppId(app.getId());
				as.setServerId(sv.getId());
				as.setServiceId(s.getId());
				as.setPort(Integer.parseInt(map.get("port")));
				as.setState(Integer.parseInt(map.get("state")));
				as.setMaxClient(Integer.parseInt(map.get("maxclient")));
				rst = this.addAppService(as);

			} else if (name.equals("AppServiceLink")) {
				Application app = this.findApp(map.get("servername"), map.get("appname"));
				Service si = this.findService(map.get("servicename"));
				Server sv = this.findServer(map.get("servername"));
				
				ApplicationService linkAs = this.findAppService(map.get("linkservername"), map.get("linkappname"), map.get("linkservicename"));
				
				ApplicationService as = new ApplicationService();
				as.setAppId(app.getId());
				as.setServiceId(si.getId());
				
				//-----赋值
				AppServiceLink asl = new AppServiceLink();
 
				
				asl.setServerId(sv.getId());
				
				asl.setAppId(app.getId()); 
				asl.setLinkServerId(linkAs.getServerId()); 
				asl.setLinkAppId(linkAs.getAppId()); 
				asl.setLinkServiceId(linkAs.getServiceId()); 
				
				rst = this.addAppServiceLink(asl);

			} else {
				rst = -2;
			}

		} catch (NumberFormatException e) {
			rst = -4;
		}
		return rst;
	}

	public int remove(String name, Map<String, String> map) throws NullPointerException, NumberFormatException, Exception {

		int rst = 0;

		try {

			// 根据名字匹配
			if (name.equals("Server")) {
				Server s = this.findServer(map.get("servername"));
				
				this.removeServer(s.getId());

			} else if (name.equals("Service")) {
				Service s = this.findService(map.get("servicename"));
				
				this.removeService(s.getId());

			} else if (name.equals("Application")) {
				Application app = this.findApp(map.get("servername"), map.get("appname"));
				
				this.removeApp(app.getServerId(), app.getId());

			} else if (name.equals("ApplicationService")) {
				ApplicationService as = this.findAppService(map.get("servername"), map.get("appname"), map.get("servicename"));
				
				this.removeAppService(as.getServerId(), as.getAppId(), as.getServiceId());

			} else if (name.equals("AppServiceLink")) {
				ApplicationService as = this.findAppService(map.get("servername"), map.get("appname"), map.get("servicename"));
				
				ApplicationService linkAs = this.findAppService(map.get("linkservername"), map.get("linkappname"), map.get("linkservicename"));
				
				AppServiceLink al = this.getAppServiceLink(as.getServerId(), as.getAppId(), linkAs.getServerId(), linkAs.getAppId(), linkAs.getServiceId());
				
				this.removeAppServiceLink(as.getServerId(), as.getAppId(), as.getServiceId(), as.getId());

			} else {
				rst = -2;
			}

		} catch (NumberFormatException e) {
			rst = -4;
		}
		return rst;
	}

}
