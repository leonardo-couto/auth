package com.bewkrop.auth.user;

public interface Credential {

	/**
	 * Checks a credential
	 * @param Credential
	 * @return
	 */
	public boolean check(char[] password);
	
}
