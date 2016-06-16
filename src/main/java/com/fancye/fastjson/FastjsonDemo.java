package com.fancye.fastjson;

import com.alibaba.fastjson.JSON;

/**
 * @author Fancye
 *
 */
public class FastjsonDemo {

	public static void main(String[] args) {
		// Encode
		Group group = new Group();
		group.setId(0L);
		group.setName("Group");
		
		User user1 = new User();
		user1.setName("oneUser");
		
		User user2 = new User();
		user2.setName("twoUser");
		
		group.addUser(user1);
		group.addUser(user2);
		
		String fastjson = JSON.toJSONString(group);
		System.out.println(fastjson);
		
		// Decode
//		Object o1 = JSON.parse(fastjson);
		JSON.parseObject(fastjson);
		Group groupParse = JSON.parseObject(fastjson, Group.class);
		System.out.println(groupParse.getName());
	}
}
