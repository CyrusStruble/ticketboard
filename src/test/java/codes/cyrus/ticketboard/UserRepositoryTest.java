package codes.cyrus.ticketboard;

import codes.cyrus.ticketboard.entity.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

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
		List<User> usersFoundWithCase = userRepository.findUserByNameIgnoreCase(user1.getName());
		List<User> usersFoundIgnoreCase = userRepository.findUserByNameIgnoreCase(user1.getName().toLowerCase());

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
		User userFound = userRepository.findUserByEmail(user.getEmail()).get();

		// Then
		Assert.notNull(userFound, "No user found");
		Assert.isTrue(user.getId().equals(userFound.getId()), "No user found");

		cleanupUser(user);
	}

	private void cleanupUser(User user) {
		userRepository.deleteById(user.getId());
	}

	private static String generateEmail() {
		return RandomStringUtils.randomAlphabetic(10) + "@test-it-well.xyz";
	}

	private static String generateName() {
		return RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
	}
}
