package org.dmp.clusters;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.dmp.conf.BaseConfigKey;
import org.dmp.util.BytesUtils;
import org.dmp.zookeeper.ZKClient;
import org.dmp.zookeeper.exception.ZkException;


public class Test4 {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws RuntimeException
	 * @throws ZkException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws Exception {
 		ZKClient zkClient=ZKClient.getInstance();
 	   zkClient.createPersistent("/uc/cluster");
		ClustersManagerImpl cm = new ClustersManagerImpl();
		int unit = 0;
 
		if(unit==-8){
			for (int i = 0; i < 4; i++) 
			{
						cm.addOrUpdateConfig(i, 0, BaseConfigKey.FJDRP_SERVICE_PATH, "http://202.101.123.5:8080/msag_client/".getBytes());
						cm.addOrUpdateConfig(i, 0, BaseConfigKey.SCMEMBER_SERVICE_PATH, "http://125.64.15.246:8100/msag_client/".getBytes());
						cm.addOrUpdateConfig(i, 0, BaseConfigKey.MSAGRES_SERVICE_PATH, "http://192.168.1.22:8088/res".getBytes());
			}
			return;
 		}
 
		if (unit == -7) {
			ApplicationService apps = cm.getAppService(1, 0, 1);
			System.out.println(apps.getMaxClient());
		}
		if (unit == -3) {
			List<Service> list = cm.getServices();
			for (Service s : list) {
				// cm.removeService(s.getId());
				System.out.println(s.getId() + "|" + s.getServiceName());
			}
			// cm.findAppService("mgpserver", "mgpclient1", "mgpservice");
			return;
		}
		if (unit == -2) {
			Application app = cm.findApp("hadoopserver2", "mup_logic_member");
			System.out.println("find app:" + app.getName());
			return;
		}

		if (unit == -1) {
			List<Server> list = cm.getServers();
			for (Server s : list) {
				System.out.println("=====================start================================");
				System.out.println("server:" + s.getId() + "|" + s.getServerName());
				List<Application> alist = cm.getApps(s.getId());
				for (Application a : alist) {
					System.out.println("app:" + a.getId() + "|" + a.getName() + "|" + a.getType());
					List<ApplicationService> aslist = cm.getAppServices(s.getId(), a.getId());
					for (ApplicationService as : aslist) {
						System.out.println("apps:" + as.getServiceId() + "|" + as.getPort());
					}
					List<AppServiceLink> asllist = cm.getAppServiceLinks(s.getId(), a.getId());
					for (AppServiceLink asl : asllist) {
						System.out.println("asl:" + asl.getId() + "|" + asl.getLinkServerId() + "|" + asl.getLinkAppId() + "|" + asl.getLinkServiceId());
					}
				}
				System.out.println("=====================end=================================");
			}
			return;
		}

		if (unit == 0) {

			String root = "test";
//			if (args != null) {
//				root = args[0];
//				
//			}
			System.out.println("ZKROOT:"+root);
			ZKClient client = ZKClient.getInstance();
			client.deleteRecursive("/" + root);
			client.createPersistent("/" + root + "/cluster/service");
			client.createPersistent("/" + root + "/cluster/server");
		}

		if (unit == 0) {

			Server server = new Server();
			server.setServerIp("192.168.1.55");
			server.setServerName("mup-401");

			int rst = cm.addOrUpdateServer(server);
			System.out.println("add server:" + rst);
			Server server2 = new Server();
			server2.setServerIp("192.168.1.56");
			server2.setServerName("mup-402");
			rst = cm.addOrUpdateServer(server2);
			System.out.println("add server:" + rst);

			Server server3 = new Server();
			server3.setServerIp("192.168.1.57");
			server3.setServerName("mup-403");
			rst = cm.addOrUpdateServer(server3);
			System.out.println("add server:" + rst);

			Server server4 = new Server();
			server4.setServerIp("192.168.1.58");
			server4.setServerName("mup-404");
			rst = cm.addOrUpdateServer(server4);
			System.out.println("add server:" + rst);
			List<Server> list = cm.getServers();
			for (Server s : list) {
				System.out.println(s.getId() + "|" + s.getServerName());
			}
		}
		// else if (unit == 1)
		{
			for (int i = 0; i < 4; i++) {
				Application app = new Application();
				app.setName("bll0"+(i+1));
				app.setServerId(i);
				app.setType(1);
				app.setBootClass("");
				// app.setId(0);
				int rst = cm.addOrUpdateApp(app);
				System.out.println("add app:" + rst);

				Application app2 = new Application();
				app2.setName("cal0"+(i+1));
				app2.setServerId(i);
				app2.setType(55);
				app2.setBootClass("com.unioncast.mup.cal.mgp.main.MGPBoot/com.unioncast.cal.sms.main.SmsBootService");
				rst = cm.addOrUpdateApp(app2);

				System.out.println("add app:" + rst);
				

				Application app3 = new Application();
				app3.setName("sal0"+(i+1));
				app3.setServerId(i);
				app3.setType(55);
				app3.setBootClass("");
				rst = cm.addOrUpdateApp(app3);
				System.out.println("add app:" + rst);
				
				
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.MGP_SERVER_IP, "192.168.1.61".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.MGP_SERVER_PORT, "int:9610".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_IP, "192.168.1.61".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_PORT, "int:9060".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_ID, "int:60".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_CLIENT_ID, "int:328".getBytes());
				cm.addOrUpdateConfig(i, 1,"mgp.user.name",BytesUtils.toBytes("plusserver"));
				cm.addOrUpdateConfig(i,1,"mgp.password",BytesUtils.toBytes("plusserver"));
				cm.addOrUpdateConfig(i, 1,"mgp.src.addr",BytesUtils.toBytes("011008"));
				
				
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.FJDRP_SERVICE_PATH, "string:http://202.101.123.5:8080/msag_client/".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.SCMEMBER_SERVICE_PATH, "string:http://125.64.15.246:8100/msag_client/".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.MSAGRES_SERVICE_PATH, "string:http://192.168.1.22:8088/res".getBytes());
			}
			List<Application> list = cm.getApps(0);
			for (Application a : list) {
				// cm.removeApp(a.getServerId(), a.getId());
				System.out.println(a.getId() + "|" + a.getName() + "|" + a.getServerId() + "|" + a.getType());
			}
		}
		// else if(unit==2)
		{

			Service s1 = new Service();
			s1.setServiceName("common");
			s1.setServiceClass("com.unioncast.protobuf.base.common.Common$CommonService");
			s1.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			cm.addOrUpdateService(s1);

			Service s2 = new Service();
			s2.setServiceName("userService");
			s2.setServiceClass("com.unioncast.protobuf.base.user.UserBase$UserBaseService");
			s2.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			cm.addOrUpdateService(s2);

			Service s3 = new Service();
			s3.setServiceName("ability");
			s3.setServiceClass("com.unioncast.protobuf.base.ability.BottomAbility$BottomAbilityService");
			s3.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			cm.addOrUpdateService(s3);

			Service s4 = new Service();
			s4.setServiceName("mgporder");
			s4.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtOrder$MGPCrbtOrderService");
			s4.setThriftClass("com.unioncast.cal.mgp.service.MGPOrderServiceImpl");
			cm.addOrUpdateService(s4);

			Service s5 = new Service();
			s5.setServiceName("mgpmember");
			s5.setServiceClass("com.unioncast.protobuf.mgp.MGPMemberOpt$MGPMemberOptService");
			s5.setThriftClass("com.unioncast.cal.mgp.service.MGPMemberServiceImpl");
			cm.addOrUpdateService(s5);

			Service s6 = new Service();
			s6.setServiceName("sms");
			s6.setServiceClass("com.unioncast.protobuf.sms.Sms$SmsService");
			s6.setThriftClass("com.unioncast.cal.sms.smgp.service.SmsProtobufServiceImpl");
			cm.addOrUpdateService(s6);

			Service s7 = new Service();
			s7.setServiceName("ismp");
			s7.setServiceClass("com.unioncast.protobuf.ismp.ISMP$IsmpService");
			s7.setThriftClass("com.unioncast.cal.ismp.service.IsmpProtobufService");
			cm.addOrUpdateService(s7);
			
			Service s8 = new Service();
			s8.setServiceName("MGPCRBTRING");
			s8.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtRing$MGPCrbtRingService");
			s8.setThriftClass("com.unioncast.cal.mgp.service.MGPCrbtRingServiceImpl");
			cm.addOrUpdateService(s8);

			// Service s2=new Service();
			// s2.setServiceName("smsservice");
			// cm.addOrUpdateService(s2);

			List<Service> list = cm.getServices();
			for (Service s : list) {
				// cm.removeService(s.getId());
				System.out.println(s.getId() + "|" + s.getServiceName());
			}

		}// else if(unit==3)
		{
			for (int i = 0; i < 4; i++) {
				ApplicationService as = new ApplicationService();
				as.setAppId(0);
				as.setPort(9000);
				as.setServiceId(0);
				as.setState(0);
				as.setServerId(i);
				as.setMaxClient(200);
				int rst = cm.addAppService(as);
				System.out.println("add apps:" + rst);

				ApplicationService as2 = new ApplicationService();
				as2.setAppId(0);
				as2.setPort(9000);
				as2.setServiceId(1);
				as2.setState(0);
				as2.setServerId(i);
				as2.setMaxClient(200);
				rst = cm.addAppService(as2);
				System.out.println("add apps:" + rst);
				cm.addOrUpdateConfig(i, 0, "config.sms.host", BytesUtils.toBytes("192.168.1.61"));
				cm.addOrUpdateConfig(i, 0, "config.sms.port", BytesUtils.toBytes(9060));
				
				ApplicationService as3 = new ApplicationService();
				as3.setAppId(0);
				as3.setPort(9000);
				as3.setServiceId(2);
				as3.setState(0);
				as3.setServerId(i);
				as3.setMaxClient(200);
				rst = cm.addAppService(as3);
				System.out.println("add apps:" + rst);

				ApplicationService as4 = new ApplicationService();
				as4.setAppId(1);
				as4.setPort(9001);
				as4.setServiceId(3);
				as4.setState(0);
				as4.setServerId(i);
				as4.setMaxClient(200);
				rst = cm.addAppService(as4);
				System.out.println("add apps:" + rst);

				ApplicationService as5 = new ApplicationService();
				as5.setAppId(1);
				as5.setPort(9001);
				as5.setServiceId(4);
				as5.setState(0);
				as5.setServerId(i);
				as5.setMaxClient(200);
				rst = cm.addAppService(as5);
				System.out.println("add apps:" + rst);

				ApplicationService as6 = new ApplicationService();
				as6.setAppId(1);
				as6.setPort(9001);
				as6.setServiceId(5);
				as6.setState(0);
				as6.setServerId(i);
				as6.setMaxClient(200);
				rst = cm.addAppService(as6);
				System.out.println("add apps:" + rst);

				ApplicationService as7 = new ApplicationService();
				as7.setAppId(1);
				as7.setPort(9001);
				as7.setServiceId(6);
				as7.setState(0);
				as7.setServerId(i);
				as7.setMaxClient(200);
				rst = cm.addAppService(as7);
				System.out.println("add apps:" + rst);
				
				ApplicationService as8 = new ApplicationService();
				as8.setAppId(1);
				as8.setPort(9001);
				as8.setServiceId(7);
				as8.setState(0);
				as8.setServerId(i);
				as8.setMaxClient(200);
				rst = cm.addAppService(as8);
				System.out.println("add apps:" + rst);

			}

			List<ApplicationService> list = cm.getAppServices(0, 0);
			for (ApplicationService a : list) {
				System.out.println(a.getServerId() + "|" + a.getAppId() + "|" + a.getServiceId() + "|" + a.getPort());
			}
		}// else if (unit==4)
		{
			int[] z = { 0, 1, 2, 3 };
			for (int i = 0; i < 4; i++) {
				for (int a = 0; a < 4; a++) {
					AppServiceLink asl = new AppServiceLink();
					asl.setServerId(i);
					asl.setAppId(0);
					asl.setLinkServerId(a);
					asl.setLinkAppId(1);
					asl.setLinkServiceId(3);

					int rst = cm.addAppServiceLink(asl);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl3 = new AppServiceLink();
					asl3.setServerId(i);
					asl3.setAppId(0);
					asl3.setLinkServerId(a);
					asl3.setLinkAppId(1);
					asl3.setLinkServiceId(4);

					rst = cm.addAppServiceLink(asl3);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl2 = new AppServiceLink();
					asl2.setServerId(i);
					asl2.setAppId(0);
					asl2.setLinkServerId(a);
					asl2.setLinkAppId(1);
					asl2.setLinkServiceId(5);
					//
					rst = cm.addAppServiceLink(asl2);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl4 = new AppServiceLink();
					asl4.setServerId(i);
					asl4.setAppId(0);
					asl4.setLinkServerId(a);
					asl4.setLinkAppId(1);
					asl4.setLinkServiceId(6);
					//
					rst = cm.addAppServiceLink(asl4);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl5 = new AppServiceLink();
					asl5.setServerId(i);
					asl5.setAppId(2);
					asl5.setLinkServerId(a);
					asl5.setLinkAppId(0);
					asl5.setLinkServiceId(0);
					//
					rst = cm.addAppServiceLink(asl5);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl6 = new AppServiceLink();
					asl6.setServerId(i);
					asl6.setAppId(2);
					asl6.setLinkServerId(a);
					asl6.setLinkAppId(0);
					asl6.setLinkServiceId(1);
					//
					rst = cm.addAppServiceLink(asl6);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl7 = new AppServiceLink();
					asl7.setServerId(i);
					asl7.setAppId(2);
					asl7.setLinkServerId(a);
					asl7.setLinkAppId(0);
					asl7.setLinkServiceId(2);
					//
					rst = cm.addAppServiceLink(asl7);
					System.out.println("add appsl:" + rst);
					
					
//					AppServiceLink asl8 = new AppServiceLink();
//					asl8.setServerId(i);
//					asl8.setAppId(1);
//					asl8.setLinkServerId(a);
//					asl8.setLinkAppId(0);
//					asl8.setLinkServiceId(0);
//					
//					//
//					rst = cm.addAppServiceLink(asl8);
//					System.out.println("add appsl:" + rst);
				}
			}
			// System.out.println("add appsl:" + rst);

			//
			// AppServiceLink asl2=new AppServiceLink();
			// asl2.setServerId(1);
			// asl2.setAppId(0);
			// asl2.setLinkServerId(0);
			// asl2.setLinkAppId(0);
			// asl2.setLinkServiceId(0);
			//
			// rst=cm.addAppServiceLink(asl2);

		}
		System.out.println("============================================================");

	}

}
