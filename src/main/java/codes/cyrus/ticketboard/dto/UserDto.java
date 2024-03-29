package codes.cyrus.ticketboard.dto;

import codes.cyrus.ticketboard.document.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class UserDto extends CommonDto {

	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private String password;

	private Set<Role> roles;

	@JsonCreator
	public UserDto(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty
	public Set<Role> getRoles() {
		return roles;
	}

	@JsonIgnore
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
