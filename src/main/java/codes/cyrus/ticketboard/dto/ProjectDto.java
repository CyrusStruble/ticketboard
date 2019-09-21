package codes.cyrus.ticketboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ProjectDto extends CommonDto {

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private String prefix;

	private List<String> associatedUserIds;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@JsonProperty
	public List<String> getAssociatedUserIds() {
		return associatedUserIds;
	}

	@JsonIgnore
	public void setAssociatedUserIds(List<String> associatedUserIds) {
		this.associatedUserIds = associatedUserIds;
	}
}
