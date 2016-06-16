package com.fancye;

import java.util.Date;

import com.fancye.bean.Administrator;
import com.fancye.bean.User;
import com.fancye.utils.BeanUtil;

public class TestCase {
	
	public static void main(String[] args) {
		Administrator src = new Administrator(new User("Kent", "123456", new Date()), true);
		Administrator dist = BeanUtil.cloneTo(src);
		
		System.out.println(src == dist);			// false
		System.out.println(src.equals(dist));		// true
		
		System.out.println(src.getUser() == dist.getUser());		//false ! Well done!
		System.out.println(src.getUser().equals(dist.getUser()));	//true
	}
	
}
