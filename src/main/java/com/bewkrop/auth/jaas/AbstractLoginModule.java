package com.bewkrop.auth.jaas;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.LoggerFactory;

import com.bewkrop.auth.user.User;

/**
 * AbstractLoginModule
 * 
 * Abstract base class for all LoginModules. Subclasses should just need to
 * implement getUserInfo method.
 * 
 */
public abstract class AbstractLoginModule implements LoginModule {
	
	private static final String ERROR = "Login ERROR";

	private CallbackHandler callbackHandler;
	private Subject subject;

	private boolean authenticated = false;
	private boolean commited = false;
	private List<Principal> principals;
	private User user;

	/**
	 * @see javax.security.auth.spi.LoginModule#abort()
	 * @throws LoginException
	 */
	public boolean abort() throws LoginException {
		if (!this.authenticated) { // not authenticated
			return false;
			
		} else if (!this.commited) { // authenticated, but not commited 
			this.authenticated = false;
			this.commited = false;
			this.principals = null;
			this.user = null;
			return true;
			
		} else { // logged in
			return this.logout();
		}
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#commit()
	 * @return true if committed, false if not (likely not authenticated)
	 * @throws LoginException
	 */
	public boolean commit() throws LoginException {
		this.commited = false;
		
		if (!this.authenticated) {
			this.principals = null;
			this.user = null;
			this.commited = true;
			return false;
		}

		try {
			
			String name = this.user.key();
			List<String> roles = this.user.roles();

			List<Principal> principals = new ArrayList<>(roles.size() + 1);
			principals.add(new com.bewkrop.auth.jaas.Principal(name));
			for (String role : roles) principals.add(new Role(role));

			this.subject.getPrincipals().addAll(principals);
			this.principals = principals;			

			this.commited = true;
			
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error(ERROR, e);
			throw new LoginException(ERROR);
		}
		
		return true;
	}

	private Callback[] getCallbacks() {

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("Enter user name: ");
		callbacks[1] = new PasswordCallback("Enter password: ", false);

		return callbacks;
	}

	public abstract User getUserInfo(String username) throws SQLException;

	/**
	 * @see javax.security.auth.spi.LoginModule#login()
	 * @return true if is authenticated, throws exception otherwise
	 * @throws LoginException
	 */
	public boolean login() throws LoginException {
		
		this.authenticated = false;
		if (this.callbackHandler == null) {
			throw new LoginException("No callback handler");
		}

		NameCallback nameCallback;
		PasswordCallback passwordCallback;

		try {

			Callback[] callbacks = this.getCallbacks();
			this.callbackHandler.handle(callbacks);
			nameCallback = ((NameCallback) callbacks[0]);
			passwordCallback = ((PasswordCallback) callbacks[1]);

		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error(ERROR, e);
			throw new LoginException(ERROR);
		}

		String email = nameCallback.getName();
		char[] password = passwordCallback.getPassword();
		User user = null;

		if (email == null || password == null) throw new FailedLoginException();

		try {
			user = this.getUserInfo(email);
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error(ERROR, e);
			throw new LoginException(ERROR);
		}

		if (user == null) throw new FailedLoginException();

		boolean auth = user.credential().check(password);
		passwordCallback.clearPassword();
		if (!auth) throw new FailedLoginException();

		this.authenticated = true;
		this.user = user;
		return true;
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#logout()
	 * @return true always
	 * @throws LoginException
	 */
	public boolean logout() throws LoginException {
		this.subject.getPrincipals().removeAll(this.principals);
		
		this.authenticated = false;
		this.commited = false;
		this.principals = null;
		this.user = null;
		
		return true;
	}

	/**
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
	 *      javax.security.auth.callback.CallbackHandler, java.util.Map,
	 *      java.util.Map)
	 * @param subject
	 * @param callbackHandler
	 * @param sharedState
	 * @param options
	 */
	public void initialize(Subject subject, 
			CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		this.callbackHandler = callbackHandler;
		this.subject = subject;
	}

}
