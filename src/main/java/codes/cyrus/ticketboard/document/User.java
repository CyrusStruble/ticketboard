package codes.cyrus.ticketboard.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
public class User extends CommonDocument {

	@Indexed(unique = true)
	private String email;

	private String name;

	@ToStringExclude
	private String password;

	private Set<Role> roles;

	public User(String name, String email) {
		setName(name);
		setEmail(email);
		roles = new HashSet<>();
		roles.add(Role.USER);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() { return email; }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() { return name; }

	public String getPassword() { return password; }

	public void setPassword(String password) {
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
		return ReflectionToStringBuilder.toString(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}

		User rhs = (User) obj;

		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(name, rhs.name)
				.append(email, rhs.email)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(9, 29)
				.append(name)
				.append(email)
				.toHashCode();
	}
}
