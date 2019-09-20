package codes.cyrus.ticketboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Document
public class User {

	public enum Role {
		USER, ADMIN
	}

	@Id
	private String id;

	@Indexed(unique = true)
	private String email;

	private String name;

	private String password;

	private Set<Role> roles;

	public User(String name, String email) {
		setName(name);
		setEmail(email);
		roles = Collections.singleton(Role.USER);
	}

	public String getId() { return id; }

	public void setEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			throw new IllegalArgumentException("Null or empty is not allowed for email");
		}

		this.email = email;
	}

	public String getEmail() { return email; }

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Null or empty is not allowed for name");
		}

		this.name = name;
	}

	public String getName() { return name; }

	public String getPassword() { return password; }

	public void setPassword(String password) {
		if (StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException("Null or empty is not allowed for password");
		}

		this.password = password;
	}

	public Set<Role> getRoles() {
		return new HashSet<>(roles);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	@Override
	public String toString() {
		return String.format("User[id=%s, name='%s', email='%s']", id, name, email);
	}
}
