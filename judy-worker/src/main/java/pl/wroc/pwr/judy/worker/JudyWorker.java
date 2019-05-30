package pl.wroc.pwr.judy.worker;

import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.cli.AbstractExec;
import pl.wroc.pwr.judy.cli.ConfigException;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.common.WriterException;
import pl.wroc.pwr.judy.worker.argsparser.JudyWorkerConfig;


/**
 * General implementation of Judy worker.
 * 
 * @author pmiwaszko
 */
public class JudyWorker extends AbstractExec {
	private static final Logger LOGGER = LogManager.getLogger(JudyWorker.class);

	/**
	 * <code>Judy</code> constructor.
	 */
	public JudyWorker(final String name, final String description, final String[] args) {
		super(name, description, args);
	}

	/**
	 * Application entry point.
	 */
	public static void main(final String[] args) {
		new JudyWorker("Judy Worker", "worker [options] -w <workspace>", args);
	}

	@Override
	protected void exec(final String[] args) throws RemoteException, InterruptedException, IOException, WorkException,
			MutationException, ConfigException, WriterException {
		LOGGER.info("Parsing input... ");
		final JudyWorkerConfig config = new JudyWorkerConfig(args);
		LOGGER.info("Parsing input... Done");
		final MutationWorker worker = new MutationWorker(config);
		LOGGER.info("Service started...");
		worker.start();
	}
}
