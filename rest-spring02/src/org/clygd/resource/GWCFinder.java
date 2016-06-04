package org.clygd.resource;

import org.clygd.service.BaseService;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public class GWCFinder extends AbstractFinder {
	
	public GWCFinder(){
		System.out.println("finder");
	}

	public GWCFinder(BaseService bs) {
		super(bs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Resource findTarget(Request request, Response response) {
		// TODO Auto-generated method stub
		System.out.println("123");
		return new GWCResource(null,request,response,null, null);
	}
	
	
	
	

}
