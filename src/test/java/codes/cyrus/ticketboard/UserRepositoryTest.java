package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.entity.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void whenFindById_thenReturnUser() {
		// Given
		String userAName = generateName();
		String userAEmail = generateEmail();
		User userA = new User(userAName, userAEmail);
		userA = userRepository.save(userA);

		// When
		User userFound = userRepository.findUserById(userA.getId());

		// Then
		assertThat(userFound.getId()).isEqualTo(userA.getId());

		cleanupUser(userA);
	}

	@Test
	public void whenFindByNameIgnoreCase_thenReturnUser() {
		// Given
		String userAName = generateName();
		String userAEmail = generateEmail();
		User userA = new User(userAName, userAEmail);
		userA = userRepository.save(userA);

		// When
		List<User> usersFoundWithCase = userRepository.findUserByNameIgnoreCase(userA.getName());
		List<User> usersFoundIgnoreCase = userRepository.findUserByNameIgnoreCase(userA.getName().toLowerCase());

		// Then
		assertThat(usersFoundWithCase.size()).isGreaterThan(0);
		assertThat(usersFoundWithCase.get(0).getName()).isEqualToIgnoringCase(userAName);

		assertThat(usersFoundIgnoreCase.size()).isGreaterThan(0);
		assertThat(usersFoundIgnoreCase.get(0).getName()).isEqualToIgnoringCase(userAName);

		cleanupUser(userA);
	}

	@Test(expected = org.springframework.dao.DuplicateKeyException.class)
//	@Test
	public void whenInsertUser_thenFailToDuplicateEmail() {
		// Given
		String userAName = generateName();
		String userAEmail = generateEmail();
		User userA = new User(userAName, userAEmail);
		userRepository.save(userA);

		User userB = new User(generateName(), userAEmail);

		// When
		userRepository.save(userB);

		// Then
		// throw DuplicateKeyException

		cleanupUser(userA);
	}

	private void cleanupUser(User user) {
		userRepository.deleteUserById(user.getId());
	}

	private static String generateEmail() {
		return RandomStringUtils.randomAlphabetic(10) + "@test-it-well.xyz";
	}

	private static String generateName() {
		return RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
	}
}
