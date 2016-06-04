package com.springmq.module.bean;

import java.io.Serializable;

public class MessageInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 29686770121393192L;
	
	private String title;
	
	private String content;
	
	private String receiver;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
