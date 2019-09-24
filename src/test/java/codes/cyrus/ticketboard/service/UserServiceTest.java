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

import java.util.Collections;
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
		Mockito.verify(userRepository).findById(user.getId());
		assertThat(user.getName(), equalTo(userDtoFound.getName()));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenGetUserAsAdminWithAccess_thenGetUser() {
		// Given
		User adminUser = getAdminUser();

		User user = new User(generateName(), generateEmail());
		user.setId(generateId());
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		String projectId = generateId();
		Project project = new Project(generateName());
		project.setId(projectId);
		project.associateUserId(adminUser.getId());

		Mockito.when(projectRepository.findByCreatorIdOrAssociatedUserIds(adminUser.getId(), adminUser.getId()))
				.thenReturn(Collections.singletonList(project));
		Mockito.when(projectRepository.findByCreatorIdOrAssociatedUserIds(user.getId(), user.getId()))
				.thenReturn(Collections.singletonList(project));

		// When
		UserDto userDtoFound = userService.getUser(user.getId());

		// Then
		Mockito.verify(userRepository).findById(user.getId());
		assertThat(user.getName(), equalTo(userDtoFound.getName()));
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenGetUserAsAdminWithoutAccess_thenForbiddenExceptionOccurs() {
		// Given
		String userId = generateId();

		// When
		userService.getUser(userId);

		// Then
		// ForbiddenException occurs
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
		Mockito.verify(userRepository).findById(user.getId());
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
		Mockito.verify(userRepository).save(any(User.class));
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
		Mockito.verify(projectRepository).save(any(Project.class));
		Mockito.verify(userRepository).save(any(User.class));
		assertThat(userDtoCreated.getName(), equalTo(userDto.getName()));
		assertThat(userDtoCreated.getRoles(), hasSize(1));
		assertThat(userDtoCreated.getRoles(), hasItem(Role.USER));
		assertThat(userDtoCreated.getPassword(), not(equalTo(userDto.getPassword())));
		assertThat(passwordEncoder.matches(userDto.getPassword(), userDtoCreated.getPassword()), is(true));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenCreateUserAsAdminWithAccess_thenUserIsCreated() {
		// Given
		User adminUser = getAdminUser();
		String projectId = generateId();
		Project project = new Project(generateName());
		project.setId(projectId);
		project.associateUserId(adminUser.getId());

		UserDto userDto = new UserDto(generateName(), generateEmail());
		userDto.setPassword(generatePassword());
		Mockito.when(projectRepository.findByCreatorIdOrAssociatedUserIds(adminUser.getId(), adminUser.getId()))
				.thenReturn(Collections.singletonList(project));
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
		Mockito.verify(projectRepository).save(any(Project.class));
		Mockito.verify(userRepository).save(any(User.class));
		assertThat(userDtoCreated.getName(), equalTo(userDto.getName()));
		assertThat(userDtoCreated.getRoles(), hasSize(1));
		assertThat(userDtoCreated.getRoles(), hasItem(Role.USER));
		assertThat(userDtoCreated.getPassword(), not(equalTo(userDto.getPassword())));
		assertThat(passwordEncoder.matches(userDto.getPassword(), userDtoCreated.getPassword()), is(true));
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenCreateUserAsAdminWithoutAccess_thenForbiddenExceptionOccurs() {
		// Given
		String projectId = generateId();

		UserDto userDto = new UserDto(generateName(), generateEmail());
		userDto.setPassword(generatePassword());
		Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(new Project(generateName())));

		// When
		userService.createUser(userDto, projectId);

		// Then
		// ForbiddenException occurs
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenCreateUserAsRegularUser_thenForbiddenExceptionOccurs() {
		// Given
		String projectId = generateId();

		UserDto userDto = new UserDto(generateName(), generateEmail());
		userDto.setPassword(generatePassword());
		Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(new Project(generateName())));

		// When
		userService.createUser(userDto, projectId);

		// Then
		// ForbiddenException occurs
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenGetCurrentUser_thenCurrentUserIsReturned() {
		// Given
		User user = getRegularUser();

		// When
		UserDto userDtoFound = userService.getCurrentUser();

		// Then
		Mockito.verify(userRepository).findByEmailIgnoreCase(user.getEmail());
		assertThat(userDtoFound.getId(), equalTo(user.getId()));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.SUPERADMIN_USER)
	public void whenDeleteUserAsSuperAdmin_thenUserIsDeleted() {
		// Given
		String userId = generateId();

		// When
		userService.deleteUser(userId);

		// Then
		Mockito.verify(userRepository).deleteById(userId);
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenDeleteUserAsAdminWithAccessAsMember_thenUserIsDeleted() {
		// Given
		User adminUser = getAdminUser();

		String userId = generateId();

		String projectId = generateId();
		Project project = new Project(generateName());
		project.setId(projectId);
		project.associateUserId(adminUser.getId());

		Mockito.when(projectRepository.findByCreatorIdOrAssociatedUserIds(adminUser.getId(), adminUser.getId()))
				.thenReturn(Collections.singletonList(project));
		Mockito.when(projectRepository.findByCreatorIdOrAssociatedUserIds(userId, userId))
				.thenReturn(Collections.singletonList(project));

		// When
		userService.deleteUser(userId);

		// Then
		Mockito.verify(userRepository).deleteById(userId);
	}


	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenDeleteUserAsAdminWithoutAccess_thenForbiddenExceptionOccurs() {
		// Given
		String userId = generateId();

		// When
		userService.deleteUser(userId);

		// Then
		// ForbiddenException occurs
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenDeleteUserAsRegularUser_thenForbiddenExceptionOccurs() {
		// Given
		String userId = generateId();

		// When
		userService.deleteUser(userId);

		// Then
		// ForbiddenException occurs
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenDeleteOwnUserAsRegularUser_thenUserIsDeleted() {
		// Given
		User user = getRegularUser();

		// When
		userService.deleteUser(user.getId());

		// Then
		Mockito.verify(userRepository).deleteById(user.getId());
	}
}
