package codes.cyrus.ticketboard.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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

	public void deassociateUserId(String userId) { associatedUserIds.remove(userId); }

	public List<String> getAssociatedUserIds() {
		return new ArrayList<>(associatedUserIds);
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

		Project rhs = (Project) obj;

		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(name, rhs.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 31)
				.append(name)
				.toHashCode();
	}
}
