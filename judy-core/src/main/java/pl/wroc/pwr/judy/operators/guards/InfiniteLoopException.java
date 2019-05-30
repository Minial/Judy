package pl.wroc.pwr.judy.operators.guards;

public class InfiniteLoopException extends RuntimeException {

	private static final long serialVersionUID = 7605677225175399611L;

	public InfiniteLoopException(String description) {
		super(description);
	}
}
