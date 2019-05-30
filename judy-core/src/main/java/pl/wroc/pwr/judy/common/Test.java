package pl.wroc.pwr.judy.common;

import pl.wroc.pwr.judy.ITest;

public class Test implements ITest {
	private final String name;
	private final long duration;

	public Test(String name, long duration) {
		this.name = name;
		this.duration = duration;
	}

	@Override
	public String getClassName() {
		return name;
	}

	@Override
	public long getDuration() {
		return duration;
	}
}
