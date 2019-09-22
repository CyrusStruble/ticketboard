package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.UserDetailsTestConfiguration;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends CommonServiceTest {

	@Autowired
	private UserService userService;

	@Test
	@WithUserDetails(UserDetailsTestConfiguration.SUPERADMIN_USER)
	public void whenGetUserAsSuperadmin_thenGetUser() {
		// Given
		User user = new User(generateName(), generateEmail());
		Mockito.when(userRepository.findById("SomeId")).thenReturn(Optional.of(user));

		// When
		UserDto userDtoFound = userService.getUser("SomeId");

		// Then
		Assert.isTrue(user.getName().equals(userDtoFound.getName()), "Failed to retrieve user");
	}
}
