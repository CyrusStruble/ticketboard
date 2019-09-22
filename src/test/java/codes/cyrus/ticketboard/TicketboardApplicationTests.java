package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.repository.RepositoryTestSuite;
import codes.cyrus.ticketboard.service.ServiceTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		RepositoryTestSuite.class,
		ServiceTestSuite.class
})
public class TicketboardApplicationTests { }
