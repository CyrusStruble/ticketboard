package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest extends CommonRepositoryTest {

	@Test
	public void whenFindById_thenReturnUser() {
		// Given
		User user = new User(generateName(), generateEmail());
		user = userRepository.save(user);

		// When
		User userFound = userRepository.findById(user.getId()).get();

		// Then
		assertThat(userFound, equalTo(user));

		cleanupUser(user);
	}

	@Test
	public void whenFindByNameIgnoreCase_thenReturnUser() {
		// Given
		User user1 = new User(generateName(), generateEmail());
		user1 = userRepository.save(user1);

		// When
		List<User> usersFoundWithCase = userRepository.findByNameIgnoreCase(user1.getName());
		List<User> usersFoundIgnoreCase = userRepository.findByNameIgnoreCase(user1.getName().toLowerCase());

		// Then
		assertThat(usersFoundWithCase, hasItem(user1));
		assertThat(usersFoundIgnoreCase, hasItem(user1));

		cleanupUser(user1);
	}

	@Test(expected = org.springframework.dao.DuplicateKeyException.class)
	public void whenCreatingUserWithDuplicateEmail_thenFailToCreateUser() {
		// Given
		User user1 = new User(generateName(), generateEmail());
		user1 = userRepository.save(user1);

		User user2 = new User(generateName(), user1.getEmail());

		// When
		userRepository.save(user2);

		// Then
		// Throw DuplicateKeyException

		cleanupUser(user1);
	}

	@Test
	public void whenFindUserByEmailIgnoreCase_thenReturnUser() {
		// Given
		User user = new User(generateName(), generateEmail());
		user = userRepository.save(user);

		// When
		User userFound = userRepository.findByEmailIgnoreCase(user.getEmail().toUpperCase()).get();

		// Then
		assertThat(userFound, equalTo(user));

		cleanupUser(user);
	}

	@Override
	protected String generateName() {
		return RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
	}
}
