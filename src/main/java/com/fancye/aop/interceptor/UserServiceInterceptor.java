/**
 * 
 */
package com.fancye.aop.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.fancye.aop.service.UserServiceImpl;

/**
 * 通过反射的原理对UserService方法的拦截器
 * 
 * @author Fancye
 *
 */
public class UserServiceInterceptor implements InvocationHandler {

	private Object targetObj;// 要拦截的目标对象
	private String method;// 要拦截的目标对象的方法名称
	private BeforeInterceptor beforeInterceptor;// 方法调用前执行
	private AfterInterceptor afterInterceptor;// 方法调用后执行
	
	public UserServiceInterceptor(Object targetObj, String method) {
		this.targetObj = targetObj;
		this.method = method;
	}

	public void setBeforeInterceptor(BeforeInterceptor beforeInterceptor) {
		this.beforeInterceptor = beforeInterceptor;
	}

	public void setAfterInterceptor(AfterInterceptor afterInterceptor) {
		this.afterInterceptor = afterInterceptor;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getName().equals(this.method)) {
			execute(beforeInterceptor);
			Object o = method.invoke(new UserServiceImpl(), args);
			execute(afterInterceptor);
			
			return o;
		}
		return method.invoke(targetObj, args);
	}
	
	public void execute(Interceptor interceptor) {
		if (interceptor != null) {
			interceptor.execute();
		}
	}
}
