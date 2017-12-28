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

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public class AppUser {
	
	
	@Id
	private String login;

	@NotEmpty
	@Length(min = 8)
	private String password;
	
	@NotNull
	private Role role;
	
	private Set<GrantedResource> grantedResources = new HashSet<>();
	
	public AppUser() {
		super();
		role = Role.USER;
	}

	public AppUser(String login, String password, Role role) {
		super();
		this.login = login;
		this.password = password;
		this.role = role;
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<GrantedResource> getGrantedResources() {
		return grantedResources;
	}

	public void setGrantedResources(Set<GrantedResource> grantedResources) {
		this.grantedResources = grantedResources;
	}
	
	public void setGrantAccess(GrantedResource grantedResource){
		this.grantedResources.add(grantedResource);
	}
	
	public void removeGrantAccess(GrantedResource grantedResource) {
		this.grantedResources.remove(grantedResource);
	}
	
	public void removeGrantAccess(String resourceid) {
		this.grantedResources.removeIf(ga -> ga.getResourdeId().equals(resourceid));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppUser [login=" + login + ", role=" + role + ", grantedResources=" + grantedResources + "]";
	}

}
