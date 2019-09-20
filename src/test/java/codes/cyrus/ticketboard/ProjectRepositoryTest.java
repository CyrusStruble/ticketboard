package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.entity.Project;
import codes.cyrus.ticketboard.repository.ProjectRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest {

	@Autowired
	private ProjectRepository projectRepository;

	@Test
	public void whenFindProjectById_thenReturnProject() {
		// Given
		Project project1 = new Project(generateName(), generateUserId());
		project1 = projectRepository.save(project1);

		// When
		Project projectFound = projectRepository.findById(project1.getId()).get();

		// Then
		Assert.notNull(projectFound, "No project found");
		Assert.isTrue(project1.getId().equals(projectFound.getId()), "No project found");

		cleanupProject(project1);
	}

	@Test
	public void whenFindProjectsByOwnerId_thenReturnProjects() {
		// Given
		String ownerId = generateUserId();
		Project project1 = new Project(generateName(), ownerId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName(), ownerId);
		project2 = projectRepository.save(project2);

		// When
		List<Project> projectsFound = projectRepository.findProjectsByOwnerId(ownerId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(projectsFound.get(0).getOwnerId().equals(ownerId), "Found a project with non-matching ownerId");
		Assert.isTrue(projectsFound.get(1).getOwnerId().equals(ownerId), "Found a project with non-matching ownerId");

		cleanupProject(project1, project2);
	}

	@Test
	public void whenFindProjectsByAssociatedUserIds_thenReturnProjects() {
		List<String> associatedUser1ProjectIds = new ArrayList<>();

		// Given
		String associatedUserId = generateUserId();

		Project project1 = new Project(generateName(), generateUserId());
		project1.associateUserId(associatedUserId);
		project1 = projectRepository.save(project1);
		associatedUser1ProjectIds.add(project1.getId());

		Project project2 = new Project(generateName(), generateUserId());
		project2.associateUserId(associatedUserId);
		project2 = projectRepository.save(project2);
		associatedUser1ProjectIds.add(project2.getId());

		Project project3 = new Project(generateName(), generateUserId());
		project3.associateUserId(associatedUserId);
		project3 = projectRepository.save(project2);

		// When
		List<Project> projectsFound = projectRepository.findProjectsByAssociatedUserIds(associatedUserId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(associatedUser1ProjectIds.contains(projectsFound.get(0).getId()),
				"Project with non-matching associatedUserIds found");
		Assert.isTrue(associatedUser1ProjectIds.contains(projectsFound.get(1).getId()),
				"Project with non-matching associatedUserIds found");

		cleanupProject(project1, project2, project3);
	}

	private void cleanupProject(Project... projects) {
		for (Project project : projects) {
			projectRepository.deleteById(project.getId());
		}
	}

	@Test
	public void whenDeleteProjectsByOwnerId_thenProjectsAreDeleted() {
		// Given
		String ownerId = generateUserId();
		Project project1 = new Project(generateName(), ownerId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName(), ownerId);
		project2 = projectRepository.save(project2);

		Project project3 = new Project(generateName(), generateUserId());
		project3 = projectRepository.save(project3);

		// When
		projectRepository.deleteProjectsByOwnerId(ownerId);
		Optional<Project> project1Found = projectRepository.findById(project1.getId());
		Optional<Project> project2Found = projectRepository.findById(project2.getId());
		Optional<Project> project3Found = projectRepository.findById(project3.getId());

		// Then
		Assert.isTrue(!project1Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(!project2Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(project3Found.isPresent(), "Incorrectly deleted project with different owner");

		cleanupProject(project1, project2, project3);
	}

	private static String generateName() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	private static String generateUserId() {
		return new ObjectId().toString();
	}
}
