package com.bewkrop.auth.web.service;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bewkrop.auth.user.User;
import com.bewkrop.auth.user.UserRepository;
import com.bewkrop.auth.user.UserRepositoryFactory;
import com.bewkrop.auth.web.api.AuthToken;
import com.bewkrop.auth.web.api.UserRequest;
import com.bewkrop.auth.web.exception.AuthenticationException;
import com.bewkrop.auth.web.exception.EmptyKey;
import com.bewkrop.auth.web.exception.EmptyPassword;
import com.bewkrop.auth.web.exception.UserAlreadyExists;

@Path("/auth")
public class AuthService {
	
	@POST
	@Path("user")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@PermitAll
	public AuthToken create(UserRequest newUser) { // TODO: only allow HTTPS calls
		this.verifyCreate(newUser);
				
		String key = newUser.getKey();
		String password = newUser.getPassword();
		
		// TODO: password digester
		User user = new SimpleUser(key, password);
		
		UserRepository repo = UserRepositoryFactory.instance().build();
		repo.save(user);
		
		return new AuthToken(key, true);
	}
	
	@POST
	@Path("token")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@PermitAll
	public AuthToken login(UserRequest unverified) {
		User user = this.verifyLogin(unverified);
		return new AuthToken(user.key(), true);
	}
	
	
	private User verifyLogin(UserRequest unverified) {
		this.verify(unverified);
		
		UserRepository repo = UserRepositoryFactory.instance().build();
		User user = repo.get(unverified.getKey());
		
		if (user == null) throw new AuthenticationException();
		if (unverified.getPassword().equals(user.hash())) throw new AuthenticationException();
		
		return user;	
	}
	
	private void verifyCreate(UserRequest newUser) {
		this.verify(newUser);
		
		UserRepository repo = UserRepositoryFactory.instance().build();
		User user = repo.get(newUser.getKey());
		
		if (user != null) throw new UserAlreadyExists();
	}
	
	// TODO: esquema de validacao global (filtro de parametros)
	private void verify(UserRequest user) {
		String key = user.getKey();
		String password = user.getPassword();
		
		if (key == null) throw new EmptyKey();
		if (key.isEmpty()) throw new EmptyKey();
		if (password == null) throw new EmptyPassword();
		if (password.isEmpty()) throw new EmptyPassword();
	}
	
	private static class SimpleUser implements User {
		
		private static final String DEFAULT_ROLE = "AUTH";
		
		private final String key;
		private final String hash;
		
		public SimpleUser(String key, String hash) {
			this.key = key;
			this.hash = hash;
		}

		@Override
		public String key() {
			return this.key;
		}

		@Override
		public String hash() {
			return this.hash;
		}

		@Override
		public String roles() {
			return DEFAULT_ROLE;
		}
		
	}

}
