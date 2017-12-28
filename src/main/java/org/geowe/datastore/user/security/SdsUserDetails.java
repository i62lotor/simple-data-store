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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.geowe.datastore.user.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SdsUserDetails extends User {

	private static final long serialVersionUID = 1L;

	private final AppUser appUser;

	private SdsUserDetails(AppUser appUser, Collection<GrantedAuthority> userAuthorities) {
		super(appUser.getLogin(), appUser.getPassword(), true, true, true,
				true, userAuthorities);
		this.appUser = appUser;
	}

	public AppUser getUser() {
		return appUser;
	}

	public static class Builder {
		private final AppUser appUser;

		public Builder(AppUser appUser) {
			this.appUser = appUser;
		}

		public SdsUserDetails build() {
			final Collection<GrantedAuthority> userAuthorities = this.getAuthorities(appUser);
			return new SdsUserDetails(appUser, userAuthorities);
		}

		private Collection<GrantedAuthority> getAuthorities(AppUser user) {
			final Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(user.getRole().getGrantedAuthority());
			return authorities;
		}
	}

}
