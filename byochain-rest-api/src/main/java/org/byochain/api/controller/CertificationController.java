package org.byochain.api.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.enumeration.ByoChainApiResponseEnum;
import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.request.BlockDataUpdateRequest;
import org.byochain.api.request.BlockRefererAddRequest;
import org.byochain.api.request.BlockRefererRemoveRequest;
import org.byochain.api.request.CertificationFastCreationRequest;
import org.byochain.api.response.ByoChainApiResponse;
import org.byochain.commons.exception.ByoChainException;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockReferer;
import org.byochain.model.entity.User;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.ICertificateGenerator;
import org.byochain.services.service.ICertificationBlockService;
import org.byochain.services.service.impl.CertificationBlockService;
import org.byochain.services.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * BlockController : A REST controller containing all possible operations about BYOChain Certifications.
 * All users have access to this list of APIs
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Api(value = "/api/v1/certifications", produces = "application/json")
@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationController {
	private static final String STAR = "*";

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	@Qualifier("certificationBlockService")
	private ICertificationBlockService blockService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ICertificateGenerator certificateGenerator;
	
	/**
	 * This service check the block by its hash and temporary token
	 * @param hash String containing the hash to search in database
	 * @param token String containing the temporary token
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Validate chain and check block by hash and temporary token",
		    notes = "This service checks a block by its hash and temporary token")
	@RequestMapping(value = "/check/{hash}/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse checkChainAndBlock(@PathVariable("hash") String hash, @PathVariable("token") String token, Locale locale, Authentication authentication)
			throws ByoChainException {	
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = getAuthenticatedUserID(locale, userDetails);
		
		Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
		ByoChainApiResponse response = null;
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		} 

		if (!blockService.validateChain(blockService.getAllBlocks(), user)) {
			response = ByoChainApiResponseEnum.BLOCK_CONTROLLER_VALIDATION_KO.getResponse(messageSource, locale, block.getData().getData());
		} else if (!block.getValidated()) {
			response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_VALIDITY.getResponse(messageSource, locale, block.getData().getData());
		} else if (block.getData().getExpirationDate()!=null && block.getData().getExpirationDate().before(Calendar.getInstance(locale))) {
			response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_EXPIRATION.getResponse(messageSource, locale, block.getData().getData());
		} else if (!block.getData().getEnabled()) {
			response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_ENABLED.getResponse(messageSource, locale, block.getData().getData());
		} else {
			String tokenToValidate = ((CertificationBlockService)blockService).getTemporaryToken(block);
			
			if (tokenToValidate.equals(token)) {
				response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_OK.getResponse(messageSource, locale, block.getData().getData());
			} else {
				response = ByoChainApiResponseEnum.CERTIFICATIONS_CONTROLLER_CHECK_KO.getResponse(messageSource, locale, block.getData().getData());
			}
		}
		response.setData(block.getData());
		return response;
	}
	
	/**
	 * This service enable a block by its hash
	 * @param hash String containing the hash to search in database
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Update block data",
		    notes = "This service checks a block by its hash and temporary token")
	@RequestMapping(value = "/admin/{hash}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ByoChainApiResponse updateBlockData(@PathVariable("hash") String hash, @RequestBody BlockDataUpdateRequest request, Locale locale)
			throws ByoChainException {	
		Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		}
		
		if (request == null || request.getEnabled() == null || request.getLogo() == null || request.getExpirationDate() == null) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_BLOCKDATA_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(request.getExpirationDate());

		block.getData().setEnabled(request.getEnabled());
		block.getData().setExpirationDate(calendar);
		block.getData().setLogo(request.getLogo());
		
		blockService.updateBlock(block);
		
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(block);
		return response;
	}
	
	/**
	 * This service enable a block by its hash
	 * @param hash String containing the hash to search in database
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Get a Certificate (PDF file) of the blockchain inscription",
		    notes = "This service creates a Certificate (PDF file) of the blockchain inscription with all Block Datas")
	@RequestMapping(value = "/admin/certificate/{hash}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> getInscriptionCertificate(@PathVariable("hash") String hash, Locale locale)
			throws ByoChainException {
		Block block = ((CertificationBlockService)blockService).getBlockByHash(hash);
		if (block == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		}
		
	    return createCertificate(block);
	}

	/**
	 * Internal method to create a Certificate of inscription to the blockchain (PDF file)
	 * @param block Block
	 * @return byte[] PDF file
	 * @throws ByoChainException
	 */
	private ResponseEntity<byte[]> createCertificate(Block block) throws ByoChainException {
		byte[] createCertificate = null;
		try{
			createCertificate = certificateGenerator.createCertificate(block);
		}catch(IOException e){
			throw new ByoChainException(e.getMessage());
		}
		
		String filename = block.getData().getData() + "_certificate.pdf";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	    headers.setContentDispositionFormData(filename, filename);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(createCertificate, headers, HttpStatus.OK);
		
		return responseEntity;
	}
	
	/**
	 * This service will be used for a "fast creation" of a new Certification
	 * @param request CertificationFastCreationResponse
	 * @param locale
	 * @return
	 * @throws ByoChainException 
	 */
	@ApiOperation(value = "Fast inscription in byochain",
		    notes = "This service creates a new user, realize a bolck mining and then adds a referer")
	@RequestMapping(value = "/fast", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<byte[]> fastCertification(@RequestBody CertificationFastCreationRequest request, Locale locale) throws ByoChainException{
		if (request == null || request.getUsername() == null || request.getUsername().isEmpty()) {
			throw ByoChainApiExceptionEnum.ADMIN_CONTROLLER_USER_DATA_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		
		if (request.getName() == null || request.getName().isEmpty() || request.getLogo() == null || request.getExpirationDate() == null) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_DATA_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		
		if (request.getReferer() == null || request.getReferer().isEmpty()) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_REFERER_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user = userService.addUser(user);
		
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(request.getExpirationDate());
		
		Block block = blockService.addCertificationBlock(user, request.getName(), calendar, request.getLogo());
		block = blockService.addReferer(request.getReferer(), block);
		
		return createCertificate(block);
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
			String refererString = iterator.next().getReferer();
			refererIsValid = refererString.endsWith(STAR) ? referer.toLowerCase().startsWith(refererString.toLowerCase()) : referer.equalsIgnoreCase(referer);
		}
		
		if(!refererIsValid){
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_CHECK_REFERER.getExceptionAfterServiceCall(messageSource, locale, referer, block.getData().getData());
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
	@RequestMapping(value = "/admin/referers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@ApiOperation(value = "Add a referer to a block",
		    notes = "This service removes a referer to a block")
	@RequestMapping(value = "/admin/referers", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	/**
	 * Private method to get the {@link User} from database by {@link UserDetails} informations (in {@link Authentication} object)
	 * @param locale Locale object (by framework)
	 * @param userDetails {@link UserDetails} informations (in {@link Authentication} object)
	 * @return {@link User}
	 * @throws {@link ByoChainServiceException}
	 * @throws {@link ByoChainApiException}
	 */
	private User getAuthenticatedUserID(Locale locale, UserDetails userDetails)
			throws ByoChainServiceException, ByoChainApiException {
		User user = userService.getUser(userDetails.getUsername(), userDetails.getPassword());
		if (user == null) {
			throw ByoChainApiExceptionEnum.CERTIFICATIONS_CONTROLLER_USER_DETAILS_INVALID.getExceptionBeforeServiceCall(messageSource, locale, userDetails.getUsername());
		}
		return user;
	}
	
}
