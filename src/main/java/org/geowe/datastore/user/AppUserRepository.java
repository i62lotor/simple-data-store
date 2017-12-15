package org.geowe.datastore.user;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppUserRepository extends PagingAndSortingRepository<AppUser, String> {

	List<AppUser> findByRole(Role role);
}
