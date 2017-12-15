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
