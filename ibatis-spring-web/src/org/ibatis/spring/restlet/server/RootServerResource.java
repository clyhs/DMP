package org.ibatis.spring.restlet.server;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class RootServerResource extends ServerResource {
	
	@Get
	public Representation represent()
	{
		Representation representation = new StringRepresentation(     
                "welcome to restful service", MediaType.TEXT_PLAIN);
		return representation;
	}

}
