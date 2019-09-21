package codes.cyrus.ticketboard.controller;

import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.repository.UserRepository;
import codes.cyrus.ticketboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/projects/{projectId}", method = RequestMethod.POST)
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user, @PathVariable("projectId") String projectId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user, projectId));
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(user));
	}

	@GetMapping(value = "/current")
	public ResponseEntity<UserDto> getCurrent() {
		return ResponseEntity.ok(userService.getCurrentUser());
	}

	@DeleteMapping(value ="/{id}")
	public ResponseEntity deleteUser(@PathVariable("id") String id) {
		userService.deleteUser(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping(value = "/{id:^[a-f0-9]{24}$}")
	public ResponseEntity<UserDto> findUserById(@PathVariable("id") String id) {
		return ResponseEntity.ok(userService.getUser(id));
	}

	@GetMapping(value = "/{email:.*@.*}")
	public ResponseEntity<UserDto> findUserByEmail(@PathVariable("email") String email) {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}
}
