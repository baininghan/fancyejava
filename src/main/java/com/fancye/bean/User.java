package com.fancye.bean;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private String username;
	private String password;
	private Date date;
	
	public User(String username, String password, Date date) {
		this.username = username;
		this.password = password;
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
