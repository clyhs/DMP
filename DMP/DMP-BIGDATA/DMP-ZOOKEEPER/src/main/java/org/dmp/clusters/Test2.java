package org.dmp.clusters;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.dmp.zookeeper.ZKClient;
import org.dmp.zookeeper.exception.ZkException;


public class Test2 {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws RuntimeException
	 * @throws ZkException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws Exception {
		// ZKClient
		// zkClient=ZKClient.getInstance("192.168.1.56:2181,192.168.1.57:2181,192.168.1.58:2181");
		// zkClient.createPersistent("/test/cluster/server");
		ClustersManagerImpl cm = new ClustersManagerImpl();
		int unit =5;
		
		if(unit==-3){
			List<Service> list = cm.getServices();
			for(Service s:list){
				//cm.removeService(s.getId());
				System.out.println(s.getId()+"|"+s.getServiceName());
			}
			//cm.findAppService("mgpserver", "mgpclient1", "mgpservice");
			return ;
		}
		if(unit==-2){
			Application app = cm.findApp("hadoopserver2", "mup_logic_member");
			System.out.println("find app:"+app.getName());
			return;
		}
		
		if(unit==-1){
			List<Server> list = cm.getServers();
			for (Server s : list) {
				System.out.println("=====================start================================");
				System.out.println("server:"+s.getId() + "|" + s.getServerName());
				List<Application> alist = cm.getApps(s.getId());
				for(Application a:alist){
					System.out.println("app:"+a.getId()+"|"+a.getName()+"|"+a.getType());
					List<ApplicationService> aslist = cm.getAppServices(s.getId(), a.getId());
					for(ApplicationService as:aslist){
						System.out.println("apps:"+as.getServiceId()+"|"+as.getPort());
					}
					List<AppServiceLink> asllist = cm.getAppServiceLinks(s.getId(), a.getId());
					for(AppServiceLink asl:asllist){
						System.out.println("asl:"+asl.getId()+"|"+asl.getLinkServerId()+"|"+asl.getLinkAppId()+"|"+asl.getLinkServiceId());
					}
				}
				System.out.println("=====================end=================================");
			}
			return;
		}
		
		//if (unit == 0) 
		{
			Server server = new Server();
			server.setServerIp("192.168.1.129");
			server.setServerName("bll59");
			int rst = cm.addOrUpdateServer(server);
			System.out.println("add server:" + rst);
			Server server2 = new Server();
			server2.setServerIp("192.168.1.60");
			server2.setServerName("cal60");
			rst = cm.addOrUpdateServer(server2);
			System.out.println("add server:" + rst);
			
			Server server3 = new Server();
			server3.setServerIp("192.168.1.108");
			server3.setServerName("web108");
			rst = cm.addOrUpdateServer(server3);
			System.out.println("add server:" + rst);
			List<Server> list = cm.getServers();
			for (Server s : list) {
				System.out.println(s.getId() + "|" + s.getServerName());
			}
		} 
		//else if (unit == 1) 
		{
			Application app = new Application();
			app.setName("bllcrbtapp59");
			app.setServerId(0);
			app.setType(1);
			app.setBootClass("com.unioncast.mup.bll.crbt.BootServiceBllImpl");
			int rst = cm.addOrUpdateApp(app);
			System.out.println("add app:" + rst);
			
			Application app2 = new Application();
			app2.setName("cal60");
			app2.setServerId(0);
			app2.setType(55);
			app2.setBootClass("com.unioncast.mup.cal.mgp.main.MGPBoot");
			 rst = cm.addOrUpdateApp(app2);
			System.out.println("add app:" + rst);
			
			Application app3 = new Application();
			app3.setName("web108");
			app3.setServerId(2);
			app3.setType(55);
			app3.setBootClass("");
			 rst = cm.addOrUpdateApp(app3);
			System.out.println("add app:" + rst);
			
			List<Application> list = cm.getApps(0);
			for(Application a:list){
				//cm.removeApp(a.getServerId(), a.getId());
				System.out.println(a.getId()+"|"+a.getName()+"|"+a.getServerId()+"|"+a.getType());
			}
		}
		//else if(unit==2)
		{
			
			Service s2=new Service();
			s2.setServiceName("BllCrbtService");
			s2.setServiceClass("com.unioncast.mup.bll.crbt.BllCrbtThriftService");
			s2.setThriftClass("com.unioncast.thrift.crbt.order.BllCrbtService");
			cm.addOrUpdateService(s2);
			
			Service s1=new Service();
			s1.setServiceName("MGPService");
			s1.setServiceClass("com.unioncast.mgp.service.MGPThriftService");
			s1.setThriftClass("com.unioncast.thrift.mgp.crbt.MGPService");
	
			cm.addOrUpdateService(s1);
			
			
			
//			Service s2=new Service();
//			s2.setServiceName("smsservice");
//			cm.addOrUpdateService(s2);
			
			List<Service> list = cm.getServices();
			for(Service s:list){
				//cm.removeService(s.getId());
				System.out.println(s.getId()+"|"+s.getServiceName());
			}
			
		}//else if(unit==3)
		{
			ApplicationService as=new ApplicationService();
			as.setAppId(0);
			as.setPort(9989);
			as.setServiceId(0);
			as.setState(0);
			as.setServerId(0);
			as.setMaxClient(15);
			int rst=cm.addAppService(as);
			System.out.println("add apps:" + rst);
			
			ApplicationService as2=new ApplicationService();
			as2.setAppId(0);
			as2.setPort(9988);
			as2.setServiceId(0);
			as2.setState(0);
			as2.setServerId(1);
			as2.setMaxClient(15);
			 rst=cm.addAppService(as2);
			System.out.println("add apps:" + rst);
			
			
			List<ApplicationService> list = cm.getAppServices(0, 0);
			for(ApplicationService a:list){
				System.out.println(a.getServerId()+"|"+a.getAppId()+"|"+a.getServiceId()+"|"+a.getPort());
			}
		}//else if (unit==4)
		{
			AppServiceLink asl=new AppServiceLink();
			asl.setServerId(0);
			asl.setAppId(0);
			asl.setLinkServerId(0);
			asl.setLinkAppId(0);
			asl.setLinkServiceId(1);
			
			int rst=cm.addAppServiceLink(asl);
			System.out.println("add appsl:" + rst);
			
			AppServiceLink asl2=new AppServiceLink();
			asl2.setServerId(2);
			asl2.setAppId(0);
			asl2.setLinkServerId(0);
			asl2.setLinkAppId(0);
			asl2.setLinkServiceId(0);
//			
		rst=cm.addAppServiceLink(asl2);
//			System.out.println("add appsl:" + rst);
			
			
//			AppServiceLink asl2=new AppServiceLink();
//			asl2.setServerId(1);
//			asl2.setAppId(0);
//			asl2.setLinkServerId(0);
//			asl2.setLinkAppId(0);
//			asl2.setLinkServiceId(0);
//			
//			 rst=cm.addAppServiceLink(asl2);
			System.out.println("add appsl:" + rst);
		}
		System.out
				.println("============================================================");

	}

}
