package org.dmp.core.log;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.dmp.core.util.JsonBuilder;
import org.slf4j.Logger;

@SuppressWarnings("all")
public interface Log {
	public void info();

	public void error();

	public void debug();

	public void warrng();

	public Logger getLogger();

	public JsonBuilder getBuffer();

	public Log append(String name, String msg);

	public Log append(String name, String format, Date date);

	public Log append(String name, List list);

	public Log append(String name, Map map);

	public Log append(String name, Set set);

	public Log append(String name, Object[] array);

	public Log append(String name, Number num);

	public Log append(String name, Boolean is);

	public Log appendSQL(String sql);

	public Log append(String name, Object msg);

	public Log appendMessage(String message);

	public Log appendException(String message);

	public Log appendCause(String message);
	// public Log appendError(String message);
}
