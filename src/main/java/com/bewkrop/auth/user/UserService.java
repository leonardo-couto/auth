package com.bewkrop.auth.user;

public interface UserService {
	
	public User get(String key);
	
	public boolean save(User user);
	
}
