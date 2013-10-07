package com.bewkrop.auth.user;

import java.io.IOException;
import java.util.Properties;

public class UserRepositoryFactory {
	
	private static final String PROP_KEY = "user.service.class";
	private static UserRepositoryFactory factory = new UserRepositoryFactory();
	
	private final UserRepository service;
	
	private UserRepositoryFactory() {
		this.service = getInstance();
	}
	
	public UserRepository build() {
		return this.service;
	}
	
	public static UserRepositoryFactory instance() {
		return factory;
	}
	
	private static UserRepository getInstance() {
		String name = getName();
		
		try {
			ClassLoader classLoader = UserRepositoryFactory.class.getClassLoader();
			Class<?> clazz = classLoader.loadClass(name);		
			return (UserRepository) clazz.newInstance();
			
		} catch (ReflectiveOperationException e) {
			String error = "Error trying to create a new instance of class '%s'";
			throw new RuntimeException(String.format(error, name), e);
		}
	}
	
	private static String getName() {
		try {
			Properties properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			properties.load(classLoader.getResourceAsStream("config.properties"));
			
			return properties.getProperty(PROP_KEY);
			
		} catch (IOException e) {
			String error = "Error trying to retrieve value for property '%s'";
			throw new RuntimeException(String.format(error, PROP_KEY), e);
		}
	}

}
