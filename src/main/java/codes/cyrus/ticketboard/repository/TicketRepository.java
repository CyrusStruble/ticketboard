package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.Priority;
import codes.cyrus.ticketboard.document.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	List<Ticket> findByProjectId(String projectId);
	Long countByProjectId(String projectId);
	List<Ticket> findByProjectIdAndPriority(String projectId, Priority priority);
	Long countByProjectIdAndPriority(String projectId, Priority priority);
	Ticket findByName(String name);
	void deleteByProjectId(String projectId);
}
