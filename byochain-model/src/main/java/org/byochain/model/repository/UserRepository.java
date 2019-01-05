package org.byochain.model.repository;

import org.byochain.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for {@link User} entity class
 * @author Giuseppe Vincenzi
 *
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	/**
	 * Method to select a {@link User} by username and password
	 * @param username String
	 * @param password String
	 * @return User
	 */
	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    public User find(@Param("username") String username, @Param("password") String password);

	public User findByUsername(String username);
}
