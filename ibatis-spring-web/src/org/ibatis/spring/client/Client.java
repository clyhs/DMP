package org.ibatis.spring.client;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ClientResource mailClient = new ClientResource(
				"http://localhost:8111/ibatis-spring-web/resources/mails/123");
		Representation mailRepresentation = mailClient
				.get(MediaType.APPLICATION_XML);
				mailClient.put(mailRepresentation);
				mailRepresentation = mailClient.get(MediaType.APPLICATION_JSON);
				mailClient.put(mailRepresentation);
	}

}
