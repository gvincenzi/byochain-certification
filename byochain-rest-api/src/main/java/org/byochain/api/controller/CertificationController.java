package org.byochain.api.controller;

import java.util.Iterator;
import java.util.Locale;

import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.enumeration.ByoChainApiResponseEnum;
import org.byochain.api.request.BlockRefererAddRequest;
import org.byochain.api.request.BlockRefererRemoveRequest;
import org.byochain.api.response.ByoChainApiResponse;
import org.byochain.commons.exception.ByoChainException;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockReferer;
import org.byochain.services.service.ICertificationBlockService;
import org.byochain.services.service.impl.CertificationBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * BlockController : A REST controller containing all possible operations about BYOChain Certifications.
 * All users have access to this list of APIs
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Api(value = "/api/v1/certifications", produces = "application/jsonp")
@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationController {
	@Autowired
	protected MessageSource messageSource;

	@Autowired
	@Qualifier("certificationBlockService")
	private ICertificationBlockService blockService;
	
	/**
	 * This service check the block by its hash and temporary token
	 * @param hash String containing the hash to search in database
	 * @param token String containing the temporary token
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Check block by hash and temporary token",
		    notes = "This service checks a block by its hash and temporary token")
	@RequestMapping(value = "/check/{hash}/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse checkBlock(@PathVariable("hash") String hash, @PathVariable("token") String token, Locale locale)
			throws ByoChainException {	
		Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
		ByoChainApiResponse response = null;
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		}
		
		if (!block.getValidated()) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_CHECK_VALIDITY.getExceptionAfterServiceCall(messageSource, locale, block.getData());
		}
		
		String tokenToValidate = ((CertificationBlockService)blockService).getTemporaryToken(block);
		
		Boolean validation = tokenToValidate.equals(token);
		if (validation) {
			response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_OK.getResponse(messageSource, locale, block.getData());
		} else {
			response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_KO.getResponse(messageSource, locale, block.getData());
		}
		return response;
	}
	
	/**
	 * This administration service retrieves temporary token for a number of days
	 * @param hash String containing the hash to search in database
	 * @param token String containing the temporary token
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Temporary token for a number of days",
		    notes = "This service retrieves all temporary tokens for a number of days")
	@RequestMapping(value = "/token/{hash}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse temporaryTokenInit(@PathVariable("hash") String hash, Locale locale, @RequestHeader(value = "referer", required = true) final String referer) throws ByoChainException {
		Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		}

		Boolean refererIsValid = false;
		Iterator<BlockReferer> iterator = block.getReferers().iterator();
		while(!refererIsValid && iterator.hasNext()){
			refererIsValid = iterator.next().getReferer().equalsIgnoreCase(referer);
		}
		
		if(!refererIsValid){
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_CHECK_REFERER.getExceptionAfterServiceCall(messageSource, locale, referer, block.getData());
		}
		
		String token = ((CertificationBlockService)blockService).getTemporaryToken(block);
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(token);
		return response;
	}
	
	/**
	 * This administration service to add a referer to a block
	 * @param hash String containing the hash to search in database
	 * @param referer String containing the URL of the referer to add
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Add a referer to a block",
		    notes = "This service adds a referer to a block")
	@RequestMapping(value = "/referers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse addReferer(@RequestBody BlockRefererAddRequest request, Locale locale) throws ByoChainException {
		if (request == null || request.getHash() == null || request.getHash().isEmpty() || request.getReferer() == null || request.getReferer().isEmpty()) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_REFERER_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		Block block = blockService.getBlockByHash(request.getHash());
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, request.getHash());
		}

		Block blockUpdated = blockService.addReferer(request.getReferer(), block);
		
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(blockUpdated);
		return response;
	}
	
	/**
	 * This administration service to remove a referer to a block
	 * @param hash String containing the hash to search in database
	 * @param referer String containing the URL of the referer to add
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Add a remove to a block",
		    notes = "This service removes a referer to a block")
	@RequestMapping(value = "/referers", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse removeReferer(@RequestBody BlockRefererRemoveRequest request, Locale locale) throws ByoChainException {
		if (request == null || request.getHash() == null || request.getHash().isEmpty() || request.getReferer() == null || request.getReferer().isEmpty()) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_REFERER_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		Block block = blockService.getBlockByHash(request.getHash());
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, request.getHash());
		}

		Block blockUpdated = blockService.removeReferer(request.getReferer(), block);
		
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(blockUpdated);
		return response;
	}
	
}
