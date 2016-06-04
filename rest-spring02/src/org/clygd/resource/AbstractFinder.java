package org.clygd.resource;

import org.clygd.service.BaseService;
import org.clygd.util.RESTUtils;
import org.restlet.Finder;
import org.restlet.data.Request;

public abstract class AbstractFinder extends Finder {
	
	protected BaseService bs;
	
	public AbstractFinder(){}
	
	
	public AbstractFinder(BaseService bs){
		this.bs = bs;
	}
	
	protected String getAttribute(Request request, String attribute) {
        return RESTUtils.getAttribute(request, attribute);
    }

}
