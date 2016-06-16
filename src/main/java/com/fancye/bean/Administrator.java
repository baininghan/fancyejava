package com.fancye.bean;

import java.io.Serializable;

public class Administrator implements Serializable {

	private User user;
	private Boolean b;
	
	public Administrator(User user, boolean b) {
		this.user = user;
		this.b = b;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getB() {
		return b;
	}

	public void setB(Boolean b) {
		this.b = b;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
