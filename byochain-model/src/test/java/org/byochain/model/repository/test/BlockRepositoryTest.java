package org.byochain.model.repository.test;

import java.util.LinkedHashSet;
import java.util.Set;

import org.byochain.model.AppModel;
import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockData;
import org.byochain.model.entity.User;
import org.byochain.model.repository.BlockDataRepository;
import org.byochain.model.repository.BlockRepository;
import org.byochain.model.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * JUnit Test
 * 
 * @author Giuseppe Vincenzi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AppModel.class })
@ActiveProfiles("test")
public class BlockRepositoryTest {
	@Autowired
	private BlockRepository serviceUnderTest;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BlockDataRepository blockDataRepository;

	private Set<Block> blocks = new LinkedHashSet<>();

	@Before
	public void init() {
		serviceUnderTest.deleteAll();
		userRepository.deleteAll();
		User miner = userRepository.save(getUserMock());

		BlockData blockData = new BlockData();
		blockData.setData("Genesis block");
		BlockData blockData2 = new BlockData();
		blockData2.setData("Block#2");
		BlockData blockData3 = new BlockData();
		blockData3.setData("Block#3");
		
		blockDataRepository.save(blockData);
		blockDataRepository.save(blockData2);
		blockDataRepository.save(blockData3);
		
		Block block1 = new Block(blockData, "0", miner);
		block1.setHash("HASH1");
		
		
		Block block2 = new Block(blockData2, block1.getHash(), miner);
		block2.setHash("HASH2");
		
		Block block3 = new Block(blockData3, block2.getHash(), miner);
		block3.setHash("HASH3");

		blocks.add(block1);
		blocks.add(block2);
		blocks.add(block3);

		serviceUnderTest.save(blocks);
	}
	
	@After
	public void after() {
		serviceUnderTest.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void count() {
		Assert.assertEquals(blocks.size(), serviceUnderTest.count());
	}

	@Test
	public void findById() {
		for (Block block : blocks) {
			Assert.assertEquals(block, serviceUnderTest.findOne(block.getId()));
		}

	}

	@Test
	public void findByHash() {
		for (Block block : blocks) {
			Assert.assertEquals(block, serviceUnderTest.find(block.getHash()));
		}
	}

	@Test
	public void findLast() {
		Assert.assertEquals("HASH3", serviceUnderTest.findLast().getHash());
	}
	
	private User getUserMock(){
		User user = new User();
		user.setUserId(10L);
		return user;
	}
}
