/**
 * 
 */
package com.fancye.aop.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.fancye.aop.service.UserServiceImpl;

/**
 * ͨ�������ԭ���UserService������������
 * 
 * @author Fancye
 *
 */
public class UserServiceInterceptor implements InvocationHandler {

	private Object targetObj;// Ҫ���ص�Ŀ�����
	private String method;// Ҫ���ص�Ŀ�����ķ�������
	private BeforeInterceptor beforeInterceptor;// ��������ǰִ��
	private AfterInterceptor afterInterceptor;// �������ú�ִ��
	
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
