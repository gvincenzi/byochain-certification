package org.byochain.services.service.impl.base;

import java.util.ArrayList;
import java.util.List;

import org.byochain.commons.utils.BlockchainUtils;
import org.byochain.model.entity.Block;
import org.byochain.services.service.impl.BlockService;
import org.springframework.stereotype.Service;

/**
 * Base implementation for {@link BlockService} with a simple mining algorithm and an hash calculation based on SHA-256
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Service
public class BaseBlockService extends BlockService {
	private static final String REGEX_DIGIT = "[0-9].*";
	
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
		String calculatedhash = BlockchainUtils.applySha256( 
				block.getPreviousHash() +
				Long.toString(block.getTimestamp().getTimeInMillis()) +
				Integer.toString(block.getNonce()) +
				block.getData() 
				);
		return calculatedhash;
	}

}
