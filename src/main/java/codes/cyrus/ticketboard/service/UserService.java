package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.entity.User;
import codes.cyrus.ticketboard.exception.ResourceNotFoundException;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDto createUser(UserDto userDto) {
		User user = new User(userDto.getName(), userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		user = userRepository.save(user);
		userDto.setId(user.getId());

		return userDto;
	}

	public UserDto getUser(String id) {
		User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

		return new UserDto(user.getName(), user.getEmail(), user.getId());
	}

	public UserDto getUserByEmail(String email) {
		User user = userRepository.findUserByEmail(email).orElseThrow(ResourceNotFoundException::new);

		return new UserDto(user.getName(), user.getEmail(), user.getId());
	}
}
