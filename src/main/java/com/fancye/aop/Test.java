/**
 * 
 */
package com.fancye.aop;

import java.lang.reflect.Proxy;
import java.util.Date;

import com.fancye.aop.interceptor.AfterInterceptor;
import com.fancye.aop.interceptor.BeforeInterceptor;
import com.fancye.aop.interceptor.UserServiceInterceptor;
import com.fancye.aop.service.UserService;
import com.fancye.aop.service.UserServiceImpl;
import com.fancye.bean.User;

/**
 * @author Fancye
 *
 */
public class Test {

	public void test(){
		UserServiceInterceptor interceptor = new UserServiceInterceptor(new UserServiceImpl(), "addUser");
		interceptor.setBeforeInterceptor(new BeforeInterceptor() {
			
			@Override
			public void execute() {
				System.out.println("execute BeforeInterceptor..");
			}
		});
		
		interceptor.setAfterInterceptor(new AfterInterceptor() {
			
			@Override
			public void execute() {
				System.out.println("execute AfterInterceptor..");
			}
		});
		
		UserService userService = (UserService)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ UserService.class }, interceptor);
		userService.addUser(new User("Fancye", "12345", new Date()));
		
	}
	public static void main(String[] args) {
		new Test().test();
	}
}
