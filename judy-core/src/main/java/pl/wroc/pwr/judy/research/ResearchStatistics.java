package pl.wroc.pwr.judy.research;

import pl.wroc.pwr.cluster.ISubresult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ResearchStatistics {

	private static long start;
	private static long testsDuration;
	private static long generationDuration;
	private static long initialTestsStart;
	private static long initialTestsDuration;
	private static final Object lock = new Object();
	private static Map<String, Long> workers = new ConcurrentHashMap<>();

	private ResearchStatistics() {
	}

	public static void initialTestsStarted() {
		initialTestsStart = System.currentTimeMillis();
	}

	public static void initialTestsStopped() {
		initialTestsDuration = System.currentTimeMillis() - initialTestsStart;
	}

	public static void subtaskTaken(String workerId) {
		if (workers.isEmpty()) {
			start = System.currentTimeMillis();
		}
		if (!workers.containsKey(workerId)) {
			workers.put(workerId, System.currentTimeMillis());
		}
	}

	public static void subtaskReturned(String workerId, ISubresult subresult) {
		synchronized (lock) {
			testsDuration += subresult.getTestsDuration();
			generationDuration += subresult.getGenerationDuration();
		}
	}

	public static Map<String, Long> getWorkers() {
		return workers;
	}

	public static long getInitialTestsDuration() {
		return initialTestsDuration;
	}

	public static long getMutantsGenerationDuration() {
		return generationDuration;
	}

	public static long getTestsDuration() {
		return testsDuration;
	}

	public static long getDuration() {
		return initialTestsDuration + System.currentTimeMillis() - start;
	}

}
