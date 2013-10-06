package com.bewkrop.auth.user;

import java.io.IOException;
import java.util.Properties;

public class UserServiceFactory {
	
	private static final String PROP_KEY = "user.service.class";
	private static UserServiceFactory factory = new UserServiceFactory();
	
	private final UserService service;
	
	private UserServiceFactory() {
		this.service = getInstance();
	}
	
	public UserService build() {
		return this.service;
	}
	
	public static UserServiceFactory instance() {
		return factory;
	}
	
	private static UserService getInstance() {
		String name = getName();
		
		try {
			ClassLoader classLoader = UserServiceFactory.class.getClassLoader();
			Class<?> clazz = classLoader.loadClass(name);		
			return (UserService) clazz.newInstance();
			
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
