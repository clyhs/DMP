package org.dmp.core.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java 调用脚本语言，
 * <p>
 * 暂时支持javaScript的调用，以后有时间在升级吧
 * </p>
 * 
 * @version 0.0.1
 * @author zhoushubin@unioncast.cn date 2013-3-4上午9:36:02
 * @see 0.0.1
 * @since 0.0.1
 * @copyRight unioncast All rights reserved
 */
@SuppressWarnings("all")
public class JavaScriptsUtils {
	static Logger logger = LoggerFactory.getLogger(JavaScriptsUtils.class);
	/***
	 * javaScript 执行引擎
	 */
	public final static String engin_name = "javascript";

	/*****
	 * 调用javascript的脚本方法
	 * <p>
	 * 调用add方法 add(3,2) var fa = 1
	 * </p>
	 * <p>
	 * C:XXOO.JS
	 * 
	 * function add(a,b){ return a*b*fa; } Map<String,String> vars = new
	 * HashMap<String,String>(); vars.put("fa","1");
	 * 调用doJAVAScriptMethodsFromFile("C:xxoo.js","add",vars,3,2);
	 * </p>
	 * 
	 * @param filepath
	 *            javascript 脚本的文件路径
	 * @param methods
	 *            需要执行的方法
	 * @param vars变量的值
	 * @param args
	 *            参数
	 *            <p>
	 * 
	 * @return
	 * @throws ScriptException
	 * @throws FileNotFoundException
	 * @throws NoSuchMethodException
	 */
	public static Object doJAVAScriptMethodsFromFile(String filepath,
			String methods, Map<String, Object> vars, Object... args) {
		// 获取JAVASCRIPT的执行引擎
		ScriptEngine engine = new ScriptEngineManager()
				.getEngineByName(engin_name);
		// 建立上下文变量
		Bindings bind = engine.createBindings();
		// bind.put("aa", 1) 设置javascript的变量的值 相当于变量有哪些值 绑定
		if (vars != null && vars.size() > 0) {
			Iterator it = vars.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				bind.put(key, vars.get(key));
			}
		}
		// 设置作用域为本次引擎
		engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
		try {
			engine.eval(new FileReader(filepath));// 执行JS文件
		} catch (FileNotFoundException e) {
			logger.error(new JsonBuilder()
					.appendException(
							String.format("file path: %s  not found...",
									filepath)).appendLogDesc(e.getMessage())
					.toString());
		} catch (ScriptException e) {
			logger.error(new JsonBuilder()
					.appendException("script exception.....")
					.appendLogDesc(e.getLocalizedMessage()).toString());
		}

		// 是否可调用方法
		Object o = null;
		if (engine instanceof Invocable) {
			Invocable in = (Invocable) engine;
			try {
				o = in.invokeFunction(methods, args);
			} catch (ScriptException e) {
				logger.error(new JsonBuilder()
						.appendException(
								String.format(
										"script exception{methods :%s}.....",
										methods)).append("args", args)
						.appendLogDesc(e.getMessage()).toString());
			} catch (NoSuchMethodException e) {
				logger.error(new JsonBuilder()
						.appendException(
								String.format(
										"script exception{methods :%s}.....",
										methods)).append("args", args)
						.appendLogDesc(e.getMessage()).toString());
			}
		}
		return o;
	}

	/***
	 * 
	 * * 调用javascript的脚本方法
	 * <p>
	 * 调用add方法 add(3,2) var fa = 1
	 * </p>
	 * <p>
	 * 方法内容： JScontext： =================== function add(a,b){ return a*b*fa; }
	 * 
	 * ==================== Map<String,String> vars = new
	 * HashMap<String,String>(); vars.put("fa","1");
	 * 调用doJAVAScriptMethodsFromFile(JScontext,"add",vars,3,2);
	 * </p>
	 * 
	 * @param JScontext
	 * @param methods
	 * @param vars
	 * @param args
	 * @return
	 * @throws FileNotFoundException
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	public static Object doJAVAScriptMethodsFromString(String JScontext,
			String methods, Map<String, Object> vars, Object... args) {
		// 获取JAVASCRIPT的执行引擎
		ScriptEngine engine = new ScriptEngineManager()
				.getEngineByName(engin_name);

		// 建立上下文变量

		Bindings bind = engine.createBindings();
		// bind.put("aa", 1) 设置javascript的变量的值 相当于变量有哪些值 绑定
		if (vars != null && vars.size() > 0) {
			Iterator it = vars.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				bind.put(key, vars.get(key));
			}
		}

		// 设置作用域为本次引擎
		engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);

		try {
			engine.eval(JScontext);// 执行JS文件
		} catch (ScriptException e) {
			logger.error(new JsonBuilder()
					.appendException(
							String.format("script exception{context :%s}.....",
									JScontext)).append("args", args)
					.appendLogDesc(e.getMessage()).toString());
		}

		// 是否可调用方法
		Object o = null;
		if (engine instanceof Invocable) {
			Invocable in = (Invocable) engine;
			try {
				o = in.invokeFunction(methods, args);
			} catch (ScriptException e) {
				logger.error(new JsonBuilder()
						.appendException(
								String.format(
										"script exception{methods :%s}.....",
										methods)).append("args", args)
						.appendLogDesc(e.getMessage()).toString());
			} catch (NoSuchMethodException e) {
				logger.error(new JsonBuilder()
						.appendException(
								String.format(
										"script exception{methods :%s}.....",
										methods)).append("args", args)
						.appendLogDesc(e.getMessage()).toString());
			}
		}
		return o;
	}

	public static void main(String[] args) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("fa", 0.5);

		System.out.println(JavaScriptsUtils.doJAVAScriptMethodsFromString(
				"function add(a,b){return (a+b+fa);}", "add", vars, 3, 2));
	}

}
