package org.dmp.clusters;

public interface IEvtListenner {
	public final static int EVT_RESTART		 = 0x000000001;
	public final static int EVT_STOP		 = 0x000000002;
	public final static int EVT_START		 = 0x000000003;
	public final static int EVT_CHANGE_PORT	 = 0x000000004;
	public final static int EVT_CHANGE_IP 	 = 0x000000005;
	public final static int EVT_CHANGE_IPPORT= 0x000000006;
	

	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	// public final static int CMD_RESTART=0x000000001;
	public void onEvt(int cmd);
}
