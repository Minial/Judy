package pl.wroc.pwr.judy.cli;

import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.ITargetClassesSorter;
import pl.wroc.pwr.judy.cli.argsparser.JudyConfig;
import pl.wroc.pwr.judy.cli.result.MutationSummaryFormatter;
import pl.wroc.pwr.judy.client.IMutationClient;
import pl.wroc.pwr.judy.client.IMutationClientFactory;
import pl.wroc.pwr.judy.client.MutationClientFactory;
import pl.wroc.pwr.judy.client.TargetClassesSorter;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.common.WriterException;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * General implementation of Judy.
 *
 * @author pmiwaszko, TM
 */
public class Judy extends AbstractExec {
	public MatrixCoverage MatrixC = new MatrixCoverage();
	public MatrixExecution MatrixE = new MatrixExecution();//add matrix of execution & coverage

	private static final Logger LOGGER = getLogger(Judy.class);

	/**
	 * Judy constructor.
	 *
	 * @param name        application name
	 * @param description application description
	 * @param args        application arguments
	 */
	public Judy(final String name, final String description, final String[] args) {
		super(name, description, args);
	}

	/**
	 * Application entry point.
	 *
	 * @param args application arguments
	 */
	public static void main(final String[] args) {
		new Judy("Judy", "judy [options] -w <workspace> -t <test-classes> -c <classes>", args);
	}

	@Override
	protected void exec(final String[] args) throws InterruptedException, IOException, WorkException, MutationException, ConfigException, WriterException {
		final IClientConfig config = parseUserInput(args);

		final IInitialTestsRun testRun = runInitialTestAnalysis(config);

		final ITargetClassesSorter targetClassesSorter = new TargetClassesSorter();
		targetClassesSorter.sortTargetClasses(config, testRun);
		
		MatrixE = new MatrixExecution();
		MatrixC = new MatrixCoverage();

		final IMutationClientFactory clientFactory = new MutationClientFactory(config, testRun, MatrixE, MatrixC);
		final IMutationClient client = clientFactory.createClient();

		final IMutationResult results = computeMutationResults(config, client);

		terminateExecutor(config);

		printResults(config, results, testRun, MatrixE, MatrixC);
		saveResult(config, results);

		System.exit(0);
	}

	private IMutationResult computeMutationResults(final IClientConfig config, final IMutationClient client) throws InterruptedException, IOException, WorkException, MutationException {
		LOGGER.info("Computing mutation result...");
		LOGGER.info(config.getResultFormatter().getHeader());
		final IMutationResult results = client.compute();
		LOGGER.info("Computing mutation result... Done");
		return results;
	}

	private void terminateExecutor(final IClientConfig config) {
		LOGGER.info("Shutting down executor...");
		config.getExecutor().shutdownNow();
		try {
			boolean terminated = config.getExecutor().awaitTermination(30, TimeUnit.SECONDS);
			LOGGER.info("Shutting down executor... Done (" + terminated + ")");
		} catch (InterruptedException e) {
			LOGGER.info("Shutting down executor... Interrupted");
		}
	}

	private JudyConfig parseUserInput(final String[] args) throws ConfigException {
		LOGGER.info("Parsing input...");
		final JudyConfig config = new JudyConfig(args, MatrixE, MatrixC);
		LOGGER.info("Parsing input... Done");
		return config;
	}

	private IInitialTestsRun runInitialTestAnalysis(final IClientConfig config) throws MutationException {
		LOGGER.info("Initial analysis...");
		final IInitialTestsRun testRun = config.getInitialTestRunner().run();
		LOGGER.info("Initial analysis... Done");
		return testRun;
	}

	private void printResults(final IClientConfig config, final IMutationResult results, final IInitialTestsRun testRun, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		for (final String line : new MutationSummaryFormatter().getSummary(results, testRun, MatrixE, MatrixC).split("\n")) {
			System.out.println(line);																						//AJOUT
			LOGGER.info(line);
		}
		LOGGER.info("");
	}

	private void saveResult(final IClientConfig config, final IMutationResult results) throws WriterException {
		LOGGER.info("Saving result... ");
		config.getResultWriter().write(results, config.showKilledMutants());
		LOGGER.info("Saving result... Done ");
	}

}
