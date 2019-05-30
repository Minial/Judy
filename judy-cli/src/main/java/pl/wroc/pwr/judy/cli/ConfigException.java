package pl.wroc.pwr.judy.cli;

/**
 * Exception indicating problem during creation of application configuration.
 *
 * @author pmiwaszko
 */
public class ConfigException extends Exception {

	private static final long serialVersionUID = 956L;

	/**
	 * <code>ConfigException</code> constructor.
	 */
	public ConfigException(String message) {
		super(message);
	}

	/**
	 * <code>ConfigException</code> constructor.
	 */
	public ConfigException(Throwable cause) {
		super(cause);
	}

}
