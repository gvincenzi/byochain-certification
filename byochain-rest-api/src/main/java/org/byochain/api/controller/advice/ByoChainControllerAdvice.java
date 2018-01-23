package org.byochain.api.controller.advice;

import java.util.Locale;

import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.exception.ByoChainApiServiceException;
import org.byochain.services.exception.ByoChainServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ExceptionHandlers specific to API REST module exceptions
 * @author Giuseppe Vincenzi
 *
 */
@RestControllerAdvice
public class ByoChainControllerAdvice {
	@Autowired
	protected MessageSource messageSource;
	
	@ExceptionHandler({ ByoChainApiException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ByoChainApiException handleException(ByoChainApiException ex) {
		return ex;
    }
	
	@ExceptionHandler({ ByoChainApiServiceException.class })
	@ResponseStatus(value = HttpStatus.OK)
    public ByoChainApiServiceException handleException(ByoChainApiServiceException ex) {
		return ex;
    }
	
	@ExceptionHandler({ ByoChainServiceException.class })
	@ResponseStatus(value = HttpStatus.OK)
    public ByoChainApiException handleServiceException(ByoChainServiceException ex, Locale locale) {
		return ByoChainApiExceptionEnum.BLOCK_CONTROLLER_SERVICE_ERROR.getExceptionBeforeServiceCall(messageSource, locale, ex.getMessage());
    }
}
