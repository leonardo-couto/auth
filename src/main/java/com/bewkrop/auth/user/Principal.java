package com.bewkrop.auth.user;

public class Principal implements java.security.Principal {
	
	private static final String CSV_REGEX = "[\\,|;]";
	
	private final User user;
	private final boolean https;

	public Principal(User user, boolean https) {
		this.user = user;
		this.https = https;
	}
	
	@Override
	public String getName() {
		return this.user.key();
	}

	public String[] getRoles() {
		return this.user.roles().split(CSV_REGEX);
	}
	
	public boolean isHttps() {
		return this.https;
	}
	
	public boolean inRole(String role) {
		String[] roles = this.getRoles();
		for (String r : roles) {
			if (r.equals(role)) return true;
		}
		return false;
	}

}
