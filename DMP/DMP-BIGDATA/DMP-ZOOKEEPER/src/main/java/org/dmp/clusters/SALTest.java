package org.dmp.clusters;

import java.util.List;

/**
 * @ClassName: SALTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author  zhoushubin@unioncast.com 
 * @date 2013-9-6 上午10:37:40
 * 
 */
public class SALTest {
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param args
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		System.out.println("-----------------");
		ClustersManagerImpl cm = new ClustersManagerImpl();
		//增加个IP地址为192.168.1.86的服务
		int serverid = 4;
//		Server new86server = new Server();
////		new86server.setId(serverid); 第一次不需要设置
//		new86server.setServerIp("192.168.1.86");
//		new86server.setServerName("mup-zhou");
//		int srt = cm.addOrUpdateServer(new86server);
//		System.out.println("add server:" + srt);
////		int art = cm.removeServer(serverid);--删除
		
		
		//为新增的Server 增加一个接入层的应用
//		Application salapp = new Application();
//		salapp.setName("sal-zhou");
//		salapp.setServerId(serverid);
//		salapp.setType(55);
//		salapp.setBootClass("");
////		salapp.setId(0);
////		int art = cm.removeApp(serverid, 0);--删除
//		int art = cm.addOrUpdateApp(salapp);
//		System.out.println("add app:" + art);
		
		//为这个APP增加服务--这里没有 就不需要
//		Service services = new Service();
//		services.setServiceName("服务名称");
//		services.setServiceClass("com.unioncast.protobuf.crbt.CrbtAuth$CrbtAuthService");
//		services.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
//		cm.addOrUpdateService(services);
		
		
		//为这个APP增加管理的连接许可，因为SAL需要管理彩铃服务，会员服务
		//彩铃服务有：app serverid[3]:appid [3],name[bllcrbt04],servicesid[8~12]，app serverid[2]:appid [3],name[bllcrbt03]，app serverid[1]:appid [3],name[bllcrbt02]，app serverid[0]:appid [3],name[bllcrbt01]servicesid[]
		int serverid_index = 4;
		int servicesid_index = 14; 
//		for(int i =0;i<serverid_index;i++){
//			for(int m = 8;m<servicesid_index;m++){
//				AppServiceLink asl = new AppServiceLink();
//				asl.setServerId(serverid);
//				asl.setAppId(0);
//				asl.setLinkServerId(i);
//				asl.setLinkAppId(3);
//				asl.setLinkServiceId(m);
////				asl.setId(salid_index);
////				cm.removeAppServiceLink(i, 3, m,salid_index);//?参数怎么填写
//				int aslrt = cm.addAppServiceLink(asl);
//				System.out.println(aslrt+i+" -----"+m);
//			}
//		}
		
		//会员服务有：app serverid[0]:appid [0],name[bll01]，app serverid[1]:appid [0],name[bll02],app serverid[2]:appid [0],name[bll03],app serverid[3]:appid [0],name[bll04] serviceid[0~2]
		
//		 serverid_index = 4;
//		 servicesid_index = 3; 
//		for(int i = 0;i<serverid_index;i++){
//			for(int m = 0;m<servicesid_index;m++){
//				AppServiceLink asl = new AppServiceLink();
//				asl.setServerId(serverid);
//				asl.setAppId(0);
//				asl.setLinkServerId(i);
//				asl.setLinkAppId(0);
//				asl.setLinkServiceId(m);
////				asl.setId(salid_index);
////				cm.removeAppServiceLink(i, 3, m,salid_index);//?参数怎么填写
//				int aslrt = cm.addAppServiceLink(asl);
//				System.out.println(aslrt+i+" -----"+m);
//			}
//		}
		
		//读取所有的服务，
		List<Server> serverList = cm.getServers();
		System.out.println(serverList.size());
		for(Server server:serverList){
			System.out.println(String.format("server:id [%s],ip[%s],name[%s]", server.getId(),server.getServerIp(),server.getServerName()));
			List<Application> alist = cm.getApps(server.getId());//获取这个服务器下的所有服务
			System.out.println("================================");
			for(Application app:alist){
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println(String.format("app serverid[%s]:appid [%s],name[%s]",app.getServerId(), app.getId(),app.getName()));
				List<ApplicationService> aslist = cm.getAppServices(server.getId(), app.getId());//获取这个应用下所提供的能力
				for(ApplicationService as:aslist){
					System.out.println(String.format("services appid[%s],serveresid[%s],,port[%s]", as.getAppId(),as.getServiceId(),as.getPort()));
				}
				List<AppServiceLink> aslllist = cm.getAppServiceLinks(server.getId(), app.getId());//获取关联的应用
				for(AppServiceLink asl:aslllist){
					System.out.println(String.format("links: app[%s],linkserver[%s],linkapp[%s],linkserveresid[%s]",asl.getAppId(),asl.getLinkServerId(), asl.getLinkAppId(),asl.getLinkServiceId()));
				}
			}
			
			List<Service> list = cm.getServices();
			for (Service s : list) {
				// cm.removeService(s.getId());
				System.out.println(String.format("id[%s],serverceName[%s],servericClass[%s]", s.getId(),s.getServiceName(),s.getServiceClass()));
			}
		}
		
	}

}
