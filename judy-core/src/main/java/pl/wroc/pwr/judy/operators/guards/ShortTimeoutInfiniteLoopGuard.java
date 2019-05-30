package pl.wroc.pwr.judy.operators.guards;

/**
 * Infinite Loop Guard with short timeout, for testing purposes only
 *
 * @author TM
 */
public class ShortTimeoutInfiniteLoopGuard extends InfiniteLoopGuard {
	private static final long DEFAULT_TIMEOUT = 200L;

	@Override
	public long getTimeout() {
		return ShortTimeoutInfiniteLoopGuard.DEFAULT_TIMEOUT;
	}
}