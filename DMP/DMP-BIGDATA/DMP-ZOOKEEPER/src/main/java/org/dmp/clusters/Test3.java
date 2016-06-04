package org.dmp.clusters;

public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClustersManagerImpl cm = new ClustersManagerImpl();
		Server s = cm.findServer("s58");
		
		System.out.println(s.getId());

	}

}
