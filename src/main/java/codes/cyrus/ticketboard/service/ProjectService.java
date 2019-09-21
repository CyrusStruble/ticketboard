package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.dto.ProjectDto;
import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.exception.NotAuthorizedException;
import codes.cyrus.ticketboard.exception.ResourceNotFoundException;
import codes.cyrus.ticketboard.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService extends CommonService<ProjectDto, Project> {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserService userService;

	public ProjectDto createProject(ProjectDto projectDto) {
		UserDto requestingUser = userService.getCurrentUser();
		if (!CollectionUtils.containsAny(requestingUser.getRoles(), Arrays.asList(Role.ADMIN, Role.SUPERADMIN))) {
			throw new NotAuthorizedException();
		}

		Project project = new Project(projectDto.getName());
		project.setPrefix(projectDto.getPrefix());
		project.setCreatorId(requestingUser.getId());

		return convertToDto(projectRepository.save(project));
	}

	public void associateUserWithProject(String userId, String projectId) {
		if (!userService.hasAccess(projectId)) {
			throw new NotAuthorizedException();
		}

		Project project = projectRepository.findById(projectId).orElseThrow(ResourceNotFoundException::new);
		project.associateUserId(userId);

		projectRepository.save(project);
	}

	/**
	 * Return the list of projectIds for a given userId (is owner or associated with).
	 * @param userId userId to find projectIds for
	 * @return list of projectIds for a given userId
	 */
	public List<String> getProjectIdsForUserId(String userId) {
		return projectRepository.findByCreatorIdOrAssociatedUserIds(userId, userId)
				.stream().map(Project::getId).collect(Collectors.toList());
	}

	@Override
	protected ProjectDto convertToDto(Project project) {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setAssociatedUserIds(project.getAssociatedUserIds());
		projectDto.setName(project.getName());
		projectDto.setPrefix(project.getPrefix());
		projectDto.setCreateDate(project.getCreateDate());
		projectDto.setUpdateDate(project.getUpdateDate());
		projectDto.setId(project.getId());
		project.setCreatorId(project.getCreatorId());

		return projectDto;
	}
}
