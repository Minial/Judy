package pl.wroc.pwr.judy.cli;

import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Exception indicating that help message should be printed.
 *
 * @author pmiwaszko
 */
public class HelpException extends ConfigException {

	private static final long serialVersionUID = 285L;
	private static final Logger LOGGER = getLogger(HelpException.class);

	private final String message;

	/**
	 * <code>HelpException</code> constructor.
	 */
	public HelpException(final String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Print help message.
	 */
	public void printHelpMessage() {
		LOGGER.error(message);
	}

}
