package org.byochain.api.request;

/**
 * UserCreationRequest used for API Service "POST /api/v1/admin/users"
 * @author Giuseppe Vincenzi
 *
 */
public class UserCreationRequest {
	private String username;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
