package codes.cyrus.ticketboard;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public abstract class CommonTest {

	protected String generateName() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	protected String generateId() {
		return new ObjectId().toString();
	}

	protected String generateEmail() {
		return RandomStringUtils.randomAlphabetic(10) + "@test-it-well.xyz";
	}
}
