package org.byochain.services.service;

import org.byochain.model.entity.Block;
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
}
