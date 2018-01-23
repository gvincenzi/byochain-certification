package org.byochain.model.repository.test;

import java.util.LinkedHashSet;
import java.util.Set;

import org.byochain.model.AppModel;
import org.byochain.model.entity.Role;
import org.byochain.model.repository.RoleRepository;
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
public class RoleRepositoryTest {
	@Autowired
	private RoleRepository serviceUnderTest;

	private Set<Role> roles = new LinkedHashSet<>();

	@Before
	public void init() {
		serviceUnderTest.deleteAll();

		Role roleAdmin = new Role();
		roleAdmin.setRole("ROLE_ADMIN");

		Role roleUser = new Role();
		roleUser.setRole("ROLE_USER");

		roles.add(roleAdmin);
		roles.add(roleUser);

		serviceUnderTest.save(roles);
	}

	@Test
	public void count() {
		Assert.assertEquals(roles.size(), serviceUnderTest.count());
	}

	@Test
	public void findById() {
		for (Role role : roles) {
			Assert.assertEquals(role, serviceUnderTest.findOne(role.getRoleId()));
		}
	}

	@Test
	public void findByName() {
		for (Role role : roles) {
			Assert.assertEquals(role, serviceUnderTest.find(role.getRole()));
		}
	}
}
