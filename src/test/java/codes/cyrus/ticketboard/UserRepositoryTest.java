package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.document.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;

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
		Assert.notNull(userFound, "No user found");
		Assert.isTrue(user.getId().equals(userFound.getId()), "No user found");

		cleanupUser(user);
	}

	@Test
	public void whenFindByNameIgnoreCase_thenReturnUser() {
		// Given
		String userAName = generateName();
		String userAEmail = generateEmail();
		User user1 = new User(userAName, userAEmail);
		user1 = userRepository.save(user1);

		// When
		List<User> usersFoundWithCase = userRepository.findByNameIgnoreCase(user1.getName());
		List<User> usersFoundIgnoreCase = userRepository.findByNameIgnoreCase(user1.getName().toLowerCase());

		// Then
		Assert.notEmpty(usersFoundWithCase, "No users found");
		Assert.isTrue(usersFoundWithCase.get(0).getName().equalsIgnoreCase(userAName), "User not found with exact case");

		Assert.notEmpty(usersFoundIgnoreCase, "No users found");
		Assert.isTrue(usersFoundIgnoreCase.get(0).getName().equalsIgnoreCase(userAName), "User not found with inexact case");

		cleanupUser(user1);
	}

	@Test(expected = org.springframework.dao.DuplicateKeyException.class)
	public void whenInsertUser_thenFailToDuplicateEmail() {
		// Given
		String userAName = generateName();
		String userAEmail = generateEmail();
		User user1 = new User(userAName, userAEmail);
		user1 = userRepository.save(user1);

		User user2 = new User(generateName(), userAEmail);

		// When
		userRepository.save(user2);

		// Then
		// throw DuplicateKeyException

		cleanupUser(user1);
	}

	@Test
	public void whenFindUserByEmail_thenReturnUser() {
		// Given
		User user = new User(generateName(), generateEmail());
		user = userRepository.save(user);

		// When
		User userFound = userRepository.findByEmail(user.getEmail()).get();

		// Then
		Assert.notNull(userFound, "No user found");
		Assert.isTrue(user.getId().equals(userFound.getId()), "No user found");

		cleanupUser(user);
	}

	@Override
	String generateName() {
		return RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
	}
}
