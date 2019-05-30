package pl.wroc.pwr.judy.work;

public class TestDuration {
	private final String testClassName;
	private final long duration;

	/**
	 * @param testClassName test class name
	 * @param duration      tests execution duration in milliseconds
	 */
	public TestDuration(String testClassName, long duration) {
		this.testClassName = testClassName;
		this.duration = duration;
	}

	/**
	 * @return the testClassName
	 */
	public String getTestClassName() {
		return testClassName;
	}

	/**
	 * Gets duration of execution of tests
	 *
	 * @return duration
	 */
	public long getDuration() {
		return duration;
	}
}