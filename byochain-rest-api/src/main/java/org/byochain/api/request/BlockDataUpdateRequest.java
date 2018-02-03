package org.byochain.api.request;

import java.util.Date;

/**
 * BlockCreationRequest used for API Service "POST /api/v1/certifications/admin/{hash}"
 * @author Giuseppe Vincenzi
 *
 */
public class BlockDataUpdateRequest {
	/**
	 * expirationDate
	 */
	private Date expirationDate;
	
	/**
	 * logo
	 */
	private String logo;
	
	/**
	 * enabled
	 */
	private Boolean enabled;

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
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
