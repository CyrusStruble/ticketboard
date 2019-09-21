package codes.cyrus.ticketboard.controller;

import codes.cyrus.ticketboard.dto.ProjectDto;
import codes.cyrus.ticketboard.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PostMapping
	public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectDto));
	}
}
