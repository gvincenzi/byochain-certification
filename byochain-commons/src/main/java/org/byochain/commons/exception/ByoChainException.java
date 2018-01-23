package org.byochain.commons.exception;

/**
 * ByoChainException : base {@link Exception} used in all BeYourChain project modules
 * @author Giuseppe Vincenzi
 *
 */
public class ByoChainException extends Exception{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7762665561495822428L;

	/**
	 * Default constructor
	 */
	public ByoChainException(){
		super();
	}
	
	/**
	 * Constructor
	 * @param message String containing error message
	 */
	public ByoChainException(String message){
		super(message);
	}
	
	/**
	 * Constructor
	 * @param message String containing error message
	 * @param e Source exception
	 */
	public ByoChainException(String message, Throwable e){
		super(message,e);
	}
}
