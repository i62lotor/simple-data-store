package org.geowe.datastore.layer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LayerRepository extends PagingAndSortingRepository<Layer, String> {
	
	Page<Layer> findByName(String name, Pageable pageable);
	
	Page<Layer> findByOpenData(boolean openData, Pageable pageable);

}
