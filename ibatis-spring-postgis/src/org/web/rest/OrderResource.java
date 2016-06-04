package org.web.rest;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class OrderResource extends Resource {  
    String orderId = "";  
    String subOrderId = "";  
    
    public OrderResource()
    {
    	getVariants().add(new Variant(MediaType.TEXT_PLAIN));  
    }
    
    
      
    @Override
	public void init(Context context, Request request, Response response) {
		// TODO Auto-generated method stub
		super.init(context, request, response);
		orderId = (String) request.getAttributes().get("orderId");  
        subOrderId = (String) request.getAttributes().get("subOrderId");
	}



	public OrderResource(Context context, Request request,     
            Response response) {     
        super(context, request, response);    
          
        // This representation has only one type of representation.     
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));     
    }     
      
    public Representation getRepresentation(Variant variant) {     
        Representation representation = new StringRepresentation(     
                "the order id is : " + orderId + " and the sub order id is : " + subOrderId, MediaType.TEXT_PLAIN);     
        return representation;     
    }  
    
    
    
}  