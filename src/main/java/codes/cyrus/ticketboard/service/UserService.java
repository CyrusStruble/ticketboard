package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.exception.NotAuthorizedException;
import codes.cyrus.ticketboard.exception.ResourceNotFoundException;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CommonService<UserDto, User> {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Sign up as a new user.
	 * @param userDto the user to create
	 * @return the created user
	 */
	public UserDto signup(UserDto userDto) {
		User user = new User(userDto.getName(), userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		// We default users created through this process as admins for demo purposes
		user.addRole(Role.ADMIN);

		return convertToDto(userRepository.save(user));
	}

	/**
	 * Register a new user for a specific project as an admin.
	 * @param userDto the user to create
	 * @param projectId the project to assign the new user to
	 * @return the created user
	 */
	public UserDto createUser(UserDto userDto, String projectId) {
		UserDto requestingUser = getCurrentUser();
		if (!hasAccess(requestingUser, projectId)) {
			throw new NotAuthorizedException();
		}

		User user = new User(userDto.getName(), userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setCreatorId(requestingUser.getId());

		user = userRepository.save(user);

		// Associate user with project
		projectService.associateUserWithProject(user.getId(), projectId);

		return convertToDto(user);
	}

	public UserDto getUser(String id) {
		User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

		return convertToDto(user);
	}

	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);

		return convertToDto(user);
	}

	public UserDto getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			return getUserByEmail(userDetails.getUsername());
		}

		logger.info("Unexpected principal found: " + principal.toString());
		return null;
	}

	/**
	 * Determine whether the current user has access to modify other users in the given project.
	 * @param projectId projectId to determine access on
	 * @return true if the current user has access
	 */
	public boolean hasAccess(String projectId) {
		return hasAccess(getCurrentUser(), projectId);
	}

	/**
	 * Determine whether the given user has access to modify other users in the given project.
	 * @param projectId projectId to determine access on
	 * @return true if the current user has access
	 */
	public boolean hasAccess(UserDto user, String projectId) {
		UserDto requestingUser = getCurrentUser();

		if (requestingUser == null) {
			return false;
		}

		if (requestingUser.getRoles().contains(Role.SUPERADMIN)) {
			return true;
		}

		// Admins have access if it is a project they are associated with
		if (requestingUser.getRoles().contains(Role.ADMIN)) {
			return projectService.getProjectIdsForUserId(requestingUser.getId()).contains(projectId);
		}

		return false;
	}

	protected UserDto convertToDto(User user) {
		UserDto userDto = new UserDto(user.getName(), user.getEmail());
		userDto.setId(user.getId());
		userDto.setCreateDate(user.getCreateDate());
		userDto.setUpdateDate(user.getUpdateDate());
		userDto.setRoles(user.getRoles());

		return userDto;
	}
}
