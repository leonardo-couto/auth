package com.bewkrop.auth.jaas;

public class Principal implements java.security.Principal {
	
	private final String key;

	public Principal(String key) {
		this.key = key;
	}
	
	@Override
	public String getName() {
		return this.key;
	}
	
//	public boolean inRole(String role) {
//		String[] roles = this.getRoles();
//		for (String r : roles) {
//			if (r.equals(role)) return true;
//		}
//		return false;
//	}
//	
//	private static final String CSV_REGEX = "[\\,|;]";
//	
//	public String[] getRoles() {
//		return this.user.roles().split(CSV_REGEX);
//	}

}
