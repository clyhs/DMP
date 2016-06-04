package org.dmp.conf;

public class BaseConfigKey {
	//BASE
	public static final String CORE_SITE="core-site.xml";
	public static final String SERVER_NAME="base.server.name";
	public static final String APP_NAME="base.app.name";
	public static final String ZOOKEEPR_QUORUM="base.zookeeper.quorum";
	
	public static final String SOCKET_IDLE_TIME="socket.idle.time";
	public static final String SOCKET_RECONNECT_TIME="socket.reconnect.time";
	
	public static final String RPC_WAIT_TIME_OUT="rpc.wait.timeout";
	//ISMP
	public static final String ISMP_DEVICE_ID="ismp.device.id";
	public static final String ISMP_CHANNEL_ID="ismp.channel.id";
	public static final String ISMP_PASSWORD="ismp.password";
	public static final String ISMP_PACKET_URL="ismp.packet.url";
	public static final String ISMP_UNPACKET_URL="ismp.unpacket.url";
	
	public static final String ISMP_WSDL_URL_V1="ismp.packet.url.v1";
	public static final String ISMP_UNPACKET_URL_V1="ismp.unpacket.url.v1";
	public static final String ISMP_PACKET_ID_0="ismp.packet.id.0";
	public static final String ISMP_PACKET_ID_1="ismp.packet.id.1";
	public static final String ISMP_PACKET_ID_2="ismp.packet.id.2";
	public static final String ISMP_PACKET_ID_3="ismp.packet.id.3";
	
	
	public static final String MGP_SERVER_IP="mgp.server.ip";
	public static final String MGP_SERVER_PORT="mgp.server.port";
	public static final String MGP_USER_NAME="mgp.user.name";
	public static final String MGP_PASSWORD="mgp.password";
	public static final String MGP_SRC_ADDR="mgp.src.addr";
	public static final String MGP_RECV_RSP_TIME_OUT="mgp.recv.rsp.time.tou";
	
	 public static final String UTM_SRC_ID="utm.src.id";
	 public static final String UTM_DST_ID="utm.dst.id";
	 
	 public static final String SMS_SERVER_IP="sms.server.ip";
	 public static final String SMS_SERVER_PORT="sms.server.port";
	 public static final String SMS_SERVER_ID="sms.server.id";
	 public static final String SMS_CLIENT_ID="sms.client.id";
	 public static final String SMS_SK_THREAD_COUNT="sms.sk.thread.count";
	 public static final String SMS_GW_NAME="sms.gw.name";
	 
	 public static final String ISAG_SPID="isag.spid";
	 public static final String ISAG_PWD="isag.pwd";
	 public static final String ISAG_PRODUCT_ID="isag.product.id";
	 public static final String ISAG_MMS_SREF_ENDPOINT="isag.mms.simplereference.endpoint";
	 public static final String ISAG_MMS_SREF_INTERFACENAME="isag.mms.simplereference.interfacename";
	 public static final String ISAG_WAPUSH_SREF_ENDPOINT="isag.wapush.simplereference.endpoint";
	 public static final String ISAG_WAPUSH_SREF_INTERFACENAME="isag.wapush.simplereference.interfacename";
	 public static final String ISAG_MMS_CHARGING_CODE="isag.mms.charging.code";
	 public static final String ISAG_MMS_CHARGING_AMOUNT="isag.mms.charging.amount";
	 public static final String ISAG_MMS_CHARGING_DESC="isag.mms.charging.desc";
	 public static final String ISAG_WAPUSH_CHARGING_CODE="isag.wapush.charging.code";
	 public static final String ISAG_WAPUSH_CHARGING_AMOUNT="isag.wapush.charging.amount";
	 public static final String ISAG_WAPUSH_CHARGING_DESC="isag.wapush.charging.desc";
	 public static final String ISAG_MMS_SERVICE_URL="isag.mms.service.url";
	 
	 public static final String ISAG_WAPUSH_SERVICE_URL="isag.wapush.service.url";
	 public static final String ISAG_SMS_SERVICE_URL="isag.sms.service.url";
	 public static final String ISAG_SMS_CHARGING_CODE="isag.sms.charging.code";
	 public static final String ISAG_SMS_CHARGING_AMOUNT="isag.sms.charging.amount";
	 public static final String ISAG_SMS_CHARGING_DESC="isag.sms.charging.desc";
	 public static final String ISAG_SMS_SREF_ENDPOINT="isag.sms.simplereference.endpoint";
	 public static final String ISAG_SMS_SREF_INTERFACENAME="isag.sms.simplereference.interfacename";
	 
	 public static final String ISAG_SMS_SPID="isag.sms.spid";
	 public static final String ISAG_SMS_PWD="isag.sms.pwd";
	 public static final String ISAG_SMS_PID="isag.sms.product.id";
	 
	 public static final String ISAG_MMS_SPID="isag.mms.spid";
	 public static final String ISAG_MMS_PWD="isag.mms.pwd";
	 public static final String ISAG_MMS_PID="isag.mms.product.id";
	 
	 public static final String ISAG_WAPUSH_SPID="isag.wapush.spid";
	 public static final String ISAG_WAPUSH_PWD="isag.wapush.pwd";
	 public static final String ISAG_WAPUSH_PID="isag.wapush.product.id";
	 
	 
	 public static final String  FJDRP_SERVICE_PATH="fjdrp.service.path";//福建分销平台
	 public static final String  SCMEMBER_SERVICE_PATH="scmsag.service.path";//四川
	 public static final String  SCMEMBER_SYNCURL="scmember.sync.url";//四川会员数据同步URL
	 public static final String  SCMEMBER_SYNCTIMEOUT="scmember.sync.timeout";//四川会员数据同步超时时间

	 
	 public static final String  MSAGRES_SERVICE_PATH="msagres.service.path";//资源MSAG地址
	 public static final String  BJ_SPECIAl_PROVINCEID="bjmember.province.idlist";//资源MSAG地址
	 public static final String  RINGGROUP_PROVINCEID="RingGroup.province.idlist";//默认铃音组省份集合
	 public static final String  RINGBOX_PROVINCEID="RingBox.province.idlist"; //默认铃音盒省份ID集合
	 public static final String  RINGGROUP_DEFAULTNAME="RingGroup.default.name";//白金会员默认铃音组名称
	 
	 public static final String  ETHNICLANGUAGEMBR_BOXID="ethnicLanguageMbr.default.ringboxid";//民语会员默认铃音盒ID

	 public static final String  CrbtQueryRingDefaultNum="crbt.queryring.defaultnum";//民语会员默认铃音盒ID

	 public static final String  CrbtDiy_SelF_Spid="crbtDiy.self.spid";//自营模式spid. 
 	
	 public static final String  CrbtDiy_Team_Spid="crbtDiy.team.spid";//合作模式spid.
	 
	 public static final String  CrbtDiy_Blog_Spid="crbtDiy.blog.spid";//博客模式spid.

	 public static final String  CrbtDiy_Free_Spid="crbtDiy.free.spid";//运行免费模式spid.

	 public static final String  CrbtDiy_SelFISMP_ProductId="crbtDiy.selfismp.productid";//自营模式计费的ISMP产品ID. 

	 public static final String  CrbtDiy_TeamFee_MusicBoxid="crbtDiy.teamfee.musicboxid";//合作模式计费的音乐盒ID

	 public static final String  CrbtDiy_Blog_IsCharge="crbtDiy.blog.ischarge";//博客模式,是否收费标志,N为免费,Y为收费.

	 
}
