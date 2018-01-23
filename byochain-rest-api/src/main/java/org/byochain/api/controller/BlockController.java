package org.byochain.api.controller;

import java.util.Locale;

import org.byochain.api.enumeration.ByoChainApiExceptionEnum;
import org.byochain.api.enumeration.ByoChainApiResponseEnum;
import org.byochain.api.exception.ByoChainApiException;
import org.byochain.api.request.BlockCreationRequest;
import org.byochain.api.response.ByoChainApiResponse;
import org.byochain.commons.exception.ByoChainException;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.User;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.ICertificationBlockService;
import org.byochain.services.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * BlockController : A REST controller containing all possible operations on BlockChain.
 * All users have access to this list of APIs
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Api(value = "/api/v1/blocks", produces = "application/json")
@RestController
@RequestMapping("/api/v1/blocks")
public class BlockController {
	@Autowired
	protected MessageSource messageSource;

	@Autowired
	@Qualifier("certificationBlockService")
	private ICertificationBlockService blockService;
	
	@Autowired
	private UserService userService;

	/**
	 * This service returns the list of blocks with pagination
	 * @param pageable Pageable object (by framework)
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 */
	@ApiOperation(value = "Get block list",
	    notes = "This service returns the list of blocks with pagination")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ByoChainApiResponse blocks(Pageable pageable, Locale locale) {
		Page<Block> blocks = blockService.getBlocks(pageable);
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(blocks);
		return response;
	}

	/**
	 * This service returns the block by its hash
	 * @param hash String containing the hash to search in database
	 * @param locale Locale object (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Get block by hash",
		    notes = "This service returns the block by its hash")
	@RequestMapping(value = "/{hash}", method = RequestMethod.GET)
	public ByoChainApiResponse blockByHash(@PathVariable("hash") String hash, Locale locale)
			throws ByoChainException {
		Block block = blockService.getBlockByHash(hash);
		ByoChainApiResponse response = null;
		if (block != null) {
			response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource,
					locale);
			response.setData(block);
		} else {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_HASH_NOT_EXIST.getExceptionAfterServiceCall(messageSource, locale, hash);
		}
		return response;
	}

	/**
	 * This service creates a new block.
	 * The block mined must be validated by different users than the miner.
	 * @param request {@link BlockCreationRequest} Input object in request body
	 * @param locale Locale object (by framework)
	 * @param authentication Authentication object containing the user authenticated by Basic Auth (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@ApiOperation(value = "Create a new block by mining it",
		    notes = "This service creates a new block. The block mined must be validated by different users than the miner.")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ByoChainApiResponse addBlock(@RequestBody BlockCreationRequest request, Locale locale, Authentication authentication)
			throws ByoChainException {
		if (request == null || request.getData() == null || request.getData().isEmpty()) {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_DATA_MANDATORY.getExceptionBeforeServiceCall(messageSource, locale);
		}
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = getAuthenticatedUserID(locale, userDetails);
		
		Block block = blockService.addBlock(request.getData(), user);
		ByoChainApiResponse response = ByoChainApiResponseEnum.CONTROLLER_OK.getResponse(messageSource, locale);
		response.setData(block);
		return response;
	}

	/**
	 * This service validate the BlockChain.
	 * If a block is not yet validated and the authenticated user is not its miner, current user will be added as validator of the block.
	 * @param locale Locale object (by framework)
	 * @param authentication Authentication object containing the user authenticated by Basic Auth (by framework)
	 * @return {@link ByoChainApiResponse}
	 * @throws {@link ByoChainException}
	 */
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public ByoChainApiResponse validate(Locale locale, Authentication authentication) throws ByoChainException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User user = getAuthenticatedUserID(locale, userDetails);
		
		Boolean validation = blockService.validateChain(blockService.getAllBlocks(), user);
		ByoChainApiResponse response = null;
		if (validation) {
			response = ByoChainApiResponseEnum.BLOCK_CONTROLLER_VALIDATION_OK.getResponse(messageSource, locale);
		} else {
			throw ByoChainApiExceptionEnum.BLOCK_CONTROLLER_VALIDATION_KO.getExceptionAfterServiceCall(messageSource, locale);
		}
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
			throw ByoChainApiExceptionEnum.ADMIN_CONTROLLER_USER_DETAILS_INVALID.getExceptionBeforeServiceCall(messageSource, locale, userDetails.getUsername());
		}
		return user;
	}
}
