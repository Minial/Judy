package pl.wroc.pwr.judy;

import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

/**
 * Mutation work factory.
 *
 * @author pmiwaszko, tmuc
 */
public interface IMutationWorkFactory {
	/**
	 * Get instance of mutation work.
	 *
	 * @param id          work id
	 * @param targetClass source class to be mutated
	 * @param envFactory  class hierarchy and structure environment factory
	 * @param MatrixE	  Execution Matrix
	 * @return mutation work
	 */
	IWork create(long id, ITargetClass targetClass, IEnvironmentFactory envFactory, MatrixExecution MatrixE);
}
