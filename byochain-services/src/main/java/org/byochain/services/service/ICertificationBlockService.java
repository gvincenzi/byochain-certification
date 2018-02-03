package org.byochain.services.service;

import java.util.Calendar;

import org.byochain.model.entity.Block;
import org.byochain.model.entity.User;
import org.byochain.services.exception.ByoChainServiceException;

public interface ICertificationBlockService extends IBlockService {
	/**
	 * Method to get the temporary code (daily) for a Block
	 * 
	 * @param block Block
	 * @return String
	 */
	String getTemporaryToken(Block block);
	
	/**
	 * Method to add a referer to a block
	 * @param referer String referer URL
	 * @param block Block to update
	 * @return Block updated
	 * @throws ByoChainServiceException
	 */
	public Block addReferer(String referer, Block block) throws ByoChainServiceException;

	/**
	 * Method to remove a referer to a block
	 * @param referer String referer URL
	 * @param block Block to update
	 * @return Block updated
	 * @throws ByoChainServiceException
	 */
	Block removeReferer(String referer, Block block) throws ByoChainServiceException;
	
	/**
	 * Add a block to the BlockChain
	 * @param name Content of the block
	 * @param expirationDate Expiration date of Certification
	 * @param miner Miner user
	 * @param logo URL of certification logo
	 * @return Block mined but not yet validated
	 * @throws ByoChainServiceException
	 */
	Block addCertificationBlock(User miner, String name, Calendar expirationDate, String logo) throws ByoChainServiceException;
}
