package org.dmp.util;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean{
	
	private static final Logger log = Logger.getLogger(MethodCacheInterceptor.class);
	private Cache cache;
	
	public void setCache(Cache cache){
		this.cache = cache;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String targetName = invocation.getThis().getClass().getName();   //表示那个类触发了这个类
	    String methodName = invocation.getMethod().getName();       //表示那个方法触发了这个类
	    
	    Object[] arguments = invocation.getArguments();
	    Object result;
	    
	    String cacheKey = getCacheKey(targetName, methodName, arguments);
	    
	    Element element = cache.get(cacheKey);
	    if(element == null){
	    	//log.info(cacheKey + "加入到缓存： " + cache.getName());
	    	result = invocation.proceed();
	    	element = new Element(cacheKey, (Serializable)result);
	    	cache.put(element);
	    }else{
	    	//log.info(cacheKey + "使用缓存： " + cache.getName());
	    }
	    return element.getValue();
	}
	
	private String getCacheKey(String targetName, String methodName,  
            Object[] arguments) {  
        StringBuffer sb = new StringBuffer();  
        sb.append(targetName).append(".").append(methodName);  
        if ((arguments != null) && (arguments.length != 0)) {  
            for (int i = 0; i < arguments.length; i++) {  
                sb.append(".").append(arguments[i]);  
            }  
        }  
  
        return sb.toString();  
    }  

	public void afterPropertiesSet() throws Exception {
		if(null == cache) {  
            throw new IllegalArgumentException("Cache should not be null.");  
        }  
	}
	
}
