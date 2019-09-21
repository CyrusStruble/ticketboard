package codes.cyrus.ticketboard.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class CommonDocument {

	@Id
	private String id;

	@Indexed
	private String creatorId;

	private LocalDateTime createDate = LocalDateTime.now();

	private LocalDateTime updateDate = LocalDateTime.now();

	public String getId() {
		return id;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}

		CommonDocument rhs = (CommonDocument) obj;

		return new EqualsBuilder()
				.append(creatorId, rhs.creatorId)
				.append(id, rhs.id)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(creatorId)
				.append(id)
				.toHashCode();
	}
}
