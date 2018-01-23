package org.byochain.api.request;

/**
 * BlockRefererAddRequest used for API Service "POST /api/v1/certifications/referers"
 * @author Giuseppe Vincenzi
 *
 */
public class BlockRefererAddRequest {
	private String hash;
	private String referer;
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
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
}
