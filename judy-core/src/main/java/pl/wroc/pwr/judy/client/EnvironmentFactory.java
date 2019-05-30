package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;

import java.util.Map;

/**
 * Environment factory.
 */
public class EnvironmentFactory implements IEnvironmentFactory {
	private static final long serialVersionUID = 345L;

	private final Map<String, String> inheritance;

	/**
	 * Default constructor.
	 *
	 * @param inheritance source classes inheritance data
	 */
	public EnvironmentFactory(Map<String, String> inheritance) {
		this.inheritance = inheritance;
	}

	@Override
	public IEnvironment create(IBytecodeCache cache) {
		return new Environment(cache, new ClassHierarchy(inheritance));
	}
}