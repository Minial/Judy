package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITargetClassesSorter;
import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.util.Collections;

public class TargetClassesSorter implements ITargetClassesSorter {

	/**
	 * Sorts saved source classes using their size and tests time
	 *
	 * @param config  configuration where target classes are specified
	 * @param testRun initial JUnit tests run results
	 */
	@Override
	public void sortTargetClasses(final IClientConfig config, final IInitialTestsRun testRun) {
		if (!testRun.getPassingResults().isEmpty()) {
			final IBytecodeCache cache = config.getInitialTestRunner().getBytecodeCache();
			Collections.sort(config.getTargetClasses(), new TargetClassComparator(cache, testRun));
		}
	}

}
