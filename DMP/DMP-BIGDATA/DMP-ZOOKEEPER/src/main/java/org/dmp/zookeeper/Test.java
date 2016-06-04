package org.dmp.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.dmp.clusters.ClustersManagerImpl;
import org.dmp.zookeeper.exception.ZkException;


public class Test {
	static ZKClient client;
	
	public static void main(String []args) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException{
//		ClusterManagerImpl impl=new ClusterManagerImpl("192.168.1.56:2181,192.168.1.57:2181,192.168.1.58:2181");
//		
		//org.w3c.dom.
		ZKClient client=ZKClient.getInstance();
//		impl.addGroup("group1");
//		impl.addNode("group1", "node1");
//		impl.addOrUpdateConfig("group1", "node1", "serverIp", "192.168.1.44".getBytes());
//		
//		byte [] buffer=impl.getConfig("group1", "node1", "serverIp");
//		System.out.println(new String (buffer));
//		impl.addGroup("testgroup");
//		impl.addNode("testgroup", "smsnode");
//		Service s=new Service();
//		s.setIp("192.168.1.100");
//		s.setName("sms");
//		s.setPort(9989);
//		s.setState(0);
//		impl.addProvidedService("testgroup", "smsnode", s);
//		
//		impl.addNode("testgroup", "mgpnode");
//		Service s1=new Service();
//		s1.setIp("192.168.1.101");
//		s1.setName("mgp");
//		s1.setPort(9989);
//		s1.setState(0);
//		impl.addProvidedService("testgroup", "mgpnode", s);
//		
//		impl.addNodeLinked("testgroup", "mgpnode", "smsnode", "sms");
//		
	 //client=new ZKClient ("192.168.1.56:2181,192.168.1.57:2181,192.168.1.58:2181");
////		Runtime.getRuntime().addShutdownHook(new com.core.zookeeper.Test.CloseThread());
//		client.createEphemeral(ClustersDef.getServiceStatePath("testgroup", "smsnode", "sms"));
////		client.writeData(ClustersDef.getServiceStatePath("testgroup", "smsnode", "sms"), "a".getBytes(), -1);
////		CountDownLatch cdl=new CountDownLatch(29);
////		cdl.await();
//		//client.close();
//
//	client.createPersistent(ClustersDef.getConfigPath("testgroup", "mgpnode", "mgptimeout12"));
//	client.writeData(ClustersDef.getConfigPath("testgroup", "mgpnode", "mgptimeout12"), "testvalue122111".getBytes(), -1);
////		System.out.println(ClustersDef.LINKED_PATTERN);
////		System.out.println(ClustersDef.NODE_LINKED_PATTERN);
////		System.out.println(ClustersDef.getNodeLinkedPath("group", "node"));
//
//	client.createPersistent(ClustersDef.getConfigPath("testgroup", "mgpnode", "mgptimeout"));
//	client.writeData(ClustersDef.getConfigPath("testgroup", "mgpnode", "mgptimeout"), "testvaluefdsfsd".getBytes(), -1);
//Sequential
		String path="/aaa/bbbb/";

		String parentDir = path.substring(0, path.lastIndexOf('/'));
		System.out.println(parentDir);
	 String str="";
	 client.deleteRecursive("/uc");
//	 
//	 client.createPersistent("/unioncast/cluster/service");
//	 client.createPersistent("/unioncast/cluster/server");
			 client.createPersistent("/mup/cluster/service");
			 client.createPersistent("/mup/cluster/server");
			 //client.createEphemeral("/test2/state");
	 System.out.println("create dir:"+str);

	CountDownLatch cdl=new CountDownLatch(29);
	cdl.await();
	}
}
