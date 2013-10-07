package com.bewkrop.auth.user;

public interface UserRepository {
	
	public User get(String key);
	
	public boolean save(User user);
	
}
