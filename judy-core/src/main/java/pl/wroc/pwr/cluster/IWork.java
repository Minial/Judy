package pl.wroc.pwr.cluster;

import pl.wroc.pwr.judy.IClassMutationResult;

import java.io.Serializable;

/**
 * Work item that should be processed.
 *
 * @author pmiwaszko
 */
public interface IWork extends Serializable {
	/**
	 * Get id of client which scheduled this job.
	 */
	long getClientId();

	/**
	 * Run work and return result.
	 */
	IClassMutationResult run(Object param) throws Exception;

	/**
	 * Can this work be retried any more?
	 */
	boolean canRetry();
}
