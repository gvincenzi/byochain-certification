package org.byochain.model.repository.test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.byochain.model.AppModel;
import org.byochain.model.entity.Role;
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
public class UserRolesRepositoryTest {
	@Autowired
	private UserRepository serviceUnderTest;

	private static Set<User> users = new HashSet<>();

	@Before
	public void init() {
		if (users.isEmpty()) {
			Role roleAdmin = new Role();
			roleAdmin.setRole("ROLE_ADMIN");
			
			Role roleUser = new Role();
			roleUser.setRole("ROLE_USER");
			
			User user1 = new User();
			user1.setCreationDate(Calendar.getInstance());
			user1.setPassword("password#1");
			user1.setUsername("user#1");
			user1.getRoles().add(roleAdmin);
			user1.getRoles().add(roleUser);
			
			User user2 = new User();
			user2.setCreationDate(Calendar.getInstance());
			user2.setPassword("password#2");
			user2.setUsername("user#2");
			user2.getRoles().add(roleUser);
			
			users.add(user1);
			users.add(user2);
		}
	}
	
	@Test
	@Transactional
	public void count() {
		serviceUnderTest.deleteAll();
		serviceUnderTest.save(users);
		Assert.assertEquals(users.size(), serviceUnderTest.count());
	}

	@Test
	@Transactional
	public void findById() {
		serviceUnderTest.deleteAll();
		serviceUnderTest.save(users);
		for (User user : users) {
			User userFound = serviceUnderTest.findOne(user.getUserId());
			Assert.assertEquals(user, userFound);
			Assert.assertEquals(user.getRoles().size(), userFound.getRoles().size());
			for (Role role : userFound.getRoles()) {
				user.getRoles().contains(role);
			}
		}

	}
}
