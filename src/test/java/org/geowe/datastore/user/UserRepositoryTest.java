package org.geowe.datastore.user;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	private static final String USER_ADMIN_LOGIN = "sAdmin";
	private static final String USER_LOGIN = "sUSER";
	private static final String USER_PASSWD = "1234";
	
	@Autowired
	private AppUserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Before
	public void setUp() {
		//repository.deleteAll();
	}
	
	@Test
	public void Given_UserAdminInfo_When_Save_Then_UserStoreInDB(){
		AppUser user = repository.save(new AppUser(USER_ADMIN_LOGIN, passwordEncoder.encode(USER_PASSWD), Role.STORE_ADMIN));
		Assert.assertTrue("User not saved", user.getLogin().equals(USER_ADMIN_LOGIN));
	}
	
	@Test
	public void Given_UserInfo_When_Save_Then_UserStoreInDB(){
		AppUser user = repository.save(createUser());
		Assert.assertTrue("User not saved", user.getLogin().equals(USER_LOGIN));
	}
	
	private AppUser createUser(){
		AppUser user = new AppUser(USER_LOGIN, passwordEncoder.encode(USER_PASSWD), Role.USER);
		Set<GrantedResource> grantedResources = new HashSet<>();
		grantedResources.add(new GrantedResource("resource Id-1 test"));
		grantedResources.add(new GrantedResource("resource Id-2 test",GrantedResource.AccessGrantedType.WRITE));
		user.setGrantedResources(grantedResources);
		return user;
	}
	
	@Test	
	public void Given_UserLogin_When_delete_Then_UserRemovedFromDB(){
		repository.delete(USER_ADMIN_LOGIN);
		Assert.assertTrue("User has not been removed from db",repository.findOne(USER_ADMIN_LOGIN) == null);
	}
	
	@Test
	public void Given_UserLogin_When_findUser_Then_GetUser(){
		Given_UserAdminInfo_When_Save_Then_UserStoreInDB();
		AppUser user = repository.findOne(USER_ADMIN_LOGIN);
		Assert.assertTrue("User not found", user.getLogin().equals(USER_ADMIN_LOGIN));
	}
	
}
