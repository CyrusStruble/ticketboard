package codes.cyrus.ticketboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public abstract class CommonDto {

	private String id;

	private LocalDateTime createDate = LocalDateTime.now();

	private LocalDateTime updateDate = LocalDateTime.now();

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
