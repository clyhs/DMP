package org.dmp.core.log;

import org.slf4j.LoggerFactory;

/***
 * 
 * @ClassName: MyLogFactory
 * @Description: 获取日志操作工厂
 * @author zhoushubin@unioncast.com
 * @date 2013-3-14 下午5:01:15
 * 
 */
public class LogFactory {
	/***
	 * 
	 * @Title: Instance
	 * @Description: 获取日志操作的实例
	 * @param clazz
	 * @return
	 * @return MyLogger 返回类型
	 * @throws
	 */
	public static Log Instance(Class<?> clazz) {
		return new LogImp(LoggerFactory.getLogger(clazz));
	}

	public static Log Instance(String name) {
		return new LogImp(LoggerFactory.getLogger(name));
	}
}
