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
package org.geowe.datastore.user.security;

import org.geowe.datastore.user.AppUser;
import org.geowe.datastore.user.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SdsUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(SdsUserDetailsService.class);

	private final AppUserRepository appUserRepository;

	public SdsUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		final AppUser user = appUserRepository.findOne(username);
		if (user == null) {
			logger.error("Couldn't find logging user " + username);
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		return new SdsUserDetails.Builder(user).build();
	}
}
