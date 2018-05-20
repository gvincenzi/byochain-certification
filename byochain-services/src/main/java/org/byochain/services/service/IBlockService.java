package org.byochain.services.service;

import java.util.Set;

import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockData;
import org.byochain.model.entity.User;
import org.byochain.services.exception.ByoChainServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface of service related to {@link Block} model entity
 * @author Giuseppe Vincenzi
 *
 */
public interface IBlockService {

	/**
	 * Method to retrieve a Set with all blocks
	 * @return Set<Block> containing all blocks
	 */
	Set<Block> getAllBlocks();
	
	/**
	 * Method to retrieve a collection of all blocks (with pagination)
	 * @param pageable Pageable - Spring Data object
	 * @return Page<Block> containing all blocks paginated
	 */
	Page<Block> getBlocks(Pageable pageable);
	
	/**
	 * Method to get a {@link Block} by its hash
	 * @param hash String
	 * @return Block
	 * @throws ByoChainServiceException
	 */
	Block getBlockByHash(String hash) throws ByoChainServiceException;
	
	/**
	 * Add a block to the BlockChain
	 * @param data Content of the block
	 * @param user Miner user
	 * @return Block mined but not yet validated
	 * @throws ByoChainServiceException
	 */
	Block addBlock(BlockData data, User user) throws ByoChainServiceException;
	
	/**
	 * Update a block in the BlockChain
	 * @param block Block to update
	 * @return Block updated
	 * @throws ByoChainServiceException
	 */
	Block updateBlock(Block block) throws ByoChainServiceException;

	/**
	 * Method to validate the blocks of the BlockChain not yet validated.
	 * The validator user cannot be the miner user of the block to validate.
	 * @param blockchain Iterable<Block> containing the BlockChain
	 * @param validator User
	 * @return Boolean True if totally validated
	 * @throws ByoChainServiceException
	 */
	Boolean validateChain(Iterable<Block> blockchain, User validator) throws ByoChainServiceException;
}
