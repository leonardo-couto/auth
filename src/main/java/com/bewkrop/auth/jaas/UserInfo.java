package com.bewkrop.auth.jaas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bewkrop.auth.user.Credential;

public class UserInfo {

	private final String name;
	private final Credential credential;
	private final List<String> roles;

	public UserInfo(String name, Credential credential, List<String> roles) {
		this.name = name;
		this.credential = credential;
		this.roles = (roles == null) ? Collections.<String>emptyList() : new ArrayList<String>(roles);
	}

	public String getUserName() {
		return this.name;
	}

	public List<String> getRoleNames() {
		return new ArrayList<String>(this.roles);
	}

	public boolean checkCredential(char[] password) {
		return this.credential.check(password);
	}

	protected Credential getCredential() {
		return this.credential;
	}

}