package codes.cyrus.ticketboard.controller;

import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.entity.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import codes.cyrus.ticketboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody UserDto createUser(@Valid @RequestBody UserDto newUser) {
		return userService.createUser(newUser);
	}

	@GetMapping(value = "/{id:^[a-f0-9]{24}$}")
	public @ResponseBody UserDto findUserById(@PathVariable("id") String id) {
		return userService.getUser(id);
	}

	@GetMapping(value = "/{email:.*@.*}")
	public @ResponseBody UserDto findUserByEmail(@PathVariable("email") String email) {
		return userService.getUserByEmail(email);
	}
}
