package com.bewkrop.auth.jaas;

import java.security.Principal;

class Role implements Principal {
	
	private final String role;
	
	public Role(String role) {
		this.role = role;
	}

	@Override
	public String getName() {
		return this.role;
	}
	
}
