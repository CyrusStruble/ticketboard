package codes.cyrus.ticketboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends RuntimeException {

	public NotAuthorizedException() {
		super("You are not authorized to access this resource");
	}

	public NotAuthorizedException(String message) { super(message); }
}
