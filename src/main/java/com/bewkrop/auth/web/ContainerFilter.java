package com.bewkrop.auth.web;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.bewkrop.auth.user.UserRequest;

//authentication filter - should go before any authorization filters
@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		UserRequest user = (UserRequest) requestContext.getProperty(AuthFilter.USER_PROPERTY);
		AuthContext auth = new AuthContext(user);
		requestContext.setSecurityContext(auth);
	}

}
