/**
 * 
 */
package org.byochain.services.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.byochain.commons.utils.BlockchainUtils;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockReferer;
import org.byochain.model.repository.BlockRefererRepository;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.ICertificationBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * CertificationBlockService
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Service
@Qualifier("certificationBlockService")
@Primary
public class CertificationBlockService extends BlockService implements ICertificationBlockService {
	/**
	 * Level of hash algorithm difficulty
	 */
	@Value("${difficult.level}")
	private Integer difficultLevel;

	private static final String REGEX_DIGIT = "[0-9].*";
	
	@Autowired
	private BlockRefererRepository blockRefererRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHashResolved(Block block, Integer difficultLevel) {
		List<Integer> digits = new ArrayList<Integer>(difficultLevel);

		Integer index = 0;
		String hash = block.getHash();
		while (index < hash.length() && digits.size() < difficultLevel) {
			String s = hash.substring(index, ++index);
			if (s.matches(REGEX_DIGIT)) {
				digits.add(Integer.parseInt(s));
			}
		}

		Integer sum = digits.parallelStream().reduce(0, Integer::sum);
		return sum % difficultLevel == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String calculateHash(Block block) {
		String calculatedhash = BlockchainUtils
				.applySha256(block.getPreviousHash() + Long.toString(block.getTimestamp().getTimeInMillis())
						+ Integer.toString(block.getNonce()) + block.getData());
		return calculatedhash;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemporaryToken(Block block) {
		LocalDateTime localDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(localDate.format(formatter));
		stringBuffer.append(block.getHash());
		stringBuffer.append(block.getNonce());
		stringBuffer.append(block.getTimestamp());
		return BlockchainUtils.applySha256(stringBuffer.toString()).substring(0, difficultLevel);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block addReferer(String referer, Block block) throws ByoChainServiceException {
		BlockReferer blockReferer = new BlockReferer();
		blockReferer.setReferer(referer);
		block.addReferer(blockReferer);
		return updateBlock(block);
	}

	@Override
	public Block removeReferer(String referer, Block block) throws ByoChainServiceException {
		for (BlockReferer blockReferer : blockRefererRepository.find(block, referer)) {
			block.removeReferer(blockReferer);
		}
		return updateBlock(block);
	}
}
