/**
 * Copyright 2005-2012 Restlet S.A.S.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.ibatis.spring.restlet.server;

import java.io.IOException;

import javax.resource.ResourceException;

import org.ibatis.spring.beans.Mail;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.ext.xstream.XstreamRepresentation;


import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account. Leverages the SAX API.
 */
public class MailServerResource extends ServerResource {

	@Override
	protected void doInit() {
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	/*
	 * 
	 * @Get public Representation toXml() throws IOException { // Create a new
	 * SAX representation
	 * 
	 * DomRepresentation result = new DomRepresentation();
	 * 
	 * result.setIndenting(true); result.setNamespaceAware(true); Document doc =
	 * result.getDocument(); Node mailElt = doc.createElementNS(
	 * "http://www.rmep.org/namespaces/1.0", "mail"); doc.appendChild(mailElt);
	 * Node statusElt = doc.createElement("status");
	 * statusElt.setTextContent("received"); mailElt.appendChild(statusElt);
	 * Node subjectElt = doc.createElement("subject");
	 * subjectElt.setTextContent("Message to self");
	 * mailElt.appendChild(subjectElt); Node contentElt =
	 * doc.createElement("content"); contentElt.setTextContent("Doh!");
	 * mailElt.appendChild(contentElt); Node accountRefElt =
	 * doc.createElement("accountRef"); accountRefElt.setTextContent(new
	 * Reference(getReference(), "..") .getTargetRef().toString());
	 * mailElt.appendChild(accountRefElt);
	 * 
	 * return result; }
	 * 
	 * @Get public Representation toJson() throws JSONException { JSONObject
	 * mailElt = new JSONObject(); mailElt.put("status", "received");
	 * mailElt.put("subject", "Message to self"); mailElt.put("content",
	 * "Doh!"); mailElt.put("accountRef", new Reference(getReference(), "..")
	 * .getTargetRef().toString()); return new JsonRepresentation(mailElt); }
	 */

	/**
	 * 
	 * url= localhost:8080/**?media=xml/json
	 */
	
	@Override
	protected Representation put(Representation representation, Variant variant)
			throws org.restlet.resource.ResourceException {
		// TODO Auto-generated method stub
		Mail mail = new Mail();
		if (MediaType.APPLICATION_XML.isCompatible(representation
				.getMediaType())) {
			mail = new XstreamRepresentation<Mail>(representation)
					.getObject();
			System.out.println("XML representation received");
		} else if (MediaType.APPLICATION_JSON.isCompatible(representation
				.getMediaType())) {
			mail = new JacksonRepresentation<Mail>(representation, Mail.class)
					.getObject();
			System.out.println("JSON representation received");
		}
		if (mail != null) {
			System.out.println("Status: " + mail.getState());
			System.out.println("Subject: " + mail.getSubject());
			System.out.println("Content: " + mail.getContext());
			System.out.println("Account URI: " + mail.getAccountRef());
			System.out.println();
		}
		return null;
	}

	@Override
	protected Representation get(Variant variant)
			throws org.restlet.resource.ResourceException {
		// TODO Auto-generated method stub
		Representation result = null;
		Mail mail = new Mail();
		mail.setState("received");
		mail.setSubject("Message to self");
		mail.setContext("Doh!");
		mail.setAccountRef(new Reference(getReference(), "..").getTargetRef()
				.toString());
		if (MediaType.APPLICATION_XML.isCompatible(variant.getMediaType())) {
			result = new XstreamRepresentation<Mail>(mail);
		} else if (MediaType.APPLICATION_JSON.isCompatible(variant
				.getMediaType())) {
			result = new JacksonRepresentation<Mail>(mail);
		}
		return result;
	}

	/*
	 * @Put public void store(SaxRepresentation mailRep) throws IOException {
	 * String rmepNs = "http://www.rmep.org/namespaces/1.0";
	 * mailRep.setNamespaceAware(true); mailRep.getNamespaces().put("", rmepNs);
	 * mailRep.getNamespaces().put("rmep", rmepNs); String status =
	 * mailRep.getText("/:mail/:status"); String subject =
	 * mailRep.getText("/rmep:mail/:subject"); String content =
	 * mailRep.getText("/rmep:mail/rmep:content"); String accountRef =
	 * mailRep.getText("/:mail/rmep:accountRef"); System.out.println("Status: "
	 * + status); System.out.println("Subject: " + subject);
	 * System.out.println("Content: " + content);
	 * System.out.println("Account URI: " + accountRef); }
	 */

}
