package org.clygd.test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.clygd.rest.MyServerExtensions;
import org.clygd.rest.RESTMapping;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanTest {
	
	private BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	@Test
	public void test1(){
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MyServerExtensions ex = new MyServerExtensions();
		ex.setApplicationContext(context);
		ex.extensions(RESTMapping.class).size();
		
		
		System.out.println(ex.extensions(RESTMapping.class).size());
		
		
		/*
		Map m = rm.getRoutes();
		
		Iterator it = m.entrySet().iterator();
		while (it.hasNext()){
			 Map.Entry entry = (Map.Entry) it.next();
			 System.out.println(entry.getValue());
		}*/
	}
}
