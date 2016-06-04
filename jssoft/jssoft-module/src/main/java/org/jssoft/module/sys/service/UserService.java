package org.jssoft.module.sys.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


import org.jssoft.module.sys.bean.User;
import org.jssoft.module.sys.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

@Named("userService")
public class UserService {
	
	@Inject
    private UserDao userDao;
	
    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }
 
    @Transactional
    public void deleteUser(User user) {
        userDao.delete(user);
    }
 
    @Transactional
    public void updateUser(User user) {
        userDao.update(user);
    }
 
    public User getUserById(int id) {
        User user = userDao.find(id);
        return user;
    }
 
    public List<User> getUsers() {
    	return userDao.findAll();
    }
 
}
