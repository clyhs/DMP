package org.ibatis.spring.restlet.server;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MailServerApplication extends Application {
	
	public MailServerApplication() {
        setName("RESTful Mail Server application");
        setDescription("Example application for 'Restlet in Action' book");
        setOwner("Restlet S.A.S.");
        setAuthor("The Restlet Team");
    }

	@Override
	public Restlet createInboundRoot() {
		// TODO Auto-generated method stub
		Router router = new Router(getContext());
		router.attach("/",RootServerResource.class);
		router.attach("/accounts/", AccountsResource.class);
		router.attach("/mails/", MailServerResource.class);
		
		return router;
	}

	
}
