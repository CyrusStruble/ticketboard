package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.repository.ProjectRepositoryTest;
import codes.cyrus.ticketboard.repository.TicketRepositoryTest;
import codes.cyrus.ticketboard.repository.UserRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		ProjectRepositoryTest.class,
		TicketRepositoryTest.class,
		UserRepositoryTest.class
})
public class TicketboardApplicationTests {

	@Test
	public void contextLoads() {
	}

}
