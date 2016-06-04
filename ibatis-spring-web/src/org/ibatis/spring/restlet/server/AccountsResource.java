package org.ibatis.spring.restlet.server;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Resource;
import org.restlet.resource.ServerResource;


public class AccountsResource extends ServerResource {
	
	@Get
	public Representation represent()
	{
		Representation representation = new StringRepresentation(     
                "123", MediaType.TEXT_PLAIN);
		return representation;
	}
	

}
