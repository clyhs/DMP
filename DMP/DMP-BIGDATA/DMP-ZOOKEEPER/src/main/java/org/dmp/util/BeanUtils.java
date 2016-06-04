package org.dmp.util;

public class BeanUtils {
	public static final Class<?> getClass(String className){
			try {
				Class<?> clazz = Class.forName(className);
				return clazz;
			} catch (ClassNotFoundException e) {
				return null;
			}
	}
	
	public static final <T> T getInstanceByDefaultConstruct(Class<?> clazz){
		try {
		T	obj = (T) clazz.newInstance();
			return (T) obj;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}
	public static final <T> T getInstanceByDefaultConstruct(String className){
		Class<?> clazz=getClass(className);
		if(null!=clazz){
			return(T) getInstanceByDefaultConstruct(clazz); 
		}
		return null;
	}
	
	
	public static final void getMethod(String method,Object obj){
		//obj
	}
}
