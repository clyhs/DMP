package org.jssoft.module.sys.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jssoft.module.sys.bean.User;
import org.jssoft.module.sys.service.UserService;
import org.springframework.context.annotation.Scope;

@Named("userMB")
@Scope("request")
public class UserManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
    private static final String SUCCESS = "success";
    private static final String ERROR   = "error";
 
    @Inject
    @Named("userService")
    private UserService userService;
    
    private List<User> userList;
 
    private int id;
    private String name;
    private String surname;
    
    public UserManagedBean() {
    }

	public String addUser() {
		User user = new User();
		user.setId(getId());
		user.setUsername(getName());
		userService.addUser(user);
		return SUCCESS;

    }
 
    public void reset() {
        this.setId(0);
        this.setName("");
        this.setSurname("");
    }
 
    public List<User> getUserList() {
        userList = new ArrayList<User>();
        userList.addAll(userService.getUsers());
        return userList;
    }
 
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getSurname() {
        return surname;
    }
 
    public void setSurname(String surname) {
        this.surname = surname;
    }
 
}
