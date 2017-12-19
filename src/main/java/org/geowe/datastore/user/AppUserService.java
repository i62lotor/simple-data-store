package org.geowe.datastore.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AppUserService {

	private static final Logger logger = Logger.getLogger(AppUserService.class);
	
	private final AppUserRepository appUserRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public AppUserService(AppUserRepository appUserRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostConstruct
	private void InitializeDefaultStoreAdminUser(){
		List<AppUser> users = appUserRepository.findByRole(Role.STORE_ADMIN);
		if (users == null || users.isEmpty()) {
			AppUser user = create(new AppUser("sds-Admin", "default-pw", Role.STORE_ADMIN));
			logger.warn("There is no user with the role STORE-ADMIN. It is recommended to keep at least one.");
			logger.warn("Default STORE-ADMIN user created: " + user.getLogin() + " Default password: default-pw	 --Change it!!!");
		}
	}

	public AppUser create(@Valid AppUser appUser) {
		
		if (appUserRepository.exists(appUser.getLogin())) {
			throw new IllegalArgumentException(
					"AppUser with username " + appUser.getLogin() + " already exists");
		}
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		return appUserRepository.save(appUser);
	}
	
	

	public Page<AppUser> get(Pageable pageable) {
		return appUserRepository.findAll(pageable);
	}

	public Optional<AppUser> get(String login) {
		return Optional.ofNullable(appUserRepository.findOne(login));
	}

	public void delete(@NotNull @NotEmpty String login) {
		if (!appUserRepository.exists(login)) {
			throw new IllegalArgumentException(
					"AppUser with username " + login + " not exists");
		}
		appUserRepository.delete(login);
	}
	
	public boolean isGranted(String userName, String resourceId){
		Set<GrantedResource> grantedResources = get(userName).orElse(new AppUser()).getGrantedResources();
		boolean isGranted = false;
		if(grantedResources!= null && !grantedResources.isEmpty()){
			isGranted = grantedResources.stream().anyMatch(gr -> gr.getResourdeId().equals(resourceId));
		}
		return isGranted;
	}
}
