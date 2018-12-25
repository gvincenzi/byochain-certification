package org.byochain.api.enumeration;

import java.util.Locale;

import org.byochain.api.response.ByoChainApiResponse;
import org.springframework.context.MessageSource;

/**
 * This class is used to obtain a {@link ByoChainApiResponse} with the right code/message inside,
 * retrieved in file messages.properties (with internationalization)
 * @author Giuseppe Vincenzi
 *
 */
public enum ByoChainApiResponseEnum {
	BLOCK_CONTROLLER_VALIDATION_OK("block.controller.validation.ok"),
	BLOCK_CONTROLLER_VALIDATION_KO("block.controller.validation.ko"),
	CERTIFICATIONS_CONTROLLER_CHECK_OK("certifications.controller.check.ok"),
	CERTIFICATIONS_CONTROLLER_CHECK_KO("certifications.controller.check.ko"),
	CERTIFICATIONS_CONTROLLER_CHECK_VALIDITY("certifications.controller.check.validity"),
	CERTIFICATIONS_CONTROLLER_CHECK_EXPIRATION("certifications.controller.check.expiration"),
	CERTIFICATIONS_CONTROLLER_CHECK_ENABLED("certifications.controller.check.enabled"),
	CONTROLLER_OK("controller.ok");

	private static final String MESSAGE = ".message";
	private static final String CODE = ".code";
	private String key;

	ByoChainApiResponseEnum(String key) {
		this.key = key;
	}

	public ByoChainApiResponse getResponse(MessageSource messageSource,
			Locale locale, Object... args) {
		ByoChainApiResponse apiResponse = new ByoChainApiResponse(
				Integer.parseInt(messageSource.getMessage(this.key + CODE, null, locale)),
				messageSource.getMessage(this.key + MESSAGE, args, locale));
		return apiResponse;
	}
}
