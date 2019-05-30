package pl.wroc.pwr.judy.cli;

import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.common.WriterException;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Abstract base class for applications entry points.
 *
 * @author pmiwaszko
 */
public abstract class AbstractExec {

	private static final Logger LOGGER = getLogger(AbstractExec.class);
	private final String name;
	private final String description;

	/**
	 * <code>AbstractExec</code> constructor.
	 */
	public AbstractExec(final String name, final String description, final String[] args) {
		this.name = name;
		this.description = description;
		try {
			LOGGER.info(this.name + ", " + new GregorianCalendar().getTime());
			exec(args);
		} catch (final HelpException e) {
			help(e);
		} catch (final ConfigException e) {
			error("Configuration error", e.getMessage());
		} catch (final MutationException e) {
			dump(e);
			error("Mutation error", e.getMessage());
		} catch (final InterruptedException e) {
			dump(e);
			error("Interrupted, unable to get result", e.getMessage());
		} catch (final WriterException e) {
			dump(e);
			error("Writer error", e.getMessage());
		} catch (final RemoteException e) {
			dump(e);
			error("Connection error", e.getMessage());
		} catch (final UndeclaredThrowableException e) {
			dump(e);
			error("Connection error", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		} catch (final IOException e) {
			dump(e);
			error("I/O error", e.getMessage());
		} catch (final WorkException e) {
			dump(e);
			error("Error on the cluster", e.getMessage());
		}
	}

	protected abstract void exec(String[] args) throws InterruptedException, IOException, WorkException, MutationException, ConfigException, WriterException;

	private void error(final String type, final String message) {
		LOGGER.error(type + ": " + message + "!");
		System.exit(1);
	}

	private void dump(final Exception e) {
		final String dumpName = name.toLowerCase().replaceAll("\\s", "-") + "-dump-" + System.currentTimeMillis();
		try {
			final PrintWriter writer = new PrintWriter(dumpName);

			writer.println(name + ", " + new GregorianCalendar().getTime().toString());
			e.printStackTrace(writer);
			writer.close();
		} catch (final FileNotFoundException ex) {
			LOGGER.error("File not found: " + dumpName + " (" + ex.getMessage() + ")");
		}
	}

	private void help(final HelpException e) {
		LOGGER.error("Usage");
		LOGGER.error("-----");
		LOGGER.error(description);
		e.printHelpMessage();
		System.exit(1);
	}

}
