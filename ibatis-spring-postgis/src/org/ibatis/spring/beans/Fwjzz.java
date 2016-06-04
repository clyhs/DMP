package org.ibatis.spring.beans;

import java.io.Serializable;

public class Fwjzz implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 996077715598196454L;
	
	private int id;
	
	private String name;
	
	private String geom;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}
	
	

}
