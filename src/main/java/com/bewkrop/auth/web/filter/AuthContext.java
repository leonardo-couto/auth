package com.bewkrop.auth.web.filter;

import javax.ws.rs.core.SecurityContext;

import com.bewkrop.auth.user.Principal;

public class AuthContext implements SecurityContext {
	
	private final Principal user;
	
	public AuthContext(Principal user) {
		this.user = user;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.user;
	}

	@Override
	public boolean isUserInRole(String role) {
		return this.user.inRole(role);
	}

	@Override
	public boolean isSecure() {
		return this.user.isHttps();
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.FORM_AUTH;
	}

}
