package pl.wroc.pwr.judy.common;

public class DurationStatistic implements IDurationStatistic {
	private static final long serialVersionUID = 101L;

	private long testsDuration;
	private long initialTestsDuration;
	private long generationDuration;

	private int testMethodsCount;

	@Override
	public long getTestsDuration() {
		return testsDuration;
	}

	@Override
	public long getInitialTestsDuration() {
		return initialTestsDuration;
	}

	@Override
	public long getGenerationDuration() {
		return generationDuration;
	}

	@Override
	public void addTestRun(long time) {
		testsDuration += time;
	}

	@Override
	public void addTestMethods(int methodsCount) {
		testMethodsCount += methodsCount;
	}

	@Override
	public void addInitialTest(long time) {
		initialTestsDuration += time;
	}

	@Override
	public void addMutantGenration(long time) {
		generationDuration += time;
	}

	@Override
	public int getTestMethods() {
		return testMethodsCount;
	}
}