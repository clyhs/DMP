package org.ibatis.spring.beans;

import java.io.Serializable;

public class Mail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4704121316547598071L;
	
	private String state;
	
	private String subject;
	
	private String Context;
	
	private String accountRef;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}

	public String getAccountRef() {
		return accountRef;
	}

	public void setAccountRef(String accountRef) {
		this.accountRef = accountRef;
	}

}
