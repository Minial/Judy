package pl.wroc.pwr.judy.work;

import java.util.Comparator;

public class TestDurationComparator implements Comparator<TestDuration> {
	@Override
	public int compare(TestDuration o1, TestDuration o2) {
		if (o1.getDuration() != o2.getDuration()) {
			return o1.getDuration() > o2.getDuration() ? 1 : -1;
		}
		return o1.getTestClassName().compareTo(o2.getTestClassName());
	}
}