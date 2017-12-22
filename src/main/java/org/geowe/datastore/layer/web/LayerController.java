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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerController {

	private final LayerService layerService;

	public LayerController(LayerService layerService) {
		super();
		this.layerService = layerService;
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN') || @appUserService.hasReadPermission(principal.username, #id)")
	@GetMapping(value = "/layers/{id}")
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
	@GetMapping(value = "/layers/{id}/data")
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
	
	@GetMapping(value = "/opendata/layers/{id}")
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
	
	@GetMapping(value = "/opendata/layers/{id}/data")
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
	@GetMapping(path = "/layers")
	public ResponseEntity<Page<Layer>> get(Pageable pageable) {
		final Page<Layer> page = layerService.get(pageable);
		return new ResponseEntity<Page<Layer>>((page), HttpStatus.OK);
	}
	
	@GetMapping(path = "/opendata/layers")
	public ResponseEntity<Page<Layer>> getOpenData(Pageable pageable) {
		final Page<Layer> page = layerService.getOpenData(pageable);
		return new ResponseEntity<Page<Layer>>((page), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN') || @appUserService.hasWritePermission(principal.username, #layer)")
	@PostMapping(path = "/layers")
	public HttpEntity<Layer> create(@RequestBody @Valid Layer layer) {
		return new ResponseEntity<>(layerService.create(layer), HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN')")
	@PutMapping(path = "/layers")
	public HttpEntity<Layer> update(@RequestBody @Valid Layer layer) {
		return new ResponseEntity<>(layerService.update(layer), HttpStatus.ACCEPTED);
	}

	@PreAuthorize("hasRole('DATA_MANAGER') || hasRole('STORE_ADMIN')")
	@DeleteMapping(path = "/layers/{id}")
	public HttpEntity<Layer> delete(@PathVariable("id") String id) {
		layerService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
