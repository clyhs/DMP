package org.dmp.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带缓存的读取配置文件工具类， PropertiesUtil pu = new PropertiesUtil(); pu.
 * load("D:/test.properties") ; pu.getValueByKey("key");
 * pu.setValueByKey("newkey", "allvalue", "测试工具类添加的KEY-VALUE组合");
 * 
 */
@SuppressWarnings("all")
public class PropertiesUtil {
	Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private LinkedHashMap fields;// 该文件字段
	private Properties properties = new Properties();
	private InputStream is = null;
	private File file = null;
	private Boolean isAllReaded = Boolean.FALSE;// 是否已经读取读取全部
	private FileOutputStream fos = null;

	public PropertiesUtil() {
		this.properties = new Properties();
	}

	public PropertiesUtil(Properties properties) {
		this.properties = properties;
	}

	/***
	 * 
	 * @Title: 加载文件load(String fileName)
	 * @Description: PropertiesUtil pu = new PropertiesUtil() pu.
	 *               load("D:/test.properties") ;
	 * @param @param fileName 配置文件的路径
	 * @return Properties 返回类型
	 * @throws
	 */
	public Properties load(String fileName) {
		file = new File(fileName);
		if (!file.exists()) {
			logger.error(new JsonBuilder()
					.appendException("file  is not exists")
					.append("file path", fileName).toString());
		}
		if (file.isDirectory()) {
			logger.error(new JsonBuilder()
					.appendException("file  is Directory")
					.append("file path", fileName).toString());
		}
		return load(this.file);
	}

	/***
	 * 
	 * @Title: load
	 * @Description: PropertiesUtil pu = new PropertiesUtil() pu. load(new
	 *               File("D:/test.properties")) ;
	 * @param @param file
	 * @return Properties 返回类型
	 * @throws
	 */
	public Properties load(File file) {
		if (!(file.exists() && file.isFile())) {
			logger.error(new JsonBuilder().appendException(
					" file  is not exists").toString());
		}
		FileInputStream fip = null;
		try {
			fip = new FileInputStream(file);

			this.is = new BufferedInputStream(fip);
			return load(this.is);
		} catch (FileNotFoundException e) {
			logger.error(new JsonBuilder().appendException("load error")
					.appendLogDesc(e.getMessage()).toString());
			if (fip != null) {
				try {
					fip.close();
				} catch (IOException e1) {
					logger.error(new JsonBuilder()
							.appendException("load error")
							.appendLogDesc(e1.getMessage()).toString());
				}
			}
		}
		return null;
	}

