package org.ibatis.spring.dao.impl;

import org.ibatis.spring.beans.Fwjz;
import org.ibatis.spring.dao.FwjzDao;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class FwjzDaoImpl extends SqlMapClientDaoSupport implements FwjzDao {

	@Override
	public void addFwjz() {
		// TODO Auto-generated method stub
		WKTReader fromText = new WKTReader();
		try {
			Point p =(Point)fromText.read("Point(1 2)");
			
			Fwjz f = new Fwjz();
			f.setId(2);
			f.setName("123");
			f.setPoint(p);
			
			getSqlMapClientTemplate().insert("addFwjz",f);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Fwjz getFwjz(int id) {
		// TODO Auto-generated method stub
		return (Fwjz) getSqlMapClientTemplate().queryForObject("getFwjz",1);
	}
	
	

}
