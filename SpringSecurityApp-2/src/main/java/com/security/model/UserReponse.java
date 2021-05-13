package com.security.model;

public class UserReponse {
	
	private String username;
	
	public UserReponse() {
		
	}

	public UserReponse(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserReponse [username=" + username + "]";
	}
	
	
	
	

}
