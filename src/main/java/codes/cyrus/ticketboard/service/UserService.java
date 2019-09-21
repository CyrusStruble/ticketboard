package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.exception.ForbiddenException;
import codes.cyrus.ticketboard.exception.ResourceNotFoundException;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
	 * Register a new user for a specific project.
	 * @param userDto the user to create
	 * @param projectId the project to assign the new user to
	 * @return the created user
	 */
	public UserDto createUser(UserDto userDto, String projectId) {
		UserDto requestingUser = getCurrentUser();
		if (!hasAccessOnProject(requestingUser, projectId)) {
			throw new ForbiddenException();
		}

		User user = new User(userDto.getName(), userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setCreatorId(requestingUser.getId());

		user = userRepository.save(user);

		// Associate user with project
		projectService.associateUserWithProject(user.getId(), projectId);

		return convertToDto(user);
	}

	/**
	 * Delete a user with the given userId.
	 * @param userId the userId to delete
	 */
	public void deleteUser(String userId) {
		if (!hasAccessOnUser(userId)) {
			throw new ForbiddenException();
		}

		userRepository.deleteById(userId);
	}

	/**
	 * Search for a user with the given userId.
	 * @param userId userId to search for
	 * @return user with matching userId
	 */
	public UserDto getUser(String userId) {
		if (!hasAccessOnUser(userId)) {
			throw new ForbiddenException();
		}

		User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);

		return convertToDto(user);
	}

	/**
	 * Get a user by their email address.
	 * @param email email address to search for
	 * @return user with matching email address
	 */
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(ResourceNotFoundException::new);

		if (!hasAccessOnUser(user.getId())) {
			throw new ForbiddenException();
		}

		return convertToDto(user);
	}

	/**
	 * Get the currently authenticated user.
	 * @return currently authenticated user
	 */
	public UserDto getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			return convertToDto(userRepository
					.findByEmailIgnoreCase(userDetails.getUsername())
					.orElseThrow(ResourceNotFoundException::new));
		}

		logger.info("Unexpected principal found: " + principal.toString());
		return null;
	}

	/**
	 * Determine whether the current user has access to modify the target user.
	 * @param targetUserId userId to determine access on
	 * @return true if the current user has access
	 */
	public boolean hasAccessOnUser(String targetUserId) {
		return hasAccessOnUser(getCurrentUser(), targetUserId);
	}

	/**
	 * Determine whether the given user has access to modify the target user.
	 * @param requestingUser user to determine access for
	 * @param targetUserId userId to determine access on
	 * @return true if the given user has access
	 */
	public boolean hasAccessOnUser(UserDto requestingUser, String targetUserId) {
		if (requestingUser == null) {
			return false;
		}

		if (requestingUser.getRoles().contains(Role.SUPERADMIN)) {
			return true;
		}

		if (requestingUser.getId().equals(targetUserId)) {
			return true;
		}

		// Admins have access if the user is in any project they have access to
		if (requestingUser.getRoles().contains(Role.ADMIN)) {
			List<String> requestingUserProjectIds = projectService.getProjectIdsForUserId(requestingUser.getId());
			List<String> targetUserProjectIds = projectService.getProjectIdsForUserId(targetUserId);

			requestingUserProjectIds.retainAll(targetUserProjectIds);

			return (requestingUserProjectIds.size() > 0);
		}

		return false;
	}

	/**
	 * Determine whether the current user has access to modify the given project.
	 * @param projectId projectId to determine access on
	 * @return true if the current user has access
	 */
	public boolean hasAccessOnProject(String projectId) {
		return hasAccessOnProject(getCurrentUser(), projectId);
	}

	/**
	 * Determine whether the given user has access to modify the given project.
	 * @param projectId projectId to determine access on
	 * @return true if the current user has access
	 */
	public boolean hasAccessOnProject(UserDto requestingUser, String projectId) {
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
