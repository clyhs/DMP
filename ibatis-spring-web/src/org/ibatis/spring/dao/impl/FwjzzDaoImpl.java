package org.ibatis.spring.dao.impl;

import org.ibatis.spring.beans.Fwjzz;
import org.ibatis.spring.dao.FwjzzDao;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class FwjzzDaoImpl extends SqlMapClientDaoSupport implements FwjzzDao {

	@Override
	public void addFwjzz() {
		// TODO Auto-generated method stub
		
		Fwjzz f = new Fwjzz();
		f.setId(3);
		f.setName("123");
		f.setGeom("Point Z (10 10 5)");
		
		getSqlMapClientTemplate().insert("addFwjzz",f);

	}

	@Override
	public Fwjzz getFwjzz(int id) {
		// TODO Auto-generated method stub
		return (Fwjzz) getSqlMapClientTemplate().queryForObject("getFwjzz",3);
	}

	
	
}
