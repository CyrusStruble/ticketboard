package codes.cyrus.ticketboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UserDto {
	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private String password;

	private String id;

	private LocalDateTime createDate = LocalDateTime.now();

	private LocalDateTime updateDate = LocalDateTime.now();

	@JsonCreator
	public UserDto(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public UserDto(String name, String email, String id) {
		this(name, email);
		this.id = id;
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
	public String getId() {
		return id;
	}

	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	@JsonIgnore
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@JsonProperty
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	@JsonIgnore
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
}
