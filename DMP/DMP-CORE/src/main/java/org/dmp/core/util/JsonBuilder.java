package org.dmp.core.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 
 * @author clyhs
 *
 */
public class JsonBuilder {

	static Logger logger = LoggerFactory.getLogger(JsonBuilder.class);

	public JsonBuilder() {
		jsonData = new StringBuilder();
		jsonData.append("{");
	}

	public void clear() {
		jsonData.delete(0, 100000);
		jsonData.append("{");
	}

	private StringBuilder jsonData;
	
	public JsonBuilder append(String key,Object obj){
		jsonData.append("\"").append(key).append("\":");
		if (obj == null) {
			jsonData.append("\"\"");
		} else if (obj instanceof Number) {
			jsonData.append(obj);
		} else if (obj instanceof Boolean) {
			jsonData.append(obj);
		} else if (obj instanceof String) {
			jsonData.append("\"").append(obj).append("\"");
		} else if (obj instanceof Object[]) {
			jsonData.append(JsonUtil.arrayToJson((Object[]) obj));
		} else if (obj instanceof List) {
			jsonData.append(JsonUtil.listToJson((List<?>) obj));
		} else if (obj instanceof Map) {
			jsonData.append(JsonUtil.mapToJson((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			jsonData.append(JsonUtil.setToJson((Set<?>) obj));
		}else if(obj instanceof Character) {
			jsonData.append("\"").append(obj).append("\"");
		}
		else {
			jsonData.append(JsonUtil.beanToJson(obj));
		}
		jsonData.append(",");
		return this;
	}
	
	public JsonBuilder appendException(String message) {
		return append(LogTags.EXCEPTION, message);
	}

	public JsonBuilder appendINFO(String message) {
		return append(LogTags.EXCEPTION, message);
	}

	public JsonBuilder appendDEBUG(String message) {
		return append(LogTags.EXCEPTION, message);
	}
	
	public JsonBuilder appendLogDesc(String message) {
		return append(LogTags.LOGDESC, message);
	}
	
	public JsonBuilder appendStatus(Object message) {
		return append(LogTags.STATUS, message);
	}

	public JsonBuilder appendError(String message) {
		return append(LogTags.ERROR, message);
	}

	public String toString() {
		jsonData.deleteCharAt(jsonData.lastIndexOf(","));
		return jsonData.append("}").toString();
	}
}
