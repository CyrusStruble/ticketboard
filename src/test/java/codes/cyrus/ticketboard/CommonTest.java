package codes.cyrus.ticketboard;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;

public abstract class CommonTest {

	protected String generateName() {
		return RandomStringUtils.randomAlphabetic(10);
	}

	protected String generateUserId() {
		return new ObjectId().toString();
	}

	protected String generateEmail() {
		return RandomStringUtils.randomAlphabetic(10) + "@test-it-well.xyz";
	}
}
