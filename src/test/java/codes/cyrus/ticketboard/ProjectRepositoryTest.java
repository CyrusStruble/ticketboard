package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.document.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest extends CommonRepositoryTest {

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
	public void whenFindProjectsByCreatorId_thenReturnProjects() {
		// Given
		String ownerId = generateUserId();
		Project project1 = new Project(generateName(), ownerId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName(), ownerId);
		project2 = projectRepository.save(project2);

		// When
		List<Project> projectsFound = projectRepository.findByCreatorId(ownerId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(projectsFound.get(0).getCreatorId().equals(ownerId), "Found a project with non-matching ownerId");
		Assert.isTrue(projectsFound.get(1).getCreatorId().equals(ownerId), "Found a project with non-matching ownerId");

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
		List<Project> projectsFound = projectRepository.findByAssociatedUserIds(associatedUserId);

		// Then
		Assert.notEmpty(projectsFound, "No projects found");
		Assert.isTrue(projectsFound.size() == 2, "Failed to find exactly two projects");
		Assert.isTrue(associatedUser1ProjectIds.contains(projectsFound.get(0).getId()),
				"Project with non-matching associatedUserIds found");
		Assert.isTrue(associatedUser1ProjectIds.contains(projectsFound.get(1).getId()),
				"Project with non-matching associatedUserIds found");

		cleanupProject(project1, project2, project3);
	}

	@Test
	public void whenDeleteProjectsByCreatorId_thenProjectsAreDeleted() {
		// Given
		String ownerId = generateUserId();
		Project project1 = new Project(generateName(), ownerId);
		project1 = projectRepository.save(project1);

		Project project2 = new Project(generateName(), ownerId);
		project2 = projectRepository.save(project2);

		Project project3 = new Project(generateName(), generateUserId());
		project3 = projectRepository.save(project3);

		// When
		projectRepository.deleteByCreatorId(ownerId);
		Optional<Project> project1Found = projectRepository.findById(project1.getId());
		Optional<Project> project2Found = projectRepository.findById(project2.getId());
		Optional<Project> project3Found = projectRepository.findById(project3.getId());

		// Then
		Assert.isTrue(!project1Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(!project2Found.isPresent(), "Failed to delete project by owner");
		Assert.isTrue(project3Found.isPresent(), "Incorrectly deleted project with different owner");

		cleanupProject(project1, project2, project3);
	}
//
//	@Test
//	public void whenFindTicketsByProjectId_thenReturnTickets() {
//		// Given
//		String ownerId = generateUserId();
//		Project project = new Project(generateName(), ownerId);
//
//
//		Ticket ticket1 = new Ticket("Ticket 1");
//		Ticket ticket2 =new Ticket("Ticket 2");
//		project.addTicket(ticket1);
//		project.addTicket(ticket2);
//
//		project = projectRepository.save(project);
//
//		// When
//		List<Ticket> tickets = projectRepository.findTicketsByProjectId(project.getId());
//
//		// Then
//		Assert.notEmpty(tickets, "Failed to find tickets");
//		Assert.isTrue(tickets.size() == 2, "Failed to find exactly two tickets, found: " + tickets.toString());
//		Assert.isTrue(tickets.containsAll(Arrays.asList(ticket1, ticket2)), "Failed to find matching tickets");
//
//		cleanupProject(project);
//	}
}
