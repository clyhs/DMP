package org.dmp.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.dmp.util.StrUtil;
import org.dmp.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Configuration {

	// public static final Log log=LogFactory.getLog(Configuration.class)
	public static final String CONFIG_DIR = "conf/";

	public static Logger log = LoggerFactory.getLogger(Configuration.class);

	private Configuration() {
	}

	public static Configuration instance = null;

	public static synchronized Configuration getIntance() {
		if (instance == null) {
			instance = new Configuration();
			// instance.addResource("config.properties");
			instance.addResource("config.xml");

		}
		return instance;
	}

	public static String getConfigDir() {
		String realPath = SystemUtils.getRealPath();
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath += "/" + CONFIG_DIR;
		return realPath;
	}

	public static String getConfigFile(String fileName) {
		String realPath = SystemUtils.getRealPath();
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath += "/" + CONFIG_DIR + fileName;
		log.info("config file path:" + realPath);
		return realPath;
	}

	public static String getClassPathFile(String fileName) {
		if (null == fileName) {
			return null;
		}
		if (!fileName.startsWith("/")) {
			fileName = "/" + fileName;
		}
		URL url = Configuration.class.getResource(fileName);
		if (url != null) {
			return url.getPath();
		}
		return null;
	}

	public void addResource(String fileName) {

		String classPathFile = getClassPathFile(fileName);
		if (null != classPathFile && !"".equals(classPathFile)) {
			File configFile = new File(classPathFile);
			addResource(configFile);
		}
		File file = new File(getConfigFile(fileName));
		addResource(file);
	}

	private void addXMLResource(File file) {

		try {
			lock.lock();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbd = dbf.newDocumentBuilder();
			
			InputStream inputStream= new FileInputStream(file);
			
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			Document doc = dbd.parse(is);
			Element el = doc.getDocumentElement();
			NodeList nodeList = el.getChildNodes();
			XPathFactory f = XPathFactory.newInstance();
			XPath path = f.newXPath();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Node nameNode = (Node) path.evaluate("name", node, XPathConstants.NODE);
					Node valNode = (Node) path.evaluate("value", node, XPathConstants.NODE);
					Node typeNode = (Node) path.evaluate("type", node, XPathConstants.NODE);
					if (null != nameNode && null != valNode) {
						String name = nameNode.getTextContent();
						String value = valNode.getTextContent();
						String type = "string";
						try {
							type = typeNode.getTextContent();
						} catch (Exception e) {
							type = "string";
						}
						Config<?> config = getConfig(name, value, type);
						configMap.put(config.getKey(), config);
						log.info("config=key:{}|value:{}", name.toLowerCase(), value);
					}
				}

			}
		} catch (Exception e) {
			log.error("add xml config file failed fileName is {}", file.getAbsolutePath());
			// logger.appendMessage("add xml config file failed").appendException(e.getMessage()).append("fileName",
			// file.getName()).warrng();
		} finally {
			lock.unlock();
		}
	}

	private Object getConfigValue(String type, String value) {
		if (StrUtil.isEmpty(type) || "string".equals(type.toLowerCase())) {

			return value;
		} else if ("int".equals(type.toLowerCase())) {
			return Integer.valueOf(value);
		} else if ("long".equals(type.toLowerCase())) {

			return Long.valueOf(value);
		} else if ("float".equals(type.toLowerCase())) {

			return Float.valueOf(value);
		} else if ("double".equals(type.toLowerCase())) {

			return Double.valueOf(value);
		}
		return null;

	}

	private Config<?> getConfig(String name, String value, String type) {
		if (StrUtil.isEmpty(type) || "string".equals(type.toLowerCase())) {
			Config<String> config = new Config<String>(name, value);
			config.setType(type);
			return config;
		} else if ("int".equals(type.toLowerCase())) {
			Config<Integer> config = new Config<Integer>(name, Integer.valueOf(value));
			config.setType(type);
			return config;
		} else if ("long".equals(type.toLowerCase())) {
			Config<Long> config = new Config<Long>(name, Long.valueOf(value));
			config.setType(type);
			return config;
		} else if ("float".equals(type.toLowerCase())) {
			Config<Float> config = new Config<Float>(name, Float.valueOf(value));
			config.setType(type);
			return config;
		} else if ("double".equals(type.toLowerCase())) {
			Config<Double> config = new Config<Double>(name, Double.valueOf(value));
			config.setType(type);
			return config;
		}
		return null;
	}

	public <T> Config<T> getConfig(final String key, T defalutValue) {
		Config<T> config = null;
		try {
			config = (Config<T>) configMap.get(key);
		} catch (Exception e) {

		}
		if (config == null) {
			config = new Config<T>(key, defalutValue);
			configMap.put(config.getKey(), config);
		}
		return config;
	}

	private void addProperResouce(File file) {

		try {
			lock.lock();
			Properties proper = new Properties();
			proper.load(new FileInputStream(file));
			Iterator<String> it = proper.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				// configMap.put(key.toLowerCase(), proper.getProperty(key));
				log.info("config=key:{}|value:{}", key.toLowerCase(), proper.getProperty(key));
			}

		} catch (Exception e) {
			log.error("add properties config file failed fileName is {}", file.getName());
		} finally {
			lock.unlock();
		}
	}

	public void addResource(File file) {
		if (file.getName().toLowerCase().trim().endsWith(".xml")) {
			addXMLResource(file);
		} else {
			addProperResouce(file);
		}
	}

	public void addPropertie(String key, String type, String value) {
		try {
			Config c = configMap.get(key);
			if (c == null) {
				c = getConfig(key, value, type);
				configMap.put(key, c);
			} else {
				c.setValue(getConfigValue(type, value));
			}
		} catch (Exception e) {
			log.error(String.format("add propertie error:%s|%s|%s",key,type,value),e);
		}

	}

	public String getString(String path, String defaultValue) {

		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;

		}
		if (val.getValue() instanceof String) {
			return (String) val.getValue();
		}
		return String.valueOf(val.getValue());
	}

	public Integer getInteger(String path, int defaultValue) {
		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;
		}
		try {
			if (val.getValue() instanceof Integer) {
				return (Integer) val.getValue();
			}
			return Integer.parseInt(val.getValue().toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Double getDouble(String path, double defaultValue) {
		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;
		}
		try {
			if (val.getValue() instanceof Double) {
				return (Double) val.getValue();
			}
			return Double.valueOf(val.getValue().toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Float getFloat(String path, float defaultValue) {
		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;
		}
		try {
			if (val.getValue() instanceof Float) {
				return (Float) val.getValue();
			}
			return Float.parseFloat(val.getValue().toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Long getLong(String path, long defaultValue) {
		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;
		}
		try {
			if (val.getValue() instanceof Long) {
				return (Long) val.getValue();
			}
			return Long.parseLong(val.getValue().toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String path, boolean defaultValue) {
		Config val = configMap.get(path.toLowerCase());
		if (null == val) {
			return defaultValue;
		}
		try {
			if (val.getValue() instanceof Boolean) {
				return (Boolean) val.getValue();
			}
			return Boolean.parseBoolean(val.getValue().toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Collection<Config<?>> getConfigs() {
		return configMap.values();
	}

	private final Map<String, Config<?>> configMap = new ConcurrentHashMap<String, Config<?>>();

	private final ReentrantLock lock = new ReentrantLock();

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = configMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			sb.append(key).append("=").append(configMap.get(key)).append("\n");
		}
		return sb.toString();
	}
}
