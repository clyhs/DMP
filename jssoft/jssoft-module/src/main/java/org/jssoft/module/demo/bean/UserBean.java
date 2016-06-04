package org.jssoft.module.demo.bean;

import java.io.Serializable;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named("user")
@SessionScoped
public class UserBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
