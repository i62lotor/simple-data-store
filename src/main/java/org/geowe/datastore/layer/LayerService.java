/*******************************************************************************
 * Copyright 2017 Rafael López Torres
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	
	public Page<Layer> getByLayerName(String layerName, Pageable pageable) {
		return layerRepository.findByName(layerName, pageable);
	}
	
	public Layer create(@Valid Layer layer) {
		layer.setId(null);
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
