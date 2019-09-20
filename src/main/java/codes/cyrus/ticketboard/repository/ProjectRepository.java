package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
	List<Project> findProjectsByOwnerId(String ownerId);
	List<Project> findProjectsByAssociatedUserIds(String associatedUserId);
	void deleteProjectsByOwnerId(String ownerId);
}
