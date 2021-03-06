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
package org.geowe.datastore.layer.web;

import java.util.Optional;

import javax.validation.Valid;

import org.geowe.datastore.layer.Layer;
import org.geowe.datastore.layer.LayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LayerController {

	private final LayerService layerService;

	public LayerController(LayerService layerService) {
		super();
		this.layerService = layerService;
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN') || @appUserService.hasReadPermission(principal.username, #id)")
	@GetMapping(value = "/layers/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Layer> get(@PathVariable("id") String id) {
		ResponseEntity<Layer> response = null;

		final Optional<Layer> layer = layerService.get(id);
		if (layer.isPresent()) {
			response = new ResponseEntity<>((layer.get()), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN') || @appUserService.hasReadPermission(principal.username, #id)")
	@GetMapping(value = "/layers/{id}/data", produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> getLayerData(@PathVariable("id") String id) {
		ResponseEntity<String> response = null;

		final Optional<Layer> layer = layerService.get(id);
		if (layer.isPresent()) {
			response = new ResponseEntity<>(layer.get().getData(), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@GetMapping(value = "/opendata/layers/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Layer> getOpenData(@PathVariable("id") String id) {
		ResponseEntity<Layer> response = null;

		final Optional<Layer> layer = layerService.get(id);
		if (layer.isPresent() && layer.get().isOpenData()) {
			response = new ResponseEntity<>((layer.get()), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@GetMapping(value = "/opendata/layers/{id}/data", produces = MediaType.TEXT_PLAIN_VALUE)
	public HttpEntity<String> getOpenDataLayerData(@PathVariable("id") String id) {
		ResponseEntity<String> response = null;

		final Optional<Layer> layer = layerService.get(id);
		if (layer.isPresent() && layer.get().isOpenData()) {
			response = new ResponseEntity<>((layer.get().getData()), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PreAuthorize("hasRole('STORE_ADMIN') || hasRole('DATA_MANAGER')")
	@GetMapping(path = "/layers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Page<Layer>> get(@RequestParam(name = "name", required = false) String layerName,
			@RequestParam(name = "only-metadata", defaultValue = "true") boolean onlyMetadata,
			Pageable pageable) {

		Page<Layer> page = null;
		if (layerName != null) {
			page = layerService.getByLayerName(layerName, pageable);
		} else {
			page = layerService.get(pageable);
		}
		
		if(onlyMetadata){
			page.getContent().parallelStream().forEach(layer -> layer.setData(""));
		}
		return new ResponseEntity<Page<Layer>>((page), HttpStatus.OK);
	}

	@GetMapping(path = "/opendata/layers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Page<Layer>> getOpenData(@RequestParam(name = "only-metadata", defaultValue = "true") boolean onlyMetadata,
			Pageable pageable) {
		
		final Page<Layer> page = layerService.getOpenData(pageable);
		
		if(onlyMetadata){
			page.getContent().parallelStream().forEach(layer -> layer.setData(""));
		}
		
		
		return new ResponseEntity<Page<Layer>>((page), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN') || @appUserService.hasWritePermission(principal.username, #layer.id)")
	@PostMapping(path = "/layers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Layer> create(@RequestBody @Valid Layer layer) {
		return new ResponseEntity<>(layerService.create(layer), HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN')")
	@PutMapping(path = "/layers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Layer> update(@RequestBody @Valid Layer layer) {
		return new ResponseEntity<>(layerService.update(layer), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN')")
	@DeleteMapping(path = "/layers/{id}")
	public HttpEntity delete(@PathVariable("id") String id) {
		layerService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
