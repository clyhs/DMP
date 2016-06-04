package org.dmp.clusters;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.dmp.conf.BaseConfigKey;
import org.dmp.util.BytesUtils;
import org.dmp.zookeeper.ZKClient;
import org.dmp.zookeeper.exception.ZkException;


/**
 * 现网ZK版本
 * @author simple
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws RuntimeException
	 * @throws ZkException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws Exception {
// 		ZKClient zkClient=ZKClient.getInstance();
// 	   zkClient.createPersistent("/uc/cluster");
		ClustersManagerImpl cm = new ClustersManagerImpl();
		int unit = 11;
		
		if(unit==11){
			
//			cm.addOrUpdateConfig(0, 1, BaseConfigKey.MGP_SERVER_IP, "string:172.17.101.7:9210".getBytes());
//			cm.addOrUpdateConfig(0, 1, BaseConfigKey.MGP_SERVER_PORT, "int:9610".getBytes());
//
//			cm.addOrUpdateConfig(0, 1,"mgp.user.name",BytesUtils.toBytes("plusserver"));
//			cm.addOrUpdateConfig(0,1,"mgp.password",BytesUtils.toBytes("plusserver"));
//			cm.addOrUpdateConfig(0, 1,"mgp.src.addr",BytesUtils.toBytes("011008"));
//			System.out.println("update mgp success!");
			for(int i=0;i<4;i++){
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_IP, "172.17.99.14".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_PORT, "int:9199".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_ID, "int:199".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_CLIENT_ID, "int:328".getBytes());
				//cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_GW_NAME, "int:328".getBytes());
				
			}
			System.out.println("update Sms config success");
			return ;
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

			String root = "mup";
			System.out.println("xx");
			System.out.println("ZKROOT:"+root);
			ZKClient client = ZKClient.getInstance();
			client.deleteRecursive("/" + root);
			client.createPersistent("/" + root + "/cluster/service");
			client.createPersistent("/" + root + "/cluster/server");
		}

		if (unit == 0) {
			//新增服务器 包括IP 和NAME
		      Server server = new Server();
		      server.setServerIp("172.17.1.5");
		      server.setServerName("mup-401");

		      int rst = cm.addOrUpdateServer(server);
		      System.out.println("add server:" + rst);
		      Server server2 = new Server();
		      server2.setServerIp("172.17.1.6");
		      server2.setServerName("mup-402");
		      rst = cm.addOrUpdateServer(server2);
		      System.out.println("add server:" + rst);

		      Server server3 = new Server();
		      server3.setServerIp("172.17.1.7");
		      server3.setServerName("mup-403");
		      rst = cm.addOrUpdateServer(server3);
		      System.out.println("add server:" + rst);

		      Server server4 = new Server();
		      server4.setServerIp("172.17.1.8");
		      server4.setServerName("mup-404");
		      rst = cm.addOrUpdateServer(server4);
		      System.out.println("add server:" + rst);
			
			List<Server> list = cm.getServers();
			for (Server s : list)
			{
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
				app.setBootClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
				// app.setId(0);
				int rst = cm.addOrUpdateApp(app);
				System.out.println("add app,APPName:bll0"+(i+1)+":" + rst);
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.FJDRP_SERVICE_PATH, "string:http://202.101.123.5:8080/msag_client/".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.SCMEMBER_SERVICE_PATH, "string:http://125.64.15.246:8100/msag_client/".getBytes());
//		        cm.addOrUpdateConfig(i, 0, BaseConfigKey.MSAGRES_SERVICE_PATH, "string:http://192.168.1.48:8080/res".getBytes());
			 	cm.addOrUpdateConfig(i, 0, BaseConfigKey.MSAGRES_SERVICE_PATH, "string:http://172.17.99.1:9080/ResDiyBox".getBytes());
			 	
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.BJ_SPECIAl_PROVINCEID, "string:|21|20|05|07|25|18|".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.RINGBOX_PROVINCEID, "string:|05|".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.RINGGROUP_PROVINCEID, "string:|21|20|07|25|18|".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.RINGGROUP_DEFAULTNAME, "string:VIP3_Tone_Group".getBytes());
				cm.addOrUpdateConfig(i, 0, BaseConfigKey.ETHNICLANGUAGEMBR_BOXID, "string:810099995363".getBytes());

				
				Application app2 = new Application();
				app2.setName("cal0"+(i+1));
				app2.setServerId(i);
				app2.setType(55);
				app2.setBootClass("com.unioncast.mup.cal.mgp.main.MGPBoot/com.unioncast.cal.sms.main.SmsBootService");
				rst = cm.addOrUpdateApp(app2);

				System.out.println("add app,APPName:cal0"+(i+1)+":" + rst);
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.MGP_SERVER_IP, "string:172.17.101.7:9210,172.17.101.8:9210".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.MGP_SERVER_PORT, "int:9610".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_IP, "10.1.1.6".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_PORT, "int:9060".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_SERVER_ID, "int:60".getBytes());
				cm.addOrUpdateConfig(i, 1, BaseConfigKey.SMS_CLIENT_ID, "int:328".getBytes());
				cm.addOrUpdateConfig(i, 1,"mgp.user.name",BytesUtils.toBytes("plusserver,plusserver"));
				cm.addOrUpdateConfig(i,1,"mgp.password",BytesUtils.toBytes("plusserver,plusserver"));
				cm.addOrUpdateConfig(i, 1,"mgp.src.addr",BytesUtils.toBytes("011008"));

				Application app3 = new Application();
				app3.setName("sal0"+(i+1));
				app3.setServerId(i);
				app3.setType(55);
				app3.setBootClass("");
				rst = cm.addOrUpdateApp(app3);
				System.out.println("add app,APPName:sal0"+(i+1)+":" + rst);
				
				
				Application app4 = new Application();
				app4.setName("bllcrbt0"+(i+1));
				app4.setServerId(i);
				app4.setType(55);
				app4.setBootClass("");
				rst = cm.addOrUpdateApp(app4);
				System.out.println("add app,APPName:bllcrbt0"+(i+1)+":" + rst);
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
			//serviceid 0 
			
			int r=cm.addOrUpdateService(s1);
			System.out.println(r);
			Service s2 = new Service();
 			s2.setServiceName("userService");
			s2.setServiceClass("com.unioncast.protobuf.base.user.UserBase$UserBaseService");
			s2.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			r=cm.addOrUpdateService(s2);
			System.out.println(r);

			Service s3 = new Service();
 			s3.setServiceName("ability");
			s3.setServiceClass("com.unioncast.protobuf.base.ability.BottomAbility$BottomAbilityService");
			s3.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			r=cm.addOrUpdateService(s3);
			

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
			
			//serviceid 8
			Service s9 = new Service();
			s9.setServiceName("MGPCRBTRINGSET");
			s9.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtRingSet$MGPCrbtRingSetService");
			s9.setThriftClass("com.unioncast.cal.mgp.service.MGPRingSetServiceImpl");
			cm.addOrUpdateService(s9);
			
			//serviceid 9
			Service s10 = new Service();
			s10.setServiceName("MGPCRBTRINGGROUP");
			s10.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtRingGroup$MGPCrbtRingGroupService");
			s10.setThriftClass("com.unioncast.cal.mgp.service.MGPCrbtRingGroupServiceImpl");
			cm.addOrUpdateService(s10);
			
			
			Service s11 = new Service();
			s11.setServiceName("MGPCRBTUSERGROUP");
			s11.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtUserGroup$MGPCrbtUserGroupService");
			s11.setThriftClass("com.unioncast.cal.mgp.service.MGPCrbtUserGroupServiceImpl");
			cm.addOrUpdateService(s11);
			
			Service s12 = new Service();
			s12.setServiceName("MGPCRBTAUTH");
			s12.setServiceClass("com.unioncast.protobuf.mgp.MGPCrbtAuth$MGPCrbtAuthService");
			s12.setThriftClass("com.unioncast.cal.mgp.service.MGPCrbtAuthServiceImpl");
			cm.addOrUpdateService(s12);
			
			//service id 12
			Service s13 = new Service();
			s13.setServiceName("CrbtAuth");
			s13.setServiceClass("com.unioncast.protobuf.crbt.CrbtAuth$CrbtAuthService");
			s13.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s13);
			
			
			Service s14 = new Service();
			s14.setServiceName("CrbtOrder");
			s14.setServiceClass("com.unioncast.protobuf.crbt.CrbtOrder$CrbtOrderService");
			s14.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s14);
			
			Service s15 = new Service();
			s15.setServiceName("CrbtRing");
			s15.setServiceClass("com.unioncast.protobuf.crbt.CrbtRing$CrbtRingService");
			s15.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s15);
			
			
			Service s16 = new Service();
			s16.setServiceName("CrbtRingGroup");
			s16.setServiceClass("com.unioncast.protobuf.crbt.CrbtRingGroup$CrbtRingGroupService");
			s16.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s16);
			
			
			Service s17 = new Service();
			s17.setServiceName("CrbtRingSet");
			s17.setServiceClass("com.unioncast.protobuf.crbt.CrbtRingSet$CrbtRingSetService");
			s17.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s17);
			
			
			Service s18 = new Service();
			s18.setServiceName("CrbtUserGroup");
			s18.setServiceClass("com.unioncast.protobuf.crbt.CrbtUserGroup$CrbtUserGroupService");
			s18.setThriftClass("com.unioncast.mup.bll.crbt.util.CrbtServiceWrap");
			cm.addOrUpdateService(s18);
			
			
			Service s19 = new Service();
 			s19.setServiceName("RecordQuery");
			s19.setServiceClass("com.unioncast.protobuf.base.record.RecordQuery$RecordQueryService");
			s19.setThriftClass("com.unioncast.mup.bllrpc.util.UserBaseTServiceWrapperImpl");
			r=cm.addOrUpdateService(s19);
			

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
			//ApplicationService 表示APP应用跟service服务的关联,表示某一个APP需要  “启动”的服务集
			for (int i = 0; i < 4; i++) {
				//App为0的表示是会员逻辑层服务, 表示APP应用跟service服务的关联,启动serviceid 为0 1 2 
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

			
				//App为1的表示是汇接层服务, 表示APP应用跟service服务的关联,启动serviceid 为3 4 5  
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
				
				
				ApplicationService as9 = new ApplicationService();
				as9.setAppId(1);
				as9.setPort(9001);
				as9.setServiceId(8);
				as9.setState(0);
				as9.setServerId(i);
				as9.setMaxClient(200);
				rst = cm.addAppService(as9);
				System.out.println("add apps:" + rst);
				
				
				ApplicationService as10 = new ApplicationService();
				as10.setAppId(1);
				as10.setPort(9001);
				as10.setServiceId(9);
				as10.setState(0);
				as10.setServerId(i);
				as10.setMaxClient(200);
				rst = cm.addAppService(as10);
				System.out.println("add apps:" + rst);
				
				ApplicationService as11 = new ApplicationService();
				as11.setAppId(1);
				as11.setPort(9001);
				as11.setServiceId(10);
				as11.setState(0);
				as11.setServerId(i);
				as11.setMaxClient(200);
				rst = cm.addAppService(as11);
				System.out.println("add apps:" + rst);
				
				ApplicationService as12 = new ApplicationService();
				as12.setAppId(1);
				as12.setPort(9001);
				as12.setServiceId(11);
				as12.setState(0);
				as12.setServerId(i);
				as12.setMaxClient(200);
				rst = cm.addAppService(as12);
				System.out.println("add apps:" + rst);

				//App为2的表示是接入层服务, 目前接入层不提供任何service，无serviceid
				
				//App为3的表示是彩铃逻辑服务, 表示APP应用跟service服务的关联,启动serviceid 为12 13 14 15 16 17   
				ApplicationService as13 = new ApplicationService();
				as13.setAppId(3);
				as13.setPort(9002);
				as13.setServiceId(12);
				as13.setState(0);
				as13.setServerId(i);
				as13.setMaxClient(200);
				 rst = cm.addAppService(as13);
				System.out.println("add apps:" + rst);

				ApplicationService as14 = new ApplicationService();
				as14.setAppId(3);
				as14.setPort(9002);
				as14.setServiceId(13);
				as14.setState(0);
				as14.setServerId(i);
				as14.setMaxClient(200);
				rst = cm.addAppService(as14);
				System.out.println("add apps:" + rst);
	 
				
				ApplicationService as15 = new ApplicationService();
				as15.setAppId(3);
				as15.setPort(9002);
				as15.setServiceId(14);
				as15.setState(0);
				as15.setServerId(i);
				as15.setMaxClient(200);
				rst = cm.addAppService(as15);
				System.out.println("add apps:" + rst);
				
				
				ApplicationService as16 = new ApplicationService();
				as16.setAppId(3);
				as16.setPort(9002);
				as16.setServiceId(15);
				as16.setState(0);
				as16.setServerId(i);
				as16.setMaxClient(200);
				rst = cm.addAppService(as16);
				System.out.println("add apps:" + rst);
				
				
				ApplicationService as17 = new ApplicationService();
				as17.setAppId(3);
				as17.setPort(9002);
				as17.setServiceId(16);
				as17.setState(0);
				as17.setServerId(i);
				as17.setMaxClient(200);
				rst = cm.addAppService(as17);
				System.out.println("add apps:" + rst);
				
				ApplicationService as18 = new ApplicationService();
				as18.setAppId(3);
				as18.setPort(9002);
				as18.setServiceId(17);
				as18.setState(0);
				as18.setServerId(i);
				as18.setMaxClient(200);
				rst = cm.addAppService(as18);
				System.out.println("add apps:" + rst);
				
				ApplicationService as19 = new ApplicationService();
				as19.setAppId(0);
				as19.setPort(9000);
				as19.setServiceId(18);
				as19.setState(0);
				as19.setServerId(i);
				as19.setMaxClient(200);
				rst = cm.addAppService(as19);
				System.out.println("add apps:" + rst);
 
			}

			List<ApplicationService> list = cm.getAppServices(0, 0);
			for (ApplicationService a : list) {
				System.out.println(a.getServerId() + "|" + a.getAppId() + "|" + a.getServiceId() + "|" + a.getPort());
			}
		}// else if (unit==4)
		{
			//AppServiceLink 表示APP应用跟service服务的链接,表示某一个APP需要  “链接”的服务集
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
					
		
					
					//彩铃逻辑层需要链接的服务
					AppServiceLink asl8 = new AppServiceLink();
					asl8.setServerId(i);
					asl8.setAppId(3);
					asl8.setLinkServerId(a);
					asl8.setLinkAppId(1);
					asl8.setLinkServiceId(3);
					
					//
					 rst = cm.addAppServiceLink(asl8);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl9 = new AppServiceLink();
					asl9.setServerId(i);
					asl9.setAppId(3);
					asl9.setLinkServerId(a);
					asl9.setLinkAppId(1);
					asl9.setLinkServiceId(4);
					rst = cm.addAppServiceLink(asl9);
					System.out.println("add appsl:" + rst);
					
					AppServiceLink asl10 = new AppServiceLink();
					asl10.setServerId(i);
					asl10.setAppId(3);
					asl10.setLinkServerId(a);
					asl10.setLinkAppId(1);
					asl10.setLinkServiceId(5);
					rst = cm.addAppServiceLink(asl10);
					System.out.println("add appsl:" + rst);
					
					AppServiceLink asl11 = new AppServiceLink();
					asl11.setServerId(i);
					asl11.setAppId(3);
					asl11.setLinkServerId(a);
					asl11.setLinkAppId(1);
					asl11.setLinkServiceId(6);
					rst = cm.addAppServiceLink(asl11);
					System.out.println("add appsl:" + rst);
					
					AppServiceLink asl12 = new AppServiceLink();
					asl12.setServerId(i);
					asl12.setAppId(3);
					asl12.setLinkServerId(a);
					asl12.setLinkAppId(1);
					asl12.setLinkServiceId(7);
					rst = cm.addAppServiceLink(asl12);
 					System.out.println("add appsl:" + rst);
					
 					AppServiceLink asl13 = new AppServiceLink();
 					asl13.setServerId(i);
 					asl13.setAppId(3);
 					asl13.setLinkServerId(a);
 					asl13.setLinkAppId(1);
 					asl13.setLinkServiceId(8);
					rst = cm.addAppServiceLink(asl13);
 					System.out.println("add appsl:" + rst);
 					
 					AppServiceLink asl14 = new AppServiceLink();
 					asl14.setServerId(i);
 					asl14.setAppId(3);
 					asl14.setLinkServerId(a);
 					asl14.setLinkAppId(1);
 					asl14.setLinkServiceId(9);
					rst = cm.addAppServiceLink(asl14);
 					System.out.println("add appsl:" + rst);
 					
 					AppServiceLink asl15 = new AppServiceLink();
 					asl15.setServerId(i);
 					asl15.setAppId(3);
 					asl15.setLinkServerId(a);
 					asl15.setLinkAppId(1);
 					asl15.setLinkServiceId(10);
					rst = cm.addAppServiceLink(asl15);
 					System.out.println("add appsl:" + rst);
					
 					
 					AppServiceLink asl16 = new AppServiceLink();
 					asl16.setServerId(i);
 					asl16.setAppId(3);
 					asl16.setLinkServerId(a);
 					asl16.setLinkAppId(1);
 					asl16.setLinkServiceId(11);
					rst = cm.addAppServiceLink(asl16);
 					System.out.println("add appsl:" + rst);
 					
 					
 					//-============================================================
 					AppServiceLink asl17 = new AppServiceLink();
 					asl17.setServerId(i);
 					asl17.setAppId(2);
 					asl17.setLinkServerId(a);
 					asl17.setLinkAppId(3);
 					asl17.setLinkServiceId(12);
					//
					rst = cm.addAppServiceLink(asl17);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl18 = new AppServiceLink();
					asl18.setServerId(i);
					asl18.setAppId(2);
					asl18.setLinkServerId(a);
					asl18.setLinkAppId(3);
					asl18.setLinkServiceId(13);
					//
					rst = cm.addAppServiceLink(asl18);
					System.out.println("add appsl:" + rst);

					AppServiceLink asl19 = new AppServiceLink();
					asl19.setServerId(i);
					asl19.setAppId(2);
					asl19.setLinkServerId(a);
					asl19.setLinkAppId(3);
					asl19.setLinkServiceId(14);
					//
					rst = cm.addAppServiceLink(asl19);
					System.out.println("add appsl:" + rst);
 					
					AppServiceLink asl20 = new AppServiceLink();
					asl20.setServerId(i);
					asl20.setAppId(2);
					asl20.setLinkServerId(a);
					asl20.setLinkAppId(3);
					asl20.setLinkServiceId(15);
					//
					rst = cm.addAppServiceLink(asl20);
					System.out.println("add appsl:" + rst);
					
					AppServiceLink asl21 = new AppServiceLink();
					asl21.setServerId(i);
					asl21.setAppId(2);
					asl21.setLinkServerId(a);
					asl21.setLinkAppId(3);
					asl21.setLinkServiceId(16);
					//
					rst = cm.addAppServiceLink(asl21);
					System.out.println("add appsl:" + rst);
					
					AppServiceLink asl22 = new AppServiceLink();
					asl22.setServerId(i);
					asl22.setAppId(2);
					asl22.setLinkServerId(a);
					asl22.setLinkAppId(3);
					asl22.setLinkServiceId(17);
					//
					rst = cm.addAppServiceLink(asl22);
					System.out.println("add appsl:" + rst);
					
					//补充新加会员逻辑层需要链接的服务
			         AppServiceLink asl23 = new AppServiceLink();
			         asl23.setServerId(i);
			         asl23.setAppId(0);
			         asl23.setLinkServerId(a);
			         asl23.setLinkAppId(1);
			         asl23.setLinkServiceId(7);
						//
					 rst = cm.addAppServiceLink(asl23);
					 System.out.println("add appsl:" + rst);

					 AppServiceLink asl24 = new AppServiceLink();
					 asl24.setServerId(i);
					 asl24.setAppId(0);
					 asl24.setLinkServerId(a);
					 asl24.setLinkAppId(1);
					 asl24.setLinkServiceId(8);
						//
					 rst = cm.addAppServiceLink(asl24);
					System.out.println("add appsl:" + rst);
						
					 AppServiceLink asl25 = new AppServiceLink();
					 asl25.setServerId(i);
					 asl25.setAppId(0);
					 asl25.setLinkServerId(a);
					 asl25.setLinkAppId(1);
					 asl25.setLinkServiceId(9);
						//
					 rst = cm.addAppServiceLink(asl25);
					 System.out.println("add appsl:" + rst);
					 
						AppServiceLink as26 = new AppServiceLink();
						as26.setServerId(i);
						as26.setAppId(2);
						as26.setLinkServerId(a);
						as26.setLinkAppId(0);
						as26.setLinkServiceId(18);
						//
						rst = cm.addAppServiceLink(as26);
						System.out.println("add appsl:" + rst);	
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
