package codes.cyrus.ticketboard.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Project extends CommonDocument {

	@Indexed
	private String name;

	private String prefix;

	private List<String> associatedUserIds;

	public Project(String name, String creatorId) {
		setName(name);
		setCreatorId(creatorId);
		associatedUserIds = new ArrayList<>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() { return name; }

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void associateUserId(String userId) {
		associatedUserIds.add(userId);
	}

	public List<String> getAssociatedUserIds() {
		return new ArrayList<>(associatedUserIds);
	}

	@Override
	public String toString() {
		return String.format("Project[id=%s, name='%s', creatorId='%s', createDate='%s', updateDate='%s']", getId(), name,
				getCreatorId(), getCreateDate(), getUpdateDate());
	}
}
