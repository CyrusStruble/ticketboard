package codes.cyrus.ticketboard.repository;

import codes.cyrus.ticketboard.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
	List<User> findByNameIgnoreCase(String name);
	Optional<User> findByEmail(String email);
}
