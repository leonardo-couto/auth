package com.bewkrop.auth.user;

import java.util.List;

public interface User {

	public String key();
	public Credential credential();
	public List<String> roles();
	
}
