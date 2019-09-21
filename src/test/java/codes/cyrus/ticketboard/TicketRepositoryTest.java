package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketRepositoryTest extends CommonRepositoryTest {

	@Test
	public void whenFindByProjectId_thenReturnTickets() {
		// Given
		String creatorId = generateUserId();
		Project project = new Project(generateName(), creatorId);
		project.setPrefix("BAC");

		project = projectRepository.save(project);

		Ticket ticket1 = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket1.setCreatorId(creatorId);
		ticket1 = ticketRepository.save(ticket1);

		Ticket ticket2 = new Ticket(project.getId(), project.getPrefix() + "-2");
		ticket2.setCreatorId(creatorId);
		ticket2 = ticketRepository.save(ticket2);

		// When
		List<Ticket> tickets = ticketRepository.findByProjectId(project.getId());

		// Then
		Assert.notEmpty(tickets, "Failed to find tickets");
		Assert.isTrue(tickets.size() == 2, "Failed to find exactly two tickets, found: " + tickets.toString());
		Assert.isTrue(tickets.containsAll(Arrays.asList(ticket1, ticket2)), "Failed to find matching tickets");

		cleanupProject(project);
	}
}
