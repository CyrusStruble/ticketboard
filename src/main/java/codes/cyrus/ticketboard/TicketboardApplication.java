package codes.cyrus.ticketboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "codes.cyrus.ticketboard.repository")
public class TicketboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketboardApplication.class, args);
	}

}
