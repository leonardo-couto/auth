package com.bewkrop.auth.web.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.bewkrop.auth.user.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ContainerFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		Principal user = (Principal) requestContext.getProperty(AuthFilter.USER_PROPERTY);
		AuthContext auth = new AuthContext(user);
		requestContext.setSecurityContext(auth);
	}

}
