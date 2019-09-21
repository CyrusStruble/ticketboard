package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
	List<Project> findByCreatorId(String createdById);
	List<Project> findByAssociatedUserIds(String associatedUserId);
	List<Project> findByCreatorIdOrAssociatedUserIds(String creatorId, String associatedUserId);
	void deleteByCreatorId(String createdById);
}
