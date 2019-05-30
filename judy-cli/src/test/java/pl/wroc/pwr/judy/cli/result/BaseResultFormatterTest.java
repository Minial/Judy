package pl.wroc.pwr.judy.cli.result;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.DurationStatistic;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.utils.ClassKind;
import pl.wroc.pwr.judy.work.MutationSummary;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class BaseResultFormatterTest extends MutationResultFormatterTest {

	private static final int maxClassNameLength = 10;
	private MutationSummary summary;
	private ITargetClass targetClass;
	private ClassKind kind;
	private IDurationStatistic statistic;
	private final String className = "ABCDEFGHIJ";
	private Collection<String> testClasses;

	@Before
	public void setUp() {
		kind = ClassKind.NORMAL;
		statistic = new DurationStatistic();
		summary = new MutationSummary();
		testClasses = new ArrayList<>();
		targetClass = mockTargetClass();
		formatter = new BaseResultFormatter(maxClassNameLength);

	}

	private ITargetClass mockTargetClass() {
		final ITargetClass tc = Mockito.mock(ITargetClass.class);
		Mockito.when(tc.getName()).thenReturn(className);
		Mockito.when(tc.getCoveringTestClasses()).thenReturn(testClasses);
		return tc;
	}

	@Test
	public void shouldPrepareBasicSummary() throws Exception {
		final int killed = 10;
		final int alive = 20;
		summary = new MutationSummary(killed, alive);

		final String expected = "N" + className + "10 30 0 0 0";
		final String summarizeText = formatter.getSummary(summary, targetClass, kind, statistic);
		assertEquals(ignoreWhitespace(expected), ignoreWhitespace(summarizeText));
	}

	@Test
	public void shouldReturnSummaryHeader() throws Exception {
		final String expected = "Progress ClassKind Class Killed All Test classes Test methods Tests duration[ms]";
		assertEquals(ignoreWhitespace(expected), ignoreWhitespace(formatter.getHeader()));
	}

}