	/***
	 * 
	 * @Title: load
	 * @Description: PropertiesUtil pu = new PropertiesUtil() pu. load(new
	 *               FileInputStream(new File("D:/test.properties"))) ;
	 * @param @param is
	 * @param @return 设定文件
	 * @return Properties 返回类型
	 * @throws
	 */
	public Properties load(InputStream is) {
		try {
			this.properties.load(is);
			if (this.fields == null) {
				this.fields = new LinkedHashMap();
			} else {
				this.fields.clear();
			}
		} catch (IOException e) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ee) {
					logger.error(new JsonBuilder()
							.appendException("load error")
							.appendLogDesc(e.getMessage()).toString());
				}
			}
			logger.error(new JsonBuilder().appendException("load error")
					.appendLogDesc(e.getMessage()).toString());
		}
		return properties;
	}

	/***
	 * 根据类路径加载配置文件
	 * 
	 * @Title: loadAsClass
	 * @Description: PropertiesUtil pu = new PropertiesUtil() pu.
	 *               load(test.properties,Test.getClass()) ;
	 * @param @param file
	 * @param @param clazz
	 * @param @return 设定文件
	 * @return Properties 返回类型
	 * @throws
	 */
	public Properties loadAsClass(String file, Class clazz) {
		return load(clazz.getResourceAsStream(file));
	}

	/****
	 * 
	 * @Title: getAllProperties
	 * @Description: 获取所有配置文件
	 * @param @return 设定文件
	 * @return Map <key,value> 返回类型
	 * @throws
	 */
	public Map getAllProperties() {
		if (this.properties == null) {
			logger.error(new JsonBuilder().appendException(
					"properties is null!Please  loading files first!")
					.toString());
			return null;
		}
		if (this.fields == null) {
			this.fields = new LinkedHashMap();

		}
		Set set = this.properties.keySet();
		for (Object key : set) {

			this.fields.put((String) key,
					this.properties.getProperty((String) key));
		}
		if (logger.isDebugEnabled()) {

			logger.debug(new JsonBuilder()
					.append(LogTags.DEBUG, "load properties")
					.append("properties:", properties).toString());
		}
		this.isAllReaded = Boolean.TRUE;
		return this.fields;
	}

	/****
	 * 
	 * @Title: getValueByKey
	 * @Description: read value by key ,if you knew zhe key!
	 * @param @param key
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getValueByKey(String key) {
		if (this.properties == null) {
			logger.error(new JsonBuilder().appendException(
					"properties is null!Please loading files first!")
					.toString());
		}
		if (this.fields == null || this.fields.size() == 0) {
			getAllProperties();
		}
		return (String) (this.fields.get(key) == null ? this.properties
				.getProperty(key) : this.fields.get(key));
	}

	/***
	 * 
	 * @Title: setValueByKey
	 * @Description: 保存一条配置信息
	 * @param @param Key 需要保存的KEY
	 * @param @param value 需要保存的Vlue
	 * @param @param note 设定文件 需要对这条KEY 添加什么样的注释
	 * @return void 返回类型
	 * @throws
	 */
	public void setValueByKey(String Key, String value, String note) {
		if (this.file == null) {
			logger.error(new JsonBuilder().appendException(
					"file is null!Please  loading files first!").toString());
			return;
		}
		if (this.properties == null) {
			logger.error(new JsonBuilder().appendException(
					"properties is null!Please  loading files first!")
					.toString());
			return;
		}
		if (Key == null) {
			logger.error(new JsonBuilder().appendException(
					String.format("properties key is null! args:%s is null",
							Key)).toString());
			return;
		}
		this.fields.put(Key, value);
		try {
			fos = new FileOutputStream(this.file);
			this.properties.setProperty(Key, value);
			this.properties.store(fos,
					"this field is modify by system(software) note:" + note);
			fos.flush();
		} catch (FileNotFoundException e) {
			logger.error(new JsonBuilder().appendException("set value error")
					.appendLogDesc(e.getMessage()).toString());
		} catch (IOException e) {
			logger.error(new JsonBuilder().appendException("set value error")
					.appendLogDesc(e.getMessage()).toString());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error(new JsonBuilder()
							.appendException("set value error")
							.appendLogDesc(e.getMessage()).toString());
				}
			}
		}
	}

	/***
	 * 
	 * @Title: list
	 * @Description:把列表输出到流里面
	 * @param @param out 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void list(PrintStream out) {
		if (this.properties == null) {
			logger.error(new JsonBuilder().appendException(
					"properties is null!Please  loading files first!")
					.toString());
			return;
		}
		this.properties.list(out);
	}

	@Override
	protected void finalize() throws Throwable {
		this.fields = null;
		super.finalize();
	}

	public void close() throws IOException {
		if (this.is != null) {
			is.close();
		}
		if (this.fos != null) {
			fos.flush();
			fos.close();
		}
	}

	/***
	 * 
	 * @Title: getByFuzzyKey
	 * @Description: 模糊查找
	 * @param @param key
	 * @param @return
	 * @return Map 返回类型
	 * @throws
	 */
	public Map getByFuzzyKey(String key) {
		if (!this.isAllReaded) {
			this.getAllProperties();
		}
		Map map = new HashMap();
		Set keys = this.fields.keySet();
		for (Object kk : keys) {
			if (((String) kk).indexOf(key) >= 0) {
				map.put(kk, this.fields.get(keys));
			}
		}
		return map;
	}

}
