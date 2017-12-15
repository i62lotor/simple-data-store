package org.geowe.datastore.user;

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
		return String.format(
                "User[Login='%s', Role='%s']",
                login, role.getName());
    }
}