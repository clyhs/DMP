package org.web.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;


public class OrderApplication extends Application {  
	
	public OrderApplication(Context context)
	{
		super(context);
	}
	
    /** 
     * Creates a root Restlet that will receive all incoming calls. 
     */  
    @Override  
    public synchronized Restlet createRoot() {  
        Router router = new Router(getContext());  

        router.attach("/orders/{orderId}/{subOrderId}", OrderResource.class);  
        router.attach("/customers/{custId}", CustomerResource.class);  
        return router;  
    }  

}  