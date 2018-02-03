package org.byochain.api.request;

import java.util.Date;

/**
 * BlockCreationRequest used for API Service "POST /api/v1/blocks"
 * @author Giuseppe Vincenzi
 *
 */
public class BlockCreationRequest {
	/**
	 * Name
	 */
	private String name;
	
	/**
	 * expirationDate
	 */
	private Date expirationDate;
	
	/**
	 * logo
	 */
	private String logo;

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
