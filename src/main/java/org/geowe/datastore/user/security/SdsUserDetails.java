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
