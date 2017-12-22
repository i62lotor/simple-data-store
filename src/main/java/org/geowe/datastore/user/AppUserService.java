package org.geowe.datastore.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.geowe.datastore.layer.Layer;
import org.geowe.datastore.layer.LayerRepository;
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

	private final LayerRepository layerRepository;

	private final PasswordEncoder passwordEncoder;

	public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
			LayerRepository layerRepository) {
		super();
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.layerRepository = layerRepository;
	}

	@PostConstruct
	private void InitializeDefaultStoreAdminUser() {
		List<AppUser> users = appUserRepository.findByRole(Role.STORE_ADMIN);
		if (users == null || users.isEmpty()) {
			AppUser user = create(new AppUser("sds-Admin", "default-pw", Role.STORE_ADMIN));
			logger.warn("There is no user with the role STORE-ADMIN. It is recommended to keep at least one.");
			logger.warn("Default STORE-ADMIN user created: " + user.getLogin()
					+ " Default password: default-pw	 --Change it!!!");
		}
	}

	public AppUser create(@Valid AppUser appUser) {

		if (appUserRepository.exists(appUser.getLogin())) {
			throw new IllegalArgumentException("AppUser with username " + appUser.getLogin() + " already exists");
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
			throw new IllegalArgumentException("AppUser with username " + login + " not exists");
		}
		appUserRepository.delete(login);
	}

	public boolean hasWritePermisson(String userName, Layer layer) {
		return isGranted(userName,
				new GrantedResource(layer.getId(), GrantedResource.AccessGrantedType.WRITE));
	}

	public boolean hasReadPermission(String userName, String resourceId) {
		return isGranted(userName,
				new GrantedResource(resourceId, GrantedResource.AccessGrantedType.READ));
	}

	private boolean isGranted(String userName, GrantedResource grantedResource) {
		Set<GrantedResource> grantedResources = get(userName).orElse(new AppUser()).getGrantedResources();
		boolean isGranted = false;
		if (grantedResources != null && !grantedResources.isEmpty()) {
			isGranted = grantedResources.stream().anyMatch(gr -> (gr.equals(grantedResource)));
		}
		return isGranted;
	}

	public AppUser grantAccessTo(String login, GrantedResource grantedResource) {
		Optional<AppUser> appUserOpt = null;
		if (existsUser(login) && existsResource(grantedResource.getResourdeId())) {
			appUserOpt = get(login);
			appUserOpt.get().setGrantAccess(grantedResource);
		}

		return appUserRepository.save(appUserOpt.get());
	}

	public AppUser removeGrantAccessTo(String login, String resourceId) {
		Optional<AppUser> appUserOpt = null;
		if (existsUser(login)) {
			appUserOpt = get(login);
			appUserOpt.get().removeGrantAccess(resourceId);
		}

		return appUserRepository.save(appUserOpt.get());
	}

	private boolean existsUser(String login) {
		if (!appUserRepository.exists(login)) {
			throw new IllegalArgumentException("AppUser with username " + login + " not exists");
		}
		return true;
	}

	private boolean existsResource(String resourceId) {
		if (!layerRepository.exists(resourceId)) {
			throw new IllegalArgumentException("Resource id " + resourceId + " not exists");
		}
		return true;
	}
}
