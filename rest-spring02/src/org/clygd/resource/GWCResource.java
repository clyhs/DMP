package org.clygd.resource;

import org.clygd.bean.User;
import org.clygd.service.BaseService;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class GWCResource extends MyResourceBase {
	


	public GWCResource(Context context, Request request, Response response,
			Class clazz, BaseService bs) {
		super(context, request, response, clazz, bs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object handleObjectGet() throws Exception {
		
		System.out.println("hello");
		// TODO Auto-generated method stub
		User u = new User();
		u.setId(1);
		u.setUsername("root");
		
		
		return u;
	}

}
