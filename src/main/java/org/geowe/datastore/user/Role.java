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
