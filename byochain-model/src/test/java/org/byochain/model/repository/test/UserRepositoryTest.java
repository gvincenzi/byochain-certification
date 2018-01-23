package org.byochain.model.repository.test;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.byochain.model.AppModel;
import org.byochain.model.entity.User;
import org.byochain.model.repository.UserRepository;
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
public class UserRepositoryTest {
	@Autowired
	private UserRepository serviceUnderTest;

	private Set<User> users = new LinkedHashSet<>();

	@Before
	public void init() {
		serviceUnderTest.deleteAll();
		
		User user1 = new User();
		user1.setCreationDate(Calendar.getInstance());
		user1.setPassword("password#1");
		user1.setUsername("user#1");

		User user2 = new User();
		user2.setCreationDate(Calendar.getInstance());
		user2.setPassword("password#2");
		user2.setUsername("user#2");

		users.add(user1);
		users.add(user2);
		
		serviceUnderTest.save(users);
	}

	@Test
	public void count() {
		Assert.assertEquals(users.size(), serviceUnderTest.count());
	}

	@Test
	public void findById() {
		for (User user : users) {
			Assert.assertEquals(user, serviceUnderTest.findOne(user.getUserId()));
		}
	}

	@Test
	public void findByUsernameAndPassword() {
		for (User user : users) {
			Assert.assertEquals(user, serviceUnderTest.find(user.getUsername(), user.getPassword()));
		}
	}
}
