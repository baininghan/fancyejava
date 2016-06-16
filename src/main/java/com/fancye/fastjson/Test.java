/**
 * 
 */
package com.fancye.fastjson;

import java.lang.reflect.Field;

/**
 * @author lvsed
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		User user = new User();
		user.setName("Li");
		user.setSex("F");
		
		pharse(user);
	}
	
	public static void pharse(Object obj) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
//		Method[] methods = clazz.getMethods();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			Object ob = field.get(obj);
			System.out.println(field.getName() + ":" + ob);
		}
	}

}
