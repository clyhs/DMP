package org.dmp.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public final class SystemUtils {

	/**
	 * 获取程序运行的路径，如果是JAR 则是返回JAR目录 ,如果是从class 则返回 class path;
	 * 
	 * @return
	 */
	public static final String getRealPath() {
		String path = SystemUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if (path != null && path.toLowerCase().endsWith(".jar")) {
			path = path.substring(0, path.lastIndexOf("/") + 1);
		}
		return path;
	}

	public static final boolean addShutdownHook(Thread task) {
		try {
			Runtime.getRuntime().addShutdownHook(task);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final String getMACAddress(InetAddress ia) throws Exception {
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		return HexUtil.encodeHexStr(mac).toUpperCase();
	}

	public static final String getMACAddress() throws Exception {
		return getMACAddress(InetAddress.getLocalHost());
	}

	public static final String getLocalIpAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "127.0.0.1";
		}
	}
}
