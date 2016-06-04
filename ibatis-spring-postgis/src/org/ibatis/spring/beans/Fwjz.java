package org.ibatis.spring.beans;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Point;

public class Fwjz implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8548146100557829007L;
	
	private int id;
	
	private String name;
	
	private Point point;

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

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
	
	

}
