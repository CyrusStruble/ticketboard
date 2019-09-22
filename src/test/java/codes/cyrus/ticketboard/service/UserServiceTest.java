package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.UserDetailsTestConfiguration;
import codes.cyrus.ticketboard.document.CommonDocument;
import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.exception.ForbiddenException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends CommonServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.SUPERADMIN_USER)
	public void whenGetUserAsSuperAdmin_thenGetUser() {
		// Given
		User user = new User(generateName(), generateEmail());
		user.setId(generateId());
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// When
		UserDto userDtoFound = userService.getUser(user.getId());

		// Then
		assertThat(user.getName(), equalTo(userDtoFound.getName()));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenGetOwnUserAsRegularUser_thenReturnUser() {
		// Given
		User user = getRegularUser();
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// When
		UserDto userDtoFound = userService.getUser(user.getId());

		// Then
		assertThat(user.getName(), equalTo(userDtoFound.getName()));
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenGetDifferentUserAsRegularUser_thenForbiddenExceptionOccurs() {
		// Given
		User user = new User(generateName(), generateEmail());
		user.setId(generateId());
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// When
		userService.getUser(user.getId());

		// Then
		// ForbiddenException occurs
	}

	@Test
	public void whenSignup_thenUserIsCreated() {
		// Given
		UserDto userDto = new UserDto(generateName(), generateEmail());
		userDto.setPassword(generatePassword());

		// When
		UserDto userDtoCreated = userService.signup(userDto);

		// Then
		assertThat(userDtoCreated.getName(), equalTo(userDto.getName()));
		assertThat(userDtoCreated.getRoles(), hasSize(2));
		assertThat(userDtoCreated.getRoles(), hasItem(Role.ADMIN));
		assertThat(userDtoCreated.getPassword(), not(equalTo(userDto.getPassword())));
		assertThat(passwordEncoder.matches(userDto.getPassword(), userDtoCreated.getPassword()), is(true));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.SUPERADMIN_USER)
	public void whenCreateUserAsSuperAdmin_thenUserIsCreated() {
		// Given
		String projectId = generateId();
		UserDto userDto = new UserDto(generateName(), generateEmail());
		userDto.setPassword(generatePassword());
		Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(new Project(generateName())));

		// When
		Mockito.when(projectRepository.save(any(Project.class))).thenAnswer(i -> {
			CommonDocument document = mockSave.answer(i);

			assertThat(document, instanceOf(Project.class));
			assertThat( ((Project) document).getAssociatedUserIds(), hasSize(1));

			return document;
		});

		UserDto userDtoCreated = userService.createUser(userDto, projectId);

		// Then
		assertThat(userDtoCreated.getName(), equalTo(userDto.getName()));
		assertThat(userDtoCreated.getRoles(), hasSize(1));
		assertThat(userDtoCreated.getRoles(), hasItem(Role.USER));
		assertThat(userDtoCreated.getPassword(), not(equalTo(userDto.getPassword())));
		assertThat(passwordEncoder.matches(userDto.getPassword(), userDtoCreated.getPassword()), is(true));
	}
}
