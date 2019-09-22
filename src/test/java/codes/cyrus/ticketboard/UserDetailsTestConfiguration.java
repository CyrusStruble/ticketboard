package codes.cyrus.ticketboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Profile(value = "test")
@TestConfiguration
public class UserDetailsTestConfiguration {

	public static final String REGULAR_USER = "regular_user@test.test-it-well.xyz";
	public static final String ADMIN_USER = "admin_user@test.test-it-well.xyz";
	public static final String SUPERADMIN_USER = "superadmin_user@test.test-it-well.xyz";

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsTestConfiguration.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		UserDetails regularUser = new User(REGULAR_USER, passwordEncoder.encode("password"),
				Arrays.asList(new SimpleGrantedAuthority("USER")));

		UserDetails adminUser = new User(ADMIN_USER, passwordEncoder.encode("password"),
				Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")));

		UserDetails superAdminUser = new User(SUPERADMIN_USER, passwordEncoder.encode("password"),
				Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("SUPERADMIN")));


		return new InMemoryUserDetailsManager(Arrays.asList(
				regularUser, adminUser, superAdminUser
		));
	}
}
