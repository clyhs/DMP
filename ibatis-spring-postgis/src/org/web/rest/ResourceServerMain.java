package org.web.rest;

import java.util.ArrayList;
import java.util.List;
import org.restlet.Component;
import org.restlet.data.Protocol;


public class ResourceServerMain {
    public static void main(String[] args) throws Exception {
        // Create a new Component.
        Component component = new Component();
       // Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, 8182);  
        component.getDefaultHost().attach("/rest",new OrderApplication(component.getContext()));
        component.start();
    }
}
