package org.ibatis.spring.test;

import static org.junit.Assert.*;

import org.ibatis.spring.beans.Product;
import org.ibatis.spring.dao.ProductDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProductTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void getProduct()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        ProductDao testDAOImpl=(ProductDao)context.getBean("productDao");
        
        //System.out.println(testDAOImpl.findById(1));
        Product p = testDAOImpl.findById(1);
        
        System.out.println(p.getName());
	}
	
	@Test
	public void addProduct()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        ProductDao testDAOImpl=(ProductDao)context.getBean("productDao");
        
        
        testDAOImpl.addProduct();
        
	}
	

}
