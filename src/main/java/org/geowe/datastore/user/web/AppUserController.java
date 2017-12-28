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
package org.geowe.datastore.user.web;

import java.util.Optional;

import javax.validation.Valid;

import org.geowe.datastore.user.AppUser;
import org.geowe.datastore.user.AppUserService;
import org.geowe.datastore.user.GrantedResource;
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
public class AppUserController {

	private final AppUserService appUserService;

	public AppUserController(AppUserService appUserService) {
		super();
		this.appUserService = appUserService;
	}

	@PreAuthorize("hasRole('STORE_ADMIN') || hasRole('DATA_MANAGER')")
	@GetMapping(value = "/appUsers/{id}")
	public HttpEntity<AppUser> get(@PathVariable("id") String id) {
		ResponseEntity<AppUser> response = null;
		final Optional<AppUser> appUser = appUserService.get(id);
		if (appUser.isPresent()) {
			appUser.get().setPassword("");
			response = new ResponseEntity<>(appUser.get(), HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN') || hasRole('DATA_MANAGER')")
	@GetMapping(path = "/appUsers")
	public ResponseEntity<Page<AppUser>> get(Pageable pageable) {
		final Page<AppUser> page = appUserService.get(pageable);
		page.getContent().stream().forEach(appUser -> appUser.setPassword(""));
		return new ResponseEntity<Page<AppUser>>((page), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@PostMapping(path = "/appUsers")
	public HttpEntity<AppUser> create(@RequestBody @Valid AppUser appUser) {
		final AppUser newUser = appUserService.create(appUser);
		newUser.setPassword("");
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@DeleteMapping(path = "/appUsers/{id}")
	public HttpEntity<AppUser> delete(@PathVariable("id") String login) {
		appUserService.delete(login);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@PutMapping(path = "/appUsers/{id}/granted-resource")
	public HttpEntity<AppUser> grantResouceAccess(@PathVariable("id") String login, 
			@RequestBody @Valid GrantedResource grantedResource) {
		final AppUser user = appUserService.grantAccessTo(login, grantedResource);
		user.setPassword("");
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@DeleteMapping(path = "/appUsers/{id}/granted-resource/{resourceId}")
	public HttpEntity<AppUser> removeGrantResouceAccess(@PathVariable("id") String login, 
			@PathVariable("resourceId") String resourceId) {
		final AppUser user = appUserService.removeGrantAccessTo(login, resourceId);
		user.setPassword("");
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

}
