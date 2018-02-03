package org.byochain.model.repository;

import org.byochain.model.entity.BlockData;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link BlockReferer} entity class
 * @author Giuseppe Vincenzi
 *
 */
public interface BlockDataRepository extends CrudRepository<BlockData, Long>{

}
