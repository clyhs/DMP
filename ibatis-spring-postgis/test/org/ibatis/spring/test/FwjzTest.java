package org.ibatis.spring.test;

import static org.junit.Assert.*;

import org.ibatis.spring.beans.Fwjz;
import org.ibatis.spring.beans.Fwjzz;
import org.ibatis.spring.dao.FwjzDao;
import org.ibatis.spring.dao.FwjzzDao;
import org.ibatis.spring.dao.ProductDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class FwjzTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	@Test
	public void addFwjz()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        FwjzDao testDAOImpl=(FwjzDao)context.getBean("fwjzDao");
        
        testDAOImpl.addFwjz();
	}
	
	@Test
	public void getFwjz()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        FwjzDao testDAOImpl=(FwjzDao)context.getBean("fwjzDao");
        
        Fwjz f =testDAOImpl.getFwjz(1);
        
        System.out.println(f);
        
        System.out.println(f.getPoint().toText());
	}
	
	@Test
	public void addFwjzz()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        FwjzzDao testDAOImpl=(FwjzzDao)context.getBean("fwjzzDao");
        
        testDAOImpl.addFwjzz();
	}
	
	@Test
	public void getFwjzz()
	{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext-*.xml");
        FwjzzDao testDAOImpl=(FwjzzDao)context.getBean("fwjzzDao");
        
        Fwjzz f =testDAOImpl.getFwjzz(3);
        
        System.out.println(f);
        
        System.out.println(f.getGeom());
        
        
        WKTReader fromText = new WKTReader();
        try {
			Geometry  geom = fromText.read(f.getGeom());
			geom.setSRID(4326);
			
			System.out.println(geom.toText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
