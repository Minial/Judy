package pl.wroc.pwr.judy;

/**
 * Configuration of JudyExec worker. Implementations of this class can e.g.
 * parse application's arguments.
 *
 * @author pmiwaszko
 */
public interface IWorkerConfig {
	/**
	 * Get number of worker's threads.
	 */
	int getThreadsCount();

	/**
	 * Get workspace path. Workspace is a directory containing all target and
	 * test <code>.class</code> files, libraries and optional sources. All other
	 * paths are relative to workspace.
	 */
	String getWorkspace();
}
