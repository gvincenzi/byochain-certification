package org.byochain.services.test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import org.byochain.commons.utils.BlockchainUtils;
import org.byochain.model.entity.Block;
import org.byochain.model.repository.BlockRepository;
import org.byochain.services.AppServices;
import org.byochain.services.service.impl.CertificationBlockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class CertificationBlockServiceTest {
	@Autowired
	CertificationBlockService serviceUnderTest;
	
	@MockBean
	BlockRepository blockRepositoryMock;
	
	/**
	 * Level of hash algorithm difficulty
	 */
	@Value("${difficult.level}")
	private Integer difficultLevel;
	
	@Before
	public void init(){
		Mockito.when(blockRepositoryMock.findAll())
	      .thenReturn(new Iterable<Block>() {
			
			@Override
			public Iterator<Block> iterator() {
				return new HashSet<Block>().iterator();
			}
		});
		
		Mockito.when(blockRepositoryMock.save(Matchers.any(Block.class))).then(i -> i.getArgumentAt(0, Block.class));
	}
	
	@Test
	public void test(){
		LocalDate localDate = LocalDate.now();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(localDate.getDayOfYear());
		stringBuffer.append("c84632759dce38b7c712497818dab004beff68093c2738411170d2b58fc56bce");
		stringBuffer.append("241705339");
		Integer timestampInMillis = 1515437848+851;
		Calendar timestamp = Calendar.getInstance();
		timestamp.setTimeInMillis(timestampInMillis);
		stringBuffer.append(timestamp);
		System.out.println(BlockchainUtils.applySha256(stringBuffer.toString()).substring(0,difficultLevel));
	}

}
