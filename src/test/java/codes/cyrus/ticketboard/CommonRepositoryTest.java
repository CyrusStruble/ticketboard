package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.repository.ProjectRepository;
import codes.cyrus.ticketboard.repository.TicketRepository;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommonRepositoryTest {

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

	String generateName() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	String generateUserId() {
		return new ObjectId().toString();
	}

	String generateEmail() {
		return RandomStringUtils.randomAlphabetic(10) + "@test-it-well.xyz";
	}
}
