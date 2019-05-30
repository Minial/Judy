package pl.wroc.pwr.judy.common;

import java.io.Serializable;

/**
 * Factory of environments.
 */
public interface IEnvironmentFactory extends Serializable {
	/**
	 * Create classes and hierarchy environment
	 *
	 * @param cache source classes cache
	 * @return evironment
	 */
	IEnvironment create(IBytecodeCache cache);
}
