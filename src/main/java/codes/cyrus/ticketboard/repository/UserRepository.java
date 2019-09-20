package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
	List<User> findUserByNameIgnoreCase(String name);
	Optional<User> findUserByEmail(String email);
}
