package org.byochain.model.repository;

import java.util.List;

import org.byochain.model.entity.Block;
import org.byochain.model.entity.BlockReferer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for {@link BlockReferer} entity class
 * @author Giuseppe Vincenzi
 *
 */
public interface BlockRefererRepository extends CrudRepository<BlockReferer, Long>{
	/**
	 * Method to select a List of {@link BlockReferer} by its owner
	 * @param hash String
	 * @return Block
	 */
	@Query("SELECT b FROM BlockReferer b WHERE b.owner = :owner AND lower(b.referer) = lower(:referer)")
    public List<BlockReferer> find(@Param("owner") Block owner, @Param("referer") String referer);
}
