package org.byochain.model.repository.test;

import java.util.HashSet;
import java.util.Set;

import org.byochain.model.AppModel;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockData;
import org.byochain.model.entity.BlockReferer;
import org.byochain.model.entity.User;
import org.byochain.model.repository.BlockDataRepository;
import org.byochain.model.repository.BlockRefererRepository;
import org.byochain.model.repository.BlockRepository;
import org.byochain.model.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * JUnit Test
 * 
 * @author Giuseppe Vincenzi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AppModel.class })
@ActiveProfiles("test")
public class BlockRefererRepositoryTest {
	@Autowired
	private BlockRepository blockRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlockRefererRepository serviceUnderTest;
	
	@Autowired
	private BlockDataRepository blockDataRepository;

	private static Set<Block> blocks = new HashSet<>();
	private static Set<BlockReferer> blockReferers = new HashSet<>();
	private static Set<BlockData> blockDatas = new HashSet<>();

	@Before
	public void init() {
		blocks.clear();
		blockReferers.clear();
		User miner = userRepository.save(getUserMock());
		BlockData blockData = new BlockData();
		blockData.setData("TEST");
		
		BlockReferer blockReferer = new BlockReferer();
		blockReferer.setReferer("127.0.0.1");

		BlockReferer blockReferer2 = new BlockReferer();
		blockReferer2.setReferer("192.168.0.1");

		Block block = new Block(blockData, "GENESIS", miner);
		block.setHash("HASH");

		block.addReferer(blockReferer);
		block.addReferer(blockReferer2);

		blockDatas.add(blockData);
		blocks.add(block);
		blockReferers.add(blockReferer);
		blockReferers.add(blockReferer2);
	}

	@Test
	@Transactional
	public void count() {
		serviceUnderTest.deleteAll();
		blockRepository.deleteAll();
		blockDataRepository.deleteAll();
		blockDataRepository.save(blockDatas);
		blockRepository.save(blocks);
		Assert.assertEquals(blockReferers.size(), serviceUnderTest.count());
	}

	@Test
	@Transactional
	public void remove() {
		serviceUnderTest.deleteAll();
		blockRepository.deleteAll();
		blockDataRepository.deleteAll();
		blockDataRepository.save(blockDatas);
		blockRepository.save(blocks);
		String hash = blocks.iterator().next().getHash();
		Block block = blockRepository.find(hash);
		BlockReferer blockReferer = block.getReferers().iterator().next();
		block.removeReferer(blockReferer);
		blockRepository.save(block);

		Block blockFound = blockRepository.find(hash);
		Assert.assertEquals(1, blockFound.getReferers().size());
		Assert.assertEquals(1, serviceUnderTest.count());
	}

	@Test
	@Transactional
	public void save() {
		serviceUnderTest.deleteAll();
		blockRepository.deleteAll();
		blockDataRepository.deleteAll();
		blockDataRepository.save(blockDatas);
		blockRepository.save(blocks);
		for (Block block : blocks) {
			Block blockFound = blockRepository.find(block.getHash());
			Assert.assertEquals(block.getReferers().size(), blockFound.getReferers().size());
			for (BlockReferer blockReferer : blockFound.getReferers()) {
				Assert.assertEquals(block.getHash(), blockReferer.getOwner().getHash());
			}
		}
	}
	
	private User getUserMock(){
		User user = new User();
		user.setUserId(10L);
		return user;
	}
}
