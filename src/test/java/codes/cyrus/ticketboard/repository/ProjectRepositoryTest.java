package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
		assertThat(projectFound.getId(), equalTo(project.getId()));
		assertThat(projectFound.getCreatorId(), equalTo(project.getCreatorId()));

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
		assertThat(projectsFound, hasSize(2));
		assertThat(projectsFound, hasItems(project1, project2));

		cleanupProject(project1, project2);
	}

	@Test
	public void whenFindProjectsByAssociatedUserIds_thenReturnProjects() {
		// Given
		String associatedUserId = generateId();

		Project project1 = new Project(generateName());
		project1.setCreatorId(generateId());
		project1.associateUserId(associatedUserId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName());
		project2.setCreatorId(generateId());
		project2.associateUserId(associatedUserId);
		project2 = projectRepository.save(project2);

		Project project3 = new Project(generateName());
		project3.setCreatorId(generateId());
		project3 = projectRepository.save(project3);

		// When
		List<Project> projectsFound = projectRepository.findByAssociatedUserIds(associatedUserId);

		// Then
		assertThat(projectsFound, hasSize(2));
		assertThat(projectsFound, hasItems(project1, project2));
		assertThat(projectsFound, not(hasItem(project3)));

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
		assertThat(project1Found.isPresent(), is(false));
		assertThat(project2Found.isPresent(), is(false));
		assertThat(project3Found.isPresent(), is(true));

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
		assertThat(projectsFoundByOwner1, hasSize(2));
		assertThat(projectsFoundByOwner1, hasItems(project1, project2));

		assertThat(projectsFoundByOwner2, hasSize(1));
		assertThat(projectsFoundByOwner2, hasItem(project3));

		assertThat(projectsFoundByAssociatedUser1, hasSize(1));
		assertThat(projectsFoundByAssociatedUser1, hasItem(project1));

		assertThat(projectsFoundByAssociatedUser2, hasSize(3));
		assertThat(projectsFoundByAssociatedUser2, hasItems(project1, project2, project3));

		cleanupProject(project1, project2, project3);
	}
}
