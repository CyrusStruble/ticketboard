package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.Priority;
import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketRepositoryTest extends CommonRepositoryTest {

	@Test
	public void whenFindByProjectId_thenReturnTickets() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR1");

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

	@Test
	public void whenCountByProjectId_thenReturnCount() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR2");

		project = projectRepository.save(project);
		Ticket ticket1 = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket1.setCreatorId(creatorId);
		ticketRepository.save(ticket1);

		Ticket ticket2 = new Ticket(project.getId(), project.getPrefix() + "-2");
		ticket2.setCreatorId(creatorId);
		ticketRepository.save(ticket2);

		// When
		Long count = ticketRepository.countByProjectId(project.getId());

		// Then
		Assert.notNull(count, "Count is null");
		Assert.isTrue(count == 2, "Count does not equal 2, found: " + count);

		cleanupProject(project);
	}

	@Test
	public void whenCountByProjectIdAndPriority_thenReturnCount() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR3");

		project = projectRepository.save(project);
		Ticket ticket1 = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket1.setCreatorId(creatorId);
		ticket1.setPriority(Priority.HIGH);
		ticketRepository.save(ticket1);

		Ticket ticket2 = new Ticket(project.getId(), project.getPrefix() + "-2");
		ticket2.setCreatorId(creatorId);
		ticket2.setPriority(Priority.HIGH);
		ticketRepository.save(ticket2);

		Ticket ticket3 = new Ticket(project.getId(), project.getPrefix() + "-3");
		ticket3.setCreatorId(creatorId);
		ticket3.setPriority(Priority.HIGHEST);
		ticketRepository.save(ticket3);

		// When
		Long countOfHigh = ticketRepository.countByProjectIdAndPriority(project.getId(), Priority.HIGH);
		Long countOfHighest = ticketRepository.countByProjectIdAndPriority(project.getId(), Priority.HIGHEST);

		// Then
		Assert.notNull(countOfHigh, "Count is null");
		Assert.isTrue(countOfHigh == 2, "Count does not equal 2, found: " + countOfHigh);

		Assert.notNull(countOfHighest, "Count is null");
		Assert.isTrue(countOfHighest == 1, "Count does not equal 1, found: " + countOfHighest);

		cleanupProject(project);
	}

	@Test
	public void whenFindByProjectIdAndPriority_thenReturnTickets() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR4");

		project = projectRepository.save(project);

		Ticket ticket1 = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket1.setCreatorId(creatorId);
		ticket1.setPriority(Priority.HIGH);
		ticket1 = ticketRepository.save(ticket1);

		Ticket ticket2 = new Ticket(project.getId(), project.getPrefix() + "-2");
		ticket2.setCreatorId(creatorId);
		ticket2.setPriority(Priority.HIGH);
		ticket2 = ticketRepository.save(ticket2);

		Ticket ticket3 = new Ticket(project.getId(), project.getPrefix() + "-3");
		ticket3.setCreatorId(creatorId);
		ticket3.setPriority(Priority.HIGHEST);
		ticketRepository.save(ticket3);

		// When
		List<Ticket> ticketsHigh = ticketRepository.findByProjectIdAndPriority(project.getId(), Priority.HIGH);
		List<Ticket> ticketsHighest = ticketRepository.findByProjectIdAndPriority(project.getId(), Priority.HIGHEST);

		// Then
		Assert.notEmpty(ticketsHigh, "Failed to find tickets");
		Assert.isTrue(ticketsHigh.size() == 2, "Failed to find exactly two tickets, found: " + ticketsHigh.toString());
		Assert.isTrue(ticketsHigh.containsAll(Arrays.asList(ticket1, ticket2)), "Failed to find matching tickets");

		Assert.notEmpty(ticketsHighest, "Failed to find tickets");
		Assert.isTrue(ticketsHighest.size() == 1, "Failed to find exactly two tickets, found: " + ticketsHigh.toString());
		Assert.isTrue(ticketsHighest.contains(ticket3), "Failed to find matching tickets");

		cleanupProject(project);
	}

	@Test
	public void whenFindByName_thenReturnTicket() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR5");

		project = projectRepository.save(project);

		Ticket ticket = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket.setCreatorId(creatorId);
		ticket = ticketRepository.save(ticket);

		// When
		Optional<Ticket> ticketFound = ticketRepository.findByName(ticket.getName());

		// Then
		Assert.isTrue(ticketFound.isPresent(), "Failed to find ticket");
		Assert.isTrue(ticketFound.get().equals(ticket), "Failed to find matching ticket");

		cleanupProject(project);
	}

	@Test(expected = org.springframework.dao.DuplicateKeyException.class)
	public void whenCreatingTicketWithDuplicateName_thenFailToCreateTicket() {
		// Given
		String creatorId = generateId();
		Project project = new Project(generateName());
		project.setCreatorId(creatorId);
		project.setPrefix("CYR6");

		project = projectRepository.save(project);

		Ticket ticket1 = new Ticket(project.getId(), project.getPrefix() + "-1");
		ticket1.setCreatorId(creatorId);
		ticket1 = ticketRepository.save(ticket1);

		// When
		Ticket ticket2 = new Ticket(project.getId(), ticket1.getName());
		ticketRepository.save(ticket2);

		// Then
		// Throw DuplicateKeyException

		cleanupProject(project);
	}
}
