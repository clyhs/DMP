package org.dmp.core.util;

import org.apache.log4j.Logger;

public final class SystemUtils {

	private static final Logger logger = Logger.getLogger(SystemUtils.class);
	/**
	 * 获取程序运行的路径，如果是JAR 则是返回JAR目录 ,如果是从class 则返回 class path;
	 * 
	 * @return
	 */
	public static final String getRealPath() {
		String path = SystemUtils.class.getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		if (path != null && path.toLowerCase().endsWith(".jar")) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}
}
