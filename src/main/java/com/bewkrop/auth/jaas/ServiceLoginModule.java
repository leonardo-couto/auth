package com.bewkrop.auth.jaas;

import java.sql.SQLException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import com.bewkrop.auth.user.User;
import com.bewkrop.auth.user.UserRepository;
import com.bewkrop.auth.user.UserRepositoryFactory;

public class ServiceLoginModule extends AbstractLoginModule {
	
	private UserRepositoryFactory factory = null;
	
	/**
	 * Initialize LoginModule. Called once by JAAS after new instance created.
	 * 
	 * @param subject
	 * @param callbackHandler
	 * @param sharedState
	 * @param options
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, 
						   Map<String, ?> sharedState, Map<String, ?> options) {
		
		super.initialize(subject, callbackHandler, sharedState, options);
		this.factory = UserRepositoryFactory.instance();
	}

	@Override
	public User getUserInfo(String username) throws SQLException {
		
		UserRepository service = this.factory.build();
		return service.get(username);
	}


}
