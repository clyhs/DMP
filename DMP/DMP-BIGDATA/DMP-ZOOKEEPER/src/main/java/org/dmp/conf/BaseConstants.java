package org.dmp.conf;


public class BaseConstants {

	public static final Configuration CONFIG=Configuration.getIntance();
	public static final String CORE_SITE="core-site.xml";
	public static final String SERVER_NAME="base.server.name";
	public static final String APP_NAME="base.app.name";
	public static final String ZOOKEEPR_QUORUM="base.zookeeper.quorum";
	public static final String APP_SERVER_NAME=CONFIG.getString(SERVER_NAME, "");
	public static final String APP_APP_NAME=CONFIG.getString(APP_NAME,"");
}
