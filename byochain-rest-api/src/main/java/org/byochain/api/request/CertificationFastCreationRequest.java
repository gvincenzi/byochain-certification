package org.byochain.api.request;

import java.util.Date;

/**
 * CertificationFastCreationResponse used for API Service "POST /api/v1/certifications/admin/fast"
 * @author Giuseppe Vincenzi
 *
 */
public class CertificationFastCreationRequest {
	/**
	 * username
	 */
	private String username;
	
	/**
	 * expirationDate
	 */
	private Date expirationDate;
	
	/**
	 * logo
	 */
	private String logo;
	
	/**
	 * logo
	 */
	private String referer;
	
	/**
	 * Name
	 */
	private String name;

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
