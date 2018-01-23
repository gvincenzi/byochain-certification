package org.byochain.model.repository;

import org.byochain.model.entity.Block;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for {@link Block} entity class
 * @author Giuseppe Vincenzi
 *
 */
public interface BlockRepository extends PagingAndSortingRepository<Block, Long> {
	
	/**
	 * Method to select a {@link Block} by its own hash
	 * @param hash String
	 * @return Block
	 */
	@Query("SELECT b FROM Block b WHERE b.hash = :hash")
    public Block find(@Param("hash") String hash);
	
	/**
	 * Method to select a Block with the MAX ID
	 * @return Block
	 */
	@Query("SELECT b FROM Block b WHERE b.id = (SELECT MAX(b.id) FROM Block b)")
    public Block findLast();
}
