package pl.wroc.pwr.judy.utils;

/**
 * Simple wrapper for {@link System#currentTimeMillis()} method.
 *
 * @author pmiwaszko
 */
public class Timer {
	private final long generationTime;

	/**
	 * <code>Timer</code> constructor.
	 */
	public Timer() {
		generationTime = System.currentTimeMillis();
	}

	/**
	 * Get time elapsed from this timer creation
	 *
	 * @return time in milliseconds
	 */
	public long getDuration() {
		return System.currentTimeMillis() - generationTime;
	}
}
