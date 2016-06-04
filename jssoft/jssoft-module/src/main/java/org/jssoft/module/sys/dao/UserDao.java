package org.jssoft.module.sys.dao;

import javax.inject.Named;

import org.jssoft.base.dao.BaseDao;
import org.jssoft.module.sys.bean.User;


@Named
public class UserDao extends BaseDao<User, Integer> {

	public UserDao() {
		super(User.class);
	}
}