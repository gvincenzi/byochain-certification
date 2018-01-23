package org.byochain.model.repository;

import org.byochain.model.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for {@link Role} entity class
 * @author Giuseppe Vincenzi
 *
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
	/**
	 * Method to select a {@link Role} by its own roleName
	 * @param hash String
	 * @return Block
	 */
	@Query("SELECT r FROM Role r WHERE r.role = :roleName")
    public Role find(@Param("roleName") String roleName);
}
