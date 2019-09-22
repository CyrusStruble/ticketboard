package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.CommonTest;
import codes.cyrus.ticketboard.UserDetailsTestConfiguration;
import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(UserDetailsTestConfiguration.class)
public abstract class CommonServiceTest extends CommonTest {

	@MockBean
	UserRepository userRepository;

	@Before
	public void setUp() {
		User regularUser = new User("Regular User", UserDetailsTestConfiguration.REGULAR_USER);
		regularUser.setId(new ObjectId().toString());
		Mockito.when(userRepository.findByEmailIgnoreCase(regularUser.getEmail())).thenReturn(Optional.of(regularUser));

		User adminUser = new User("Admin User", UserDetailsTestConfiguration.ADMIN_USER);
		adminUser.setId(new ObjectId().toString());
		adminUser.addRole(Role.ADMIN);
		Mockito.when(userRepository.findByEmailIgnoreCase(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

		User superadminUser = new User("Superadmin User", UserDetailsTestConfiguration.SUPERADMIN_USER);
		superadminUser.setId(new ObjectId().toString());
		superadminUser.addRole(Role.SUPERADMIN);
		Mockito.when(userRepository.findByEmailIgnoreCase(superadminUser.getEmail())).thenReturn(Optional.of(superadminUser));
	}
}
