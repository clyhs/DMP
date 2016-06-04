package org.ibatis.spring.dao;

import org.ibatis.spring.beans.Product;

public interface ProductDao {
	
	public Product findById(int id);
	
	public void addProduct();

}
