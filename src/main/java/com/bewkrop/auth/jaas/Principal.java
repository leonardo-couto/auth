package com.bewkrop.auth.jaas;

class Principal implements java.security.Principal {
	
	private final String key;

	public Principal(String key) {
		this.key = key;
	}
	
	@Override
	public String getName() {
		return this.key;
	}

}
