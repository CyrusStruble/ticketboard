package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.dto.UserDto;
import codes.cyrus.ticketboard.document.User;
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

	public static UserDto convertToDto(User user) {
		UserDto userDto = new UserDto(user.getName(), user.getEmail());
		userDto.setId(user.getId());
		userDto.setCreateDate(user.getCreateDate());
		userDto.setUpdateDate(user.getUpdateDate());

		return userDto;
	}
}
