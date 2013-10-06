package com.bewkrop.auth.web;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.bewkrop.auth.user.UserRequest;

public class AuthContext implements SecurityContext {
	
	private final UserRequest user;
	
	public AuthContext(UserRequest user) {
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
