package org.web.rest;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

@SuppressWarnings("deprecation")
public class CustomerResource extends Resource {  
    String customerId = ""; 
    
    
      
    @Override
	public void init(Context context, Request request, Response response) {
		// TODO Auto-generated method stub
		super.init(context, request, response);
		customerId = (String) request.getAttributes().get("custId");  
	}

    public CustomerResource(){  
        getVariants().add(new Variant(MediaType.APPLICATION_XML));   
    }  
    
	public CustomerResource(Context context, Request request, Response response) {  
        super(context, request, response);  
        // This representation has only one type of representation.  
        getVariants().add(new Variant(MediaType.APPLICATION_XML));  
    }  
  
	public Representation getRepresentation(Variant variant) {  
		
        Representation representation = new StringRepresentation("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>" +customerId+"</root>",  
                MediaType.APPLICATION_XML);  
        
        return representation;  
    }  
	
	
}  