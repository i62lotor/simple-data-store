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
package org.geowe.datastore.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	USER(Values.USER), DATA_MANAGER(Values.DATA_MANAGER), STORE_ADMIN(Values.STORE_ADMIN);

	private String name;
	private SimpleGrantedAuthority grantedAuthority;

	Role(String name) {
		this.name = name;
		this.grantedAuthority = new SimpleGrantedAuthority("ROLE_" + name);
	}

	public String getName() {
		return this.name;
	}

	public SimpleGrantedAuthority getGrantedAuthority() {
		return this.grantedAuthority;
	}

	public static class Values {
		public static final String USER = "USER";
		public static final String DATA_MANAGER = "DATA_MANAGER";
		public static final String STORE_ADMIN = "STORE_ADMIN";
	}

	
}
