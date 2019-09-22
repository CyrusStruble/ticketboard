package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.CommonTest;
import codes.cyrus.ticketboard.UserDetailsTestConfiguration;
import codes.cyrus.ticketboard.document.CommonDocument;
import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.repository.ProjectRepository;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(UserDetailsTestConfiguration.class)
public abstract class CommonServiceTest extends CommonTest {

	private static final User REGULAR_USER;
	private static final User ADMIN_USER;
	private static final User SUPER_ADMIN_USER;

	static {
		REGULAR_USER = new User("Regular User", UserDetailsTestConfiguration.REGULAR_USER);
		REGULAR_USER.setId(new ObjectId().toString());

		ADMIN_USER = new User("Admin User", UserDetailsTestConfiguration.ADMIN_USER);
		ADMIN_USER.setId(new ObjectId().toString());
		ADMIN_USER.addRole(Role.ADMIN);

		SUPER_ADMIN_USER = new User("Superadmin User", UserDetailsTestConfiguration.SUPERADMIN_USER);
		SUPER_ADMIN_USER.setId(new ObjectId().toString());
		SUPER_ADMIN_USER.addRole(Role.SUPERADMIN);
	}

	@MockBean
	UserRepository userRepository;

	@MockBean
	ProjectRepository projectRepository;

	protected Answer<CommonDocument> mockSave = i -> {
		CommonDocument document = i.getArgument(0);

		if (document.getId() == null) {
			document.setId(generateId());
		}

		return document;
	};

	@Before
	public void setUp() {
		// Mock findByEmailIgnoreCase to return mock users
		Mockito.when(userRepository.findByEmailIgnoreCase(REGULAR_USER.getEmail()))
				.thenReturn(Optional.of(ObjectUtils.clone(REGULAR_USER)));

		Mockito.when(userRepository.findByEmailIgnoreCase(ADMIN_USER.getEmail()))
				.thenReturn(Optional.of(ObjectUtils.clone(ADMIN_USER)));

		Mockito.when(userRepository.findByEmailIgnoreCase(SUPER_ADMIN_USER.getEmail()))
				.thenReturn(Optional.of(ObjectUtils.clone(SUPER_ADMIN_USER)));

		// Mock mockSave
		Mockito.when(userRepository.save(any(User.class))).thenAnswer(mockSave);
		Mockito.when(projectRepository.save(any(Project.class))).thenAnswer(mockSave);
	}

	public User getRegularUser() {
		return ObjectUtils.clone(REGULAR_USER);
	}

	public User getAdminUser() {
		return ObjectUtils.clone(ADMIN_USER);
	}

	public User getSuperAdminUser() {
		return ObjectUtils.clone(SUPER_ADMIN_USER);
	}
}
