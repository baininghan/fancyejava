/**
 * 
 */
package com.fancye.aop.service;

import java.io.Serializable;

import com.fancye.bean.User;

/**
 * @author Fancye
 *
 */
public class UserServiceImpl implements UserService {

	@Override
	public void addUser(User user) {
		System.out.println("save user success!");
	}

	@Override
	public void delUser(User user) {
		System.out.println("delete user success!");
	}

	@Override
	public void uptUser(User user) {
		System.out.println("update user success!");
	}

	@Override
	public User getUser(Serializable id) {
		System.out.println("get user success!");
		return null;
	}

}
