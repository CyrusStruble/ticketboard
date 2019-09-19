package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
	User findUserById(String id);
	List<User> findUserByNameIgnoreCase(String name);
	void deleteUserById(String id);
}
