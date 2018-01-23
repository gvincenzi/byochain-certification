package org.byochain.services.test;

import java.util.Calendar;

import org.byochain.model.entity.Role;
import org.byochain.model.entity.User;
import org.byochain.model.repository.RoleRepository;
import org.byochain.model.repository.UserRepository;
import org.byochain.services.AppServices;
import org.byochain.services.exception.ByoChainServiceException;
import org.byochain.services.service.IUserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UserServiceTest {
	@Autowired
	IUserService serviceUnderTest;
	
	@MockBean
	UserRepository userRepositoryMock;
	
	@MockBean
	RoleRepository roleRepositoryMock;
	
	@Before
	public void init(){
		Mockito.when(userRepositoryMock.findOne(Mockito.any(Long.class))).thenReturn(getUserMock());
		Mockito.when(userRepositoryMock.save(Mockito.any(User.class))).thenReturn(getUserMock());
	}
	
	public static User getUserMock(){
		User user1 = new User();
		user1.setCreationDate(Calendar.getInstance());
		user1.setPassword("password#1");
		user1.setUsername("user#1");
		
		return user1;
	}
	
	public static Role getRoleMock(){
		Role role = new Role();
		role.setRole("ROLE_USER");
		
		return role;
	}
	
	@Test(expected = ByoChainServiceException.class)
	public void testException() throws ByoChainServiceException{
		serviceUnderTest.addUser(null);
	}
	
	@Test(expected = ByoChainServiceException.class)
	public void testPasswordException() throws ByoChainServiceException{
		User user = getUserMock();
		serviceUnderTest.addUser(user);
	}
	
	@Test(expected = ByoChainServiceException.class)
	public void testUsernameException() throws ByoChainServiceException{
		User user = getUserMock();
		user.setPassword(null);
		user.setUsername(null);
		serviceUnderTest.addUser(user);
	}
	
	@Test(expected = ByoChainServiceException.class)
	public void testDatabaseInitError() throws ByoChainServiceException{
		User user = getUserMock();
		user.setPassword(null);
		user = serviceUnderTest.addUser(user);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void test() throws ByoChainServiceException{
		Mockito.when(roleRepositoryMock.find(Mockito.anyString())).thenReturn(getRoleMock());
		User user = getUserMock();
		user.setPassword(null);
		user = serviceUnderTest.addUser(user);
		Assert.assertNotNull(user);
	}
	
	@Test(expected = ByoChainServiceException.class)
	public void testEnableUserIDException() throws ByoChainServiceException{
		Mockito.when(roleRepositoryMock.find(Mockito.anyString())).thenReturn(getRoleMock());
		User user = getUserMock();
		user = serviceUnderTest.enableUser(user.getUserId(), Boolean.FALSE);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testEnableUser() throws ByoChainServiceException{
		Mockito.when(roleRepositoryMock.find(Mockito.anyString())).thenReturn(getRoleMock());
		User user = getUserMock();
		user.setUserId(10L);
		user = serviceUnderTest.enableUser(user.getUserId(), Boolean.FALSE);
		Assert.assertNotNull(user);
	}
}
