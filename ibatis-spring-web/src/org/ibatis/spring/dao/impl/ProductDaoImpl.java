package org.ibatis.spring.dao.impl;

import org.ibatis.spring.beans.Product;
import org.ibatis.spring.dao.ProductDao;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ProductDaoImpl extends SqlMapClientDaoSupport implements
		ProductDao {

	@Override
	public Product findById(int id) {
		// TODO Auto-generated method stub
		return (Product) getSqlMapClientTemplate().queryForObject("getProduct",id);
	}

	@Override
	public void addProduct() {
		// TODO Auto-generated method stub
		
		Product p = new Product();
		p.setId(2);
		p.setName("123");
		
		getSqlMapClientTemplate().insert("addProduct",p);
		
	}

	
	
}
