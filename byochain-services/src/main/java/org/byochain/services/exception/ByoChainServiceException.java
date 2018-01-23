package org.byochain.services.exception;

import org.byochain.commons.exception.ByoChainException;

/**
 * ByoChainServiceException : specific extension of {@link ByoChainException}
 * @author Giuseppe Vincenzi
 *
 */
public class ByoChainServiceException extends ByoChainException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3994114088237135634L;

	/**
	 * Constructor
	 * @param message String containing error message
	 */
	public ByoChainServiceException(String message) {
		super(message);
	}
	
}
