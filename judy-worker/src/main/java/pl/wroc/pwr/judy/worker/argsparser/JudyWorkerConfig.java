package pl.wroc.pwr.judy.worker.argsparser;

import pl.wroc.pwr.judy.IWorkerConfig;
import pl.wroc.pwr.judy.cli.ConfigException;


/**
 * This implementation of {@link IWorkerConfig} interface takes program arguments and parses them using
 * {@link JudyWorkerArgsParser}.
 * 
 * @author pmiwaszko
 */
public class JudyWorkerConfig implements IWorkerConfig {
	private final String workspace;
	private final int threadsCount;

	/**
	 * <code>JudyWorkerConfig</code> constructor.
	 */
	public JudyWorkerConfig(final String[] args) throws ConfigException {
		final JudyWorkerArgsParser parser = new JudyWorkerArgsParser(args);
		this.workspace = parser.getWorkspace();
		this.threadsCount = parser.getThreadsCount();
	}

	/**
	 * <code>JudyWorkerConfig</code> constructor.
	 */
	public JudyWorkerConfig(final String workspace, final int threadsCount) {
		this.workspace = workspace;
		this.threadsCount = threadsCount;
	}

	@Override
	public int getThreadsCount() {
		return this.threadsCount;
	}

	@Override
	public String getWorkspace() {
		return this.workspace;
	}
}
