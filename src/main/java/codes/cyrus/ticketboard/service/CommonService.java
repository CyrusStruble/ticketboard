package codes.cyrus.ticketboard.service;

public abstract class CommonService<T, S> {

	protected abstract T convertToDto(S obj);
}
