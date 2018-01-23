package org.byochain.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ByoChainApiResponse: API REST Standard response
 * @author Giuseppe Vincenzi
 *
 */
@JsonInclude(Include.NON_NULL)
public class ByoChainApiResponse {
	private String message;
	private Integer code;
	private Object data;
	
	public ByoChainApiResponse(Integer code, String message){
		setCode(code);
		setMessage(message);
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
