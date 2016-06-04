package org.clygd.resource;

import org.clygd.format.DataFormat;
import org.clygd.format.MediaTypes;
import org.clygd.format.ReflectiveXMLFormat;
import org.clygd.rest.PageInfo;
import org.clygd.service.BaseService;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class MyResourceBase extends ReflectiveResource {
	
	
	protected BaseService bs;
	
	protected Class clazz;
	
	
	
	
	public MyResourceBase(Context context,Request request, Response response, Class clazz,BaseService bs){
		
	}

	
    protected void encodeAlternateAtomLink( String link, HierarchicalStreamWriter writer ) {
        encodeAlternateAtomLink( link, writer, getFormatGet() );
    }
    
    protected void encodeAlternateAtomLink( String link, HierarchicalStreamWriter writer, DataFormat format ) {
        writer.startNode( "atom:link");
        writer.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
        writer.addAttribute( "rel", "alternate" );
        writer.addAttribute( "href", href(link,format) );
        
        if ( format != null ) {
            writer.addAttribute( "type", format.getMediaType().toString() );
        }
        
        writer.endNode();
    }
    
    protected void encodeLink( String link, HierarchicalStreamWriter writer ) {
        encodeLink( link, writer, getFormatGet() );
    }
    
    protected void encodeLink( String link, HierarchicalStreamWriter writer, DataFormat format ) {
        if ( getFormatGet() instanceof ReflectiveXMLFormat  ) {
            //encode as an atom link
            encodeAlternateAtomLink(link, writer, format);
        }
        else {
            //encode as a child element
            writer.startNode( "href" );
            writer.setValue( href( link, format) );
            writer.endNode();
        }
    }
    
    protected void encodeCollectionLink( String link, HierarchicalStreamWriter writer ) {
        encodeCollectionLink( link, writer, getFormatGet() );
    }
    
    protected void encodeCollectionLink( String link, HierarchicalStreamWriter writer, DataFormat format) {
        if ( format instanceof ReflectiveXMLFormat ) {
            //encode as atom link
            encodeAlternateAtomLink(link, writer, format);
        }
        else {
            //encode as a value
            writer.setValue( href( link, format ) );
        }
    }
    
    String href( String link, DataFormat format ) {
        PageInfo pg = getPageInfo();
        
        //try to figure out extension
        String ext = null;
        if ( format != null ) {
            ext = MediaTypes.getExtensionForMediaType( format.getMediaType() );
        }
        
        if ( ext == null ) {
            ext = pg.getExtension();
        }
        
        if(ext != null && ext.length() > 0)
            link = link+ "." + ext;
        
        // encode as relative or absolute depending on the link type
        if ( link.startsWith( "/") ) {
            // absolute, encode from "root"
            return pg.rootURI(link);
        } else {
            //encode as relative
            return pg.pageURI(link);
        }
    }

}
