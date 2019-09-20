package codes.cyrus.ticketboard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationEntryPointImpl extends BasicAuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);

	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 : " + authException.getMessage());

		logger.info(authException.getMessage());

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName(SecurityConfig.REALM_NAME);
		super.afterPropertiesSet();
	}
}
