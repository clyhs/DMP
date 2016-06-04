package org.dmp.clusters;


import org.dmp.conf.Configuration;

public class ClustersDef {
	
	public static final String CLUSTER_DIR="base.zk.cluster.dir";
	public static final String CORE_SITE="core-site.xml";
	public static final String SERVER_NAME_P="base.server.name";
	public static final String APP_NAME_P="base.app.name";
	public static final String ZOOKEEPR_QUORUM_P="base.zookeeper.quorum";
	
	private final static Configuration config=Configuration.getIntance();
	public static final String SERVER_NAME=config.getString(SERVER_NAME_P, "");
	public static final String APP_NAME=config.getString(APP_NAME_P, "");
	public static final String ZOOKEEPR_QUORUM=config.getString(ZOOKEEPR_QUORUM_P, "");

	public static final String ROOT_DIR=config.getString(CLUSTER_DIR, "/uc/cluster");
	public static final String SERVICE_DIR=ROOT_DIR+"/services";
	public static final String NODE_DIR=ROOT_DIR+"/nodes";
	public static final String CONFIG_DIR="config";
	
	
	public static final String ROOT_SERVER_DIR=ROOT_DIR+"/server";
	public static final String ROOT_SERVICE_DIR=ROOT_DIR+"/service";
	
	/**
	 * 
	 */
	public static final String ROOT_APP_SERVICE_DIR=ROOT_SERVER_DIR+"/%010d/%010d/service";
	public static final String APP_SERVICE_DIR=ROOT_APP_SERVICE_DIR+"/%010d";
	public static final String APP_SERVICE_STATE_DIR=APP_SERVICE_DIR+"/state";
	public static final String ROOT_APP_SERVICE_CONNECTION_DIR=APP_SERVICE_DIR+"/connection";
	public static final String APP_SERVICE_CONNECTION_DIR=ROOT_APP_SERVICE_CONNECTION_DIR+"/%di%di%d";
	
	public static final String ROOT_APP_CONF_DIR=ROOT_SERVER_DIR+"/%010d/%010d/config";
	public static final String APP_CONF_DIR=ROOT_SERVER_DIR+"/%010d/%010d/config/%s";
	
	public static final String ROOT_APP_SERVICE_LINK_DIR=ROOT_SERVER_DIR+"/%010d/%010d/links";
	public static final String APP_SERVICE_LINK_DIR=ROOT_SERVER_DIR+"/%010d/%010d/links/%010d/%010d";
	
	
	public static final String ROOT_DIR_PATTERN=ROOT_DIR;
	public static final String ROOT_SERVER_PATTERN=ROOT_SERVER_DIR;
	public static final String SERVER_PATTERN=ROOT_SERVER_DIR+"/[0-9]{10}";
	public static final String APP_PATTERN=SERVER_PATTERN+"/[0-9]{10}";
	public static final String APP_SERVICE_PATTERN=APP_PATTERN+"/service/[0-9]{10}";
	public static final String APP_SERVICE_STATE_PATTERN=APP_SERVICE_PATTERN+"/state";
	public static final String APP_SERVICE_CONNECTION_PATTERN=APP_SERVICE_PATTERN+"/connection";
	public static final String CONFIG_PATTERN=APP_SERVICE_PATTERN+"/connection";
	
	public static final String ID_PATTERN="[0-9]{10}";
	public static final int ERR_INVALID_ID=0x30000008;
	public static final int ERR_INVALID_PARAM=0x3000000C;
	
	public static final int ERR_NODE_NOT_EXIST=0x30000001;
	public static final int ERR_NODE_EXIST=0x30000002;
	public static final int ERR_SUCESS=0x00000000;
	public static final int ERR_ADDNODE_EXCEPTION=0x30000003;
	
	public static final int ERR_SERVER_EXIST=0x30000005;
	public static final int ERR_ADD_SERVER_FAILD=0x30000006;
	public static final int ERR_SERVER_NOT_EXIST=0x30000007;
	
	
	public static final int ERR_APP_EXIST=0x30000009;
	public static final int ERR_APP_NOT_EXIST=0x3000000A;
	public static final int ERR_ADD_APP_FAILD=0x3000000B;
	
	public static final int ERR_SERVICE_EXIST=0x30000004;
	public static final int ERR_SERVICE_NOT_EXIST=0x3000000A;
	public static final int ERR_ADD_SERVICE_FAILD=0x3000000B;
	
	public static final int ERR_APP_SERVICE_EXIST=0x3000000C;
	public static final int ERR_APP_SERVICE_NOT_EXIST=0x3000000D;
	public static final int ERR_ADD_APP_SERVICE_FAILD=0x3000000E;
	
	public static final int ERR_APP_SERVICE_LINK_EXIST=0x3000000F;
	public static final int ERR_APP_SERVICE_LINK_NOT_EXIST=0x30000010;
	public static final int DIR_LEN=10;
	public static final int INVALID_ID=-9999;
	
	
}
