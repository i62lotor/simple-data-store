package org.geowe.datastore.layer;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LayerService {

	
	private final LayerRepository layerRepository;

	public LayerService(LayerRepository layerRepository) {
		super();
		this.layerRepository = layerRepository;
	}

	public Optional<Layer> get(String id) {
		return Optional.ofNullable(layerRepository.findOne(id));
	}

	public Page<Layer> get(Pageable pageable) {
		return layerRepository.findAll(pageable);
	}
	
	public Page<Layer> getOpenData(Pageable pageable) {
		return layerRepository.findByOpenData(true, pageable);
	}
	
	public Layer create(@Valid Layer layer) {
		if (layer.getId() != null && layerRepository.exists(layer.getId())) {
			throw new IllegalArgumentException("Layer with id " +  layer.getId()+ " already exists, try update!");
		}
		updateLastUpdate(layer);
		return layerRepository.save(layer);
	}
	
	public Layer update(@Valid Layer layer){
		updateLastUpdate(layer);
		return layerRepository.save(layer);
	}
	
	private void updateLastUpdate(Layer layer){
		if(layer.getLastUpdate() == null){
			layer.setLastUpdate(LocalDateTime.now());
		}
	}
	
	public void delete(@NotNull @NotEmpty String id) {
		if (!layerRepository.exists(id)) {
			throw new IllegalArgumentException(
					"Layer with id" + id + " not exists");
		}
		layerRepository.delete(id);
	}
}
