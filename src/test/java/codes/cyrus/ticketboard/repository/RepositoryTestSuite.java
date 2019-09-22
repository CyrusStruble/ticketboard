package codes.cyrus.ticketboard.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		ProjectRepositoryTest.class,
		TicketRepositoryTest.class,
		UserRepositoryTest.class
})
public class RepositoryTestSuite { }
