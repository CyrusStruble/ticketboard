package codes.cyrus.ticketboard.service;

import codes.cyrus.ticketboard.document.Role;
import codes.cyrus.ticketboard.document.User;
import codes.cyrus.ticketboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = userRepository.findByEmailIgnoreCase(s).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		logger.info("Authenticating user: " + user.toString());

		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()));

		logger.info(userDetails.toString());

		return userDetails;
	}

	private static List<GrantedAuthority> getAuthorities(Set<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.toString()))
				.collect(Collectors.toList());
	}
}
