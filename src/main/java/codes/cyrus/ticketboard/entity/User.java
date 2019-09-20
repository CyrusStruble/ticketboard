package codes.cyrus.ticketboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

@Document
public class User {

	@Id
	private String id;

	@Indexed(unique = true)
	private String email;

	private String name;

	public User(String name, String email) {
		setName(name);
		setEmail(email);
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

	@Override
	public String toString() {
		return String.format("User[id=%s, name='%s', email='%s']", id, name, email);
	}
}
