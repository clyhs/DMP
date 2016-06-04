package org.clygd.rest;

import javax.servlet.ServletContext;

import org.restlet.Restlet;

import com.noelios.restlet.ext.servlet.ServletConverter;


public class MyServerServletConverter extends ServletConverter {

	public MyServerServletConverter(ServletContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyServerServletConverter(ServletContext context, Restlet target) {
		super(context, target);
		// TODO Auto-generated constructor stub
	}
	
	

}
