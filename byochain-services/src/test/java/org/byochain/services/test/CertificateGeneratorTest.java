package org.byochain.services.test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockData;
import org.byochain.model.entity.User;
import org.byochain.model.repository.BlockDataRepository;
import org.byochain.model.repository.BlockRepository;
import org.byochain.services.AppServices;
import org.byochain.services.service.ICertificateGenerator;
import org.byochain.services.service.impl.CertificationBlockService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * JUnit Test
 * 
 * @author Giuseppe Vincenzi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AppServices.class })
@ActiveProfiles("test")
public class CertificateGeneratorTest {
	@Autowired
	ICertificateGenerator serviceUnderTest;

	@Autowired
	CertificationBlockService certificationBlockService;

	@MockBean
	BlockRepository blockRepositoryMock;

	@Autowired
	BlockDataRepository blockDataRepository;

	static BlockData blockDataGenesis;

	@Before
	public void init() {
		blockDataGenesis = new BlockData();
		blockDataGenesis.setData("Giuseppe Vincenzi");
		blockDataGenesis.setExpirationDate(Calendar.getInstance());

		blockDataRepository.save(blockDataGenesis);
		
		Mockito.when(blockRepositoryMock.findAll()).thenReturn(new Iterable<Block>() {

			@Override
			public Iterator<Block> iterator() {
				return new HashSet<Block>().iterator();
			}
		});

		Mockito.when(blockRepositoryMock.save(Matchers.any(Block.class))).then(i -> i.getArgumentAt(0, Block.class));
	}

	@Test
	public void test() throws Exception {
		long now = System.currentTimeMillis();
		Mockito.when(blockRepositoryMock.findLast()).thenReturn(null);
		Block block = certificationBlockService.addBlock(blockDataGenesis, getUserMock());
		System.out.println("Block mined in " + (System.currentTimeMillis() - now) + "ms >> " + block);

		byte[] createCertificate = serviceUnderTest.createCertificate(block);
		Assert.assertNotNull(createCertificate);

//		try (FileOutputStream fos = new FileOutputStream("test1.pdf")) {
//			fos.write(createCertificate);
//		}
	}

	@Test
	public void testAfterFastCreation() throws Exception {
		long now = System.currentTimeMillis();
		Mockito.when(blockRepositoryMock.findLast()).thenReturn(null);
		Block block = certificationBlockService.addBlock(blockDataGenesis, getUserMockAfterCreation());
		System.out.println("Block mined in " + (System.currentTimeMillis() - now) + "ms >> " + block);

		byte[] createCertificate = serviceUnderTest.createCertificate(block);
		Assert.assertNotNull(createCertificate);

//		try (FileOutputStream fos = new FileOutputStream("test2.pdf")) {
//			fos.write(createCertificate);
//		}
	}

	private User getUserMock() {
		User user = new User();
		user.setUsername("user#1");
		user.setUserId(10L);
		return user;
	}
	
	private User getUserMockAfterCreation() {
		User user = new User();
		user.setUsername("user#2");
		user.setUserId(11L);
		user.setTemporaryPassword("tempPass");
		return user;
	}
}
