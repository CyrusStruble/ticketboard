package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest extends CommonRepositoryTest {

	@Test
	public void whenFindProjectById_thenReturnProject() {
		// Given
		Project project = new Project(generateName());
		project.setCreatorId(generateId());
		project = projectRepository.save(project);

		// When
		Project projectFound = projectRepository.findById(project.getId()).get();

		// Then
		Assert.notNull(projectFound, "No project found");
		Assert.isTrue(project.getId().equals(projectFound.getId()), "No project found");

		cleanupProject(project);
	}

	@Test
	public void whenFindProjectsByCreatorId_thenReturnProjects() {
		// Given
		String creatorId = generateId();
		Project project1 = new Project(generateName());
		project1.setCreatorId(creatorId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName());
		project2.setCreatorId(creatorId);
		project2 = projectRepository.save(project2);

		// When
		List<Project> projectsFound = projectRepository.findByCreatorId(creatorId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(projectsFound.get(0).getCreatorId().equals(creatorId), "Found a project with non-matching creatorId");
		Assert.isTrue(projectsFound.get(1).getCreatorId().equals(creatorId), "Found a project with non-matching creatorId");

		cleanupProject(project1, project2);
	}

	@Test
	public void whenFindProjectsByAssociatedUserIds_thenReturnProjects() {
		List<String> associatedUserProjectIds = new ArrayList<>();

		// Given
		String associatedUserId = generateId();

		Project project1 = new Project(generateName());
		project1.setCreatorId(generateId());
		project1.associateUserId(associatedUserId);
		project1 = projectRepository.save(project1);
		associatedUserProjectIds.add(project1.getId());

		Project project2 = new Project(generateName());
		project2.setCreatorId(generateId());
		project2.associateUserId(associatedUserId);
		project2 = projectRepository.save(project2);
		associatedUserProjectIds.add(project2.getId());

		Project project3 = new Project(generateName());
		project3.setCreatorId(generateId());
		project3.associateUserId(associatedUserId);
		project3 = projectRepository.save(project2);

		// When
		List<Project> projectsFound = projectRepository.findByAssociatedUserIds(associatedUserId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(associatedUserProjectIds.contains(projectsFound.get(0).getId()),
				"Project with non-matching associatedUserIds found");
		Assert.isTrue(associatedUserProjectIds.contains(projectsFound.get(1).getId()),
				"Project with non-matching associatedUserIds found");

		cleanupProject(project1, project2, project3);
	}

	@Test
	public void whenDeleteProjectsByCreatorId_thenProjectsAreDeleted() {
		// Given
		String creatorId = generateId();
		Project project1 = new Project(generateName());
		project1.setCreatorId(creatorId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName());
		project2.setCreatorId(creatorId);
		project2 = projectRepository.save(project2);

		Project project3 = new Project(generateName());
		project3.setCreatorId(generateId());
		project3 = projectRepository.save(project3);

		// When
		projectRepository.deleteByCreatorId(creatorId);
		Optional<Project> project1Found = projectRepository.findById(project1.getId());
		Optional<Project> project2Found = projectRepository.findById(project2.getId());
		Optional<Project> project3Found = projectRepository.findById(project3.getId());

		// Then
		Assert.isTrue(!project1Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(!project2Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(project3Found.isPresent(), "Incorrectly deleted project with different owner");

		cleanupProject(project1, project2, project3);
	}

	@Test
	public void whenFindByCreatorIdOrAssociatedUserIds_thenReturnProjectIds() {
		// Given
		String creatorId1 = generateId();
		String creatorId2 = generateId();
		String associatedUserId1 = generateId();
		String associatedUserId2 = generateId();

		Project project1 = new Project(generateName());
		project1.setCreatorId(creatorId1);
		project1.associateUserId(associatedUserId1);
		project1.associateUserId(associatedUserId2);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName());
		project2.setCreatorId(creatorId1);
		project2.associateUserId(associatedUserId2);
		project2 = projectRepository.save(project2);

		Project project3 = new Project(generateName());
		project3.setCreatorId(creatorId2);
		project3.associateUserId(associatedUserId2);
		project3 = projectRepository.save(project3);

		// When
		List<Project> projectsFoundByOwner1 = projectRepository.findByCreatorIdOrAssociatedUserIds(creatorId1, creatorId1);
		List<Project> projectsFoundByOwner2 = projectRepository.findByCreatorIdOrAssociatedUserIds(creatorId2, creatorId2);
		List<Project> projectsFoundByAssociatedUser1 = projectRepository.findByCreatorIdOrAssociatedUserIds(associatedUserId1, associatedUserId1);
		List<Project> projectsFoundByAssociatedUser2 = projectRepository.findByCreatorIdOrAssociatedUserIds(associatedUserId2, associatedUserId2);

		// Then
		Assert.notEmpty(projectsFoundByOwner1, "Failed to find projects");
		Assert.isTrue(projectsFoundByOwner1.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(projectsFoundByOwner1.containsAll(Arrays.asList(project1, project2)), "Failed to find matching projects");

		Assert.notEmpty(projectsFoundByOwner2, "Failed to find project");
		Assert.isTrue(projectsFoundByOwner2.size() == 1, "Failed to find exactly one project");
		Assert.isTrue(projectsFoundByOwner2.contains(project3), "Failed to find matching project");

		Assert.notEmpty(projectsFoundByAssociatedUser1, "Failed to find project");
		Assert.isTrue(projectsFoundByAssociatedUser1.size() == 1, "Failed to find exactly one project");
		Assert.isTrue(projectsFoundByAssociatedUser1.contains(project1), "Failed to find matching project");

		Assert.notEmpty(projectsFoundByAssociatedUser2, "Failed to find projects");
		Assert.isTrue(projectsFoundByAssociatedUser2.size() == 3, "Failed to find exactly three projects");
		Assert.isTrue(projectsFoundByAssociatedUser2.containsAll(Arrays.asList(project1, project2, project3)),
				"Failed to find matching projects");

		cleanupProject(project1, project2, project3);
	}
}
