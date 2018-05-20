package org.byochain.services.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockData;
import org.byochain.model.entity.User;
import org.byochain.model.repository.BlockRepository;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.IBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of {@link IBlockService}
 * 
 * @author Giuseppe Vincenzi
 *
 */
public abstract class BlockService implements IBlockService {
	/**
	 * GENESIS
	 */
	private static final String GENESIS = "GENESIS";

	/**
	 * BlockRepository
	 */
	@Autowired
	private BlockRepository blockRepository;

	/**
	 * Level of hash algorithm difficulty
	 */
	@Value("${difficult.level}")
	private Integer difficultLevel;
	
	/**
	 * Number of validations required to validate a block
	 */
	@Value("${required.validation.number}")
	private Integer requiredValidationNumber;

	/**
	 * Abstract method to verify is the hash is resolved by a Block
	 * @param block
	 * @param difficultLevel
	 * @return boolean true if resolved
	 */
	public abstract boolean isHashResolved(Block block, Integer difficultLevel);
	
	/**
	 * Abstract method to calculate an hash for a Block
	 * @param previousHash String
	 * @param timestamp Long
	 * @param nonce Integer
	 * @param data String
	 * @return Hash String
	 */
	public abstract String calculateHash(Block block);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Block> getAllBlocks() {
		Set<Block> blocks = new TreeSet<>();
		for (Block block : blockRepository.findAll()) {
			block.setValidated(block.getValidators().size()==requiredValidationNumber);
			blocks.add(block);
		}

		return blocks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Block> getBlocks(Pageable pageable) {
		Page<Block> blocks = blockRepository.findAll(pageable);
		
		for (Block block : blocks) {
			block.setValidated(block.getValidators().size()==requiredValidationNumber);
		}
		
		return blocks;
	}

	/**
	 * Method to mine a block.
	 * @param data Content of the block
	 * @param previousBlock Previous Block in the BlockChain
	 * @param miner User
	 * @return Block mined but not yet validated
	 * @throws ByoChainServiceException
	 */
	protected Block mineBlock(BlockData blockData, Block previousBlock, User miner) throws ByoChainServiceException {
		if (blockData == null || miner == null) {
			throw new ByoChainServiceException("Data and Miner are mandatory");
		}
		Block block = new Block(blockData, previousBlock != null ? previousBlock.getHash() : GENESIS, miner);
		
		Random random = new Random(block.getTimestamp().getTimeInMillis());
		int nonce = Math.abs(random.nextInt());
		block.setNonce(nonce);
		block.setHash(calculateHash(block));
		while (!isHashResolved(block, difficultLevel)) {
			nonce = Math.abs(random.nextInt());
			block.setNonce(nonce);
			block.setHash(calculateHash(block));
		}

		block.setMiner(miner);
		return block;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean validateChain(Iterable<Block> blockchain, User validator) throws ByoChainServiceException {
		if (blockchain == null) {
			throw new ByoChainServiceException("Iterable blockchain object is mandatory");
		}
		Block currentBlock;
		Block previousBlock;

		List<Block> blocks = new ArrayList<>();

		blockchain.forEach(block -> blocks.add(block));
		Collections.sort(blocks);

		Boolean result = true;
		for (int i = 0; i < blocks.size(); i++) {
			previousBlock = i>0?blocks.get(i - 1):null;
			currentBlock = blocks.get(i);
			if (!currentBlock.getHash().equals(calculateHash(currentBlock))) {
				result = false;
			}
			if (previousBlock!=null && !previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				result = false;
			}
			if (!isHashResolved(currentBlock, difficultLevel)) {
				result = false;
			}
			
			if(currentBlock.getValidators().size()<requiredValidationNumber && !currentBlock.getMiner().equals(validator) && !currentBlock.getValidators().contains(validator)){
				currentBlock.getValidators().add(validator);
				blockRepository.save(currentBlock);
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block getBlockByHash(String hash) throws ByoChainServiceException {
		if (hash == null || hash.isEmpty()) {
			throw new ByoChainServiceException("Hash is mandatory");
		}
		Block block = blockRepository.find(hash);
		if (block != null) {
			block.setValidated(block.getValidators().size()==requiredValidationNumber);
		}
		return block;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block addBlock(BlockData data, User user) throws ByoChainServiceException {
		if (data == null || data.getBlockDataId() == null) {
			throw new ByoChainServiceException("Data (persisted object) is mandatory");
		}
		Block previousBlock = blockRepository.findLast();
		Block newBlock = mineBlock(data, previousBlock, user);
		return blockRepository.save(newBlock);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block updateBlock(Block block) throws ByoChainServiceException {
		if (block == null) {
			throw new ByoChainServiceException("Data is mandatory");
		}
		block.getValidators().clear();
		return blockRepository.save(block);
	}
}
