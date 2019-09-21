package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.CommonTest;
import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
abstract class CommonRepositoryTest extends CommonTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TicketRepository ticketRepository;

	void cleanupProject(Project... projects) {
		for (Project project : projects) {
			projectRepository.deleteById(project.getId());
			ticketRepository.deleteByProjectId(project.getId());
		}
	}

	void cleanupUser(User... users) {
		for (User user : users) {
			userRepository.deleteById(user.getId());
		}
	}
}
