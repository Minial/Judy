package pl.wroc.pwr.cluster;

/**
 * Abstract base class for {@link IWork} implementations.
 *
 * @author pmiwaszko
 */
public abstract class AbstractWork implements IWork {
	private final long clientId;
	private volatile int retries;
	private static final long serialVersionUID = 999L;

	/**
	 * <code>AbstractWork</code> constructor.
	 */
	public AbstractWork(long clientId, int retries) {
		this.clientId = clientId;
		this.retries = retries;
	}

	@Override
	public long getClientId() {
		return clientId;
	}

	@Override
	public synchronized boolean canRetry() {
		return retries-- > 0;
	}

	public int getRetries() {
		return retries;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
