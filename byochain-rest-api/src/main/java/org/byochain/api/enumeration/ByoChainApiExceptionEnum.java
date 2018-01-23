package org.byochain.api.enumeration;

import java.util.Locale;

import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.exception.ByoChainApiServiceException;
import org.springframework.context.MessageSource;

/**
 * This class is used to obtain a {@link ByoChainApiException} or {@link ByoChainApiServiceException} with the right code/message inside,
 * retrieved in file messages.properties (with internationalization)
 * @author Giuseppe Vincenzi
 *
 */
public enum ByoChainApiExceptionEnum {
	BLOCK_CONTROLLER_DATA_MANDATORY("block.controller.data.mandatory"),
	BLOCK_CONTROLLER_HASH_NOT_EXIST("block.controller.byhash.ko"),
	BLOCK_CONTROLLER_VALIDATION_KO("block.controller.validation.ko"),
	BLOCK_CONTROLLER_SERVICE_ERROR("block.controller.service.error"),
	
	ADMIN_CONTROLLER_USER_ENABLING_MANDATORY("admin.controller.user.enabling.mandatory"),
	ADMIN_CONTROLLER_USER_DATA_MANDATORY("admin.controller.user.data.mandatory"),
	ADMIN_CONTROLLER_USER_DETAILS_INVALID("admin.controller.user.details.invalid"),
	CERTIFICATIONS_CONTROLLER_CHECK_REFERER("certifications.controller.check.referer"),
	CERTIFICATIONS_CONTROLLER_CHECK_VALIDITY("certifications.controller.check.validity"),
	CERTIFICATIONS_CONTROLLER_REFERER_MANDATORY("certifications.controller.referer.mandatory");

	private static final String MESSAGE = ".message";
	private static final String CODE = ".code";
	private String key;

	ByoChainApiExceptionEnum(String key) {
		this.key = key;
	}

	public ByoChainApiException getExceptionBeforeServiceCall(MessageSource messageSource,
			Locale locale, Object... args) {
		ByoChainApiException apiException = new ByoChainApiException(
				Integer.parseInt(messageSource.getMessage(this.key + CODE, null, locale)),
				messageSource.getMessage(this.key + MESSAGE, args, locale));
		return apiException;
	}
	
	public ByoChainApiServiceException getExceptionAfterServiceCall(MessageSource messageSource,
			Locale locale, Object... args) {
		ByoChainApiServiceException apiException = new ByoChainApiServiceException(
				Integer.parseInt(messageSource.getMessage(this.key + CODE, null, locale)),
				messageSource.getMessage(this.key + MESSAGE, args, locale));
		return apiException;
	}
}
