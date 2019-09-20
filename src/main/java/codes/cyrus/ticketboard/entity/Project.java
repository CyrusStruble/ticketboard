package codes.cyrus.ticketboard.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
public class Project {

	@Id
	private String id;

	@Indexed
	private String name;

	@Indexed
	private String ownerId;

	private List<String> associatedUserIds;

	private LocalDateTime createDate = LocalDateTime.now();

	private LocalDateTime updateDate = LocalDateTime.now();

	public Project(String name, String ownerId) {
		setName(name);
		setOwnerId(ownerId);
		associatedUserIds = new ArrayList<>();
	}

	public String getId() { return id; }

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Null or empty is not allowed for name");
		}

		this.name = name;
	}

	public String getName() { return name; }

	public void setOwnerId(String ownerId) {
		if (StringUtils.isEmpty(ownerId)) {
			throw new IllegalArgumentException("Null or empty is not allowed for ownerId");
		}

		this.ownerId = ownerId;
	}

	public String getOwnerId() { return ownerId; }

	public void associateUserId(String userId) {
		associatedUserIds.add(userId);
	}

	public List<String> getAssociatedUserIds() {
		return new ArrayList<>(associatedUserIds);
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return String.format("Project[id=%s, name='%s', ownerId='%s']", id, name, ownerId);
	}
}
