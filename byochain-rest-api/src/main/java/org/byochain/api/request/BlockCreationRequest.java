package org.byochain.api.request;

/**
 * BlockCreationRequest used for API Service "POST /api/v1/blocks"
 * @author Giuseppe Vincenzi
 *
 */
public class BlockCreationRequest {
	/**
	 * Data
	 */
	private String data;

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
}
