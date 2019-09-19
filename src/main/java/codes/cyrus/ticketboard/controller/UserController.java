package codes.cyrus.ticketboard.controller;

import codes.cyrus.ticketboard.entity.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody User createUser(@RequestBody User newUser) {
		return userRepository.save(newUser);
	}
}
