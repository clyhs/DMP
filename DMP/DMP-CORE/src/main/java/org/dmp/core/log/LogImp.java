package org.dmp.core.log;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dmp.core.util.DateUtil;
import org.dmp.core.util.JsonBuilder;
import org.dmp.core.util.LogTags;
import org.slf4j.Logger;

/*****
 * 
 * @ClassName: MyDBAppender
 * @Description: 添加日志到数据库
 * @author zhoushubin@unioncast.com
 * @date 2013-3-14 上午11:37:27
 * 
 */
@SuppressWarnings("all")
public class LogImp implements Log {
	private Logger logger = null;

	private volatile JsonBuilder jb = null;

	public LogImp(Logger logger) {
		this.logger = logger;
		this.jb = new JsonBuilder();
	}

	private void clear() {
		jb.clear();
	}

	public JsonBuilder getBuffer() {
		return jb;
	}

	public Log append(String name, String msg) {
		getBuffer().append(name, msg);
		return this;
	}

	public Log append(String name, String format, Date date) {
		getBuffer().append(name, DateUtil.dateFormat(format, date));
		return this;
	}

	public Log append(String name, List list) {
		getBuffer().append(name, list);
		return this;
	}

	public Log append(String name, Map map) {
		getBuffer().append(name, map);
		return this;
	}

	public Log append(String name, Set set) {
		getBuffer().append(name, set);
		return this;
	}

	public Log append(String name, Object[] array) {
		getBuffer().append(name, array);
		return this;
	}

	public Log append(String name, Number num) {
		getBuffer().append(name, num);
		return this;
	}

	public Log append(String name, Boolean is) {
		getBuffer().append(name, is);
		return this;
	}

	public Log appendSQL(String sql) {
		getBuffer().append("SQL", sql);
		return this;
	}

	public Log append(String name, Object msg) {
		getBuffer().append(name, msg);
		return this;
	}

	public void info() {
		logger.info(jb.toString());
		clear();
	}

	public void error() {
		logger.error(jb.toString());
		clear();
	}

	public void debug() {
		logger.debug(jb.toString());
		clear();
	}

	public void warrng() {
		logger.warn(jb.toString());
		clear();
	}

	public Logger getLogger() {
		return logger;
	}

	public Log appendMessage(String message) {
		getBuffer().append(LogTags.LOGDESC, message);
		return this;
	}

	public Log appendException(String message) {
		getBuffer().append(LogTags.EXCEPTION, message);
		return this;
	}

	public Log appendCause(String message) {
		getBuffer().append(LogTags.CAUSE, message);
		return this;
	}
}
