package codes.cyrus.ticketboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	public ForbiddenException() {
		super("You are not authorized to access this resource");
	}

	public ForbiddenException(String message) { super(message); }
}
