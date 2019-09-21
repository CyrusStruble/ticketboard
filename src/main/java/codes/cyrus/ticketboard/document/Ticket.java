package codes.cyrus.ticketboard.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ticket extends CommonDocument {

	@Indexed(unique = true)
	private String name;

	private String title;

	private String description;

	@Indexed
	private String assigneeId;

	@Indexed
	private String projectId;

	private Priority priority = Priority.MEDIUM;

	public Ticket(String projectId, String name) {
		this.projectId = projectId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
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

		Ticket rhs = (Ticket) obj;

		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(name, rhs.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 33)
				.append(name)
				.toHashCode();
	}
}
