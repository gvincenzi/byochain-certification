package org.byochain.services.service;

import org.byochain.model.entity.User;
import org.byochain.services.exception.ByoChainServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface of service related to {@link User} model entity
 * @author Giuseppe Vincenzi
 *
 */
public interface IUserService {

	/**
	 * Method to retrieve a collection of all users (with pagination)
	 * @param pageable Pageable - Spring Data object
	 * @return Page<User> containing all users paginated
	 */
	Page<User> getUsers(Pageable pageable);
	
	/**
	 * Method enable/disable an User by its ID
	 * @param userId ID of the user
	 * @param enabled TRUE or FALSE to enable or disable
	 * @return User modified
	 * @throws ByoChainServiceException
	 */
	User enableUser(Long userId, Boolean enabled) throws ByoChainServiceException;
	
	/**
	 * Method to add an User (with Role ROLE_USER as default)
	 * @param user User to create
	 * @return User created
	 * @throws ByoChainServiceException
	 */
	User addUser(User user) throws ByoChainServiceException;
	
	/**
	 * Method to get an User by username and password
	 * @param username
	 * @param password Encoded password
	 * @return User retrived if exists
	 * @throws ByoChainServiceException
	 */
	User getUser(String username, String password) throws ByoChainServiceException;
}
