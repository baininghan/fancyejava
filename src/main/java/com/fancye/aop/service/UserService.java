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
public interface UserService {

	public void addUser(User user);
	public void delUser(User user);
	public void uptUser(User user);
	public User getUser(Serializable id);
	
}
