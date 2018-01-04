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

public class GrantedResource {

	public enum AccessGrantedType {
		READ, WRITE;
	}
	
	private String resourdeId;
	
	private AccessGrantedType accessGrantedType;
	

	public GrantedResource() {
		super();
	}
	
	public GrantedResource(String resourdeId) {
		super();
		this.resourdeId = resourdeId;
		this.accessGrantedType = AccessGrantedType.READ;
	}
	
	public GrantedResource(String resourdeId, AccessGrantedType accessGrantedType) {
		super();
		this.resourdeId = resourdeId;
		this.accessGrantedType = accessGrantedType;
	}

	public String getResourdeId() {
		return resourdeId;
	}

	public void setResourdeId(String resourdeId) {
		this.resourdeId = resourdeId;
	}

	public AccessGrantedType getAccessGrantedType() {
		return accessGrantedType;
	}

	public void setAccessGrantedType(AccessGrantedType accessGrantedType) {
		this.accessGrantedType = accessGrantedType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessGrantedType == null) ? 0 : accessGrantedType.hashCode());
		result = prime * result + ((resourdeId == null) ? 0 : resourdeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GrantedResource other = (GrantedResource) obj;
		if (accessGrantedType != other.accessGrantedType) {
			return false;
		}
		if (resourdeId == null) {
			if (other.resourdeId != null) {
				return false;
			}
		} else if (!resourdeId.equals(other.resourdeId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GrantedResource [resourdeId=" + resourdeId + ", accessGrantedType=" + accessGrantedType + "]";
	}
}
