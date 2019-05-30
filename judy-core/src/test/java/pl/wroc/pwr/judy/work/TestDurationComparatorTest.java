package pl.wroc.pwr.judy.work;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDurationComparatorTest {

	private TestDurationComparator comparator;
	private TestDuration test1;
	private TestDuration test2;
	private TestDuration test3;

	@Before
	public void setUp() throws Exception {
		comparator = new TestDurationComparator();
		test1 = new TestDuration("Abc", 10);
		test2 = new TestDuration("Def", 20);
		test3 = new TestDuration("Fgh", 20);
	}

	@Test
	public void shouldCompareTestsWithDifferentDurations() throws Exception {
		assertTrue(comparator.compare(test1, test2) < 0);
	}

	@Test
	public void shouldCompareTestsSameDurationsDifferentTestClasses() throws Exception {
		assertTrue(comparator.compare(test2, test3) < 0);
	}

	@Test
	public void shouldCompareTestsSameDurationsSameTestClasses() throws Exception {
		assertEquals(0, comparator.compare(test3, test3));
	}

}
