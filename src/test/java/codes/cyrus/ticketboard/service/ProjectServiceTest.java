package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.UserDetailsTestConfiguration;
import codes.cyrus.ticketboard.document.Project;
import codes.cyrus.ticketboard.dto.ProjectDto;
import codes.cyrus.ticketboard.exception.ForbiddenException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceTest extends CommonServiceTest {

	@Autowired
	private ProjectService projectService;

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.SUPERADMIN_USER)
	public void whenCreateProjectAsSuperAdmin_thenCreateProject() {
		// Given
		ProjectDto projectDto = new ProjectDto();
		projectDto.setName(generateName());
		projectDto.setPrefix(generateName());

		// When
		ProjectDto projectDtoCreated = projectService.createProject(projectDto);

		// Then
		Mockito.verify(projectRepository).save(any(Project.class));
		assertThat(projectDtoCreated.getName(), equalTo(projectDto.getName()));
	}

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.ADMIN_USER)
	public void whenCreateProjectAsAdmin_thenCreateProject() {
		// Given
		ProjectDto projectDto = new ProjectDto();
		projectDto.setName(generateName());
		projectDto.setPrefix(generateName());

		// When
		ProjectDto projectDtoCreated = projectService.createProject(projectDto);

		// Then
		Mockito.verify(projectRepository).save(any(Project.class));
		assertThat(projectDtoCreated.getName(), equalTo(projectDto.getName()));
	}

	@Test(expected = ForbiddenException.class)
	@WithUserDetails(UserDetailsTestConfiguration.REGULAR_USER)
	public void whenCreateProjectAsRegularUser_thenForbiddenExceptionOccurs() {
		// Given
		ProjectDto projectDto = new ProjectDto();
		projectDto.setName(generateName());
		projectDto.setPrefix(generateName());

		// When
		projectService.createProject(projectDto);

		// Then
		// Throw ForbiddenException
	}
}
