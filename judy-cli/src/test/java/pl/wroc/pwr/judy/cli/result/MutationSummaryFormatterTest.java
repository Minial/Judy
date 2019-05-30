package pl.wroc.pwr.judy.cli.result;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IMutationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class MutationSummaryFormatterTest {

	private MutationSummaryFormatter printer;
	private IMutationResult results;
	private StringBuilder sb;

	@Before
	public void setUp() throws Exception {
		results = Mockito.mock(IMutationResult.class);
		printer = new MutationSummaryFormatter();

		sb = new StringBuilder();

		List<IClassMutationResult> classResults = new ArrayList<>();
		classResults.add(Mockito.mock(IClassMutationResult.class));
		classResults.add(Mockito.mock(IClassMutationResult.class));
		Mockito.when(results.getResults()).thenReturn(classResults);
		//System.out.println(classResults);
	}

	@Test
	public void shouldSummarizeClasses() throws Exception {
		printer.summarizeClass(sb, 2);
		assertEquals("\nClasses                      : 2\n", sb.toString());
	}

	@Test
	public void shouldSummarizeScore() throws Exception {
		printer.summarizeScore(sb, 25, 100, 10, 0);
		String expected = "Score                    [%] : 25.00\n" + "  all mutants                : 100\n"
				+ "  killed mutants             : 25\n" + "    by timeout               : 10\n"
				+ "    by infinite loop guard   : 0\n";
		assertEquals(expected, sb.toString());
	}

	@Test
	public void shouldSummarizeScoreWithZeroMutants() throws Exception {
		printer.summarizeScore(sb, 0, 0, 0, 0);
		String expected = "Score                    [%] : 0.00\n" + "  all mutants                : 0\n"
				+ "  killed mutants             : 0\n" + "    by timeout               : 0\n"
				+ "    by infinite loop guard   : 0\n";
		assertEquals(expected, sb.toString());
	}

	@Test
	public void shouldSummarizeParallel() throws Exception {
		long generationDuration = TimeUnit.SECONDS.toMillis(1337);
		long testDuration = TimeUnit.SECONDS.toMillis(7331);
		printer.summarizeParallel(sb, generationDuration, testDuration);
		String expected = "\nAnalysis duration (parallel)\n" + "  generation duration       [s] : 1337\n"
				+ "  test duration             [s] : 7331\n";
		assertEquals(expected, sb.toString());
	}

	@Test
	public void shouldSummarizeTestCount() throws Exception {
		printer.summarizeTestCount(sb, 7331, 1337);
		String expected = "Test classes used\n" + "  initial tests runs         : 7331\n"
				+ "  tests runs                 : 1337\n";
		assertEquals(expected, sb.toString());
	}

	@Test
	public void shouldSummarizeDuration() throws Exception {
		long mutationDuration = TimeUnit.SECONDS.toMillis(150);
		long initialTestDuration = TimeUnit.SECONDS.toMillis(50);

		Mockito.when(results.getDuration()).thenReturn(mutationDuration);
		String expected = "Duration                 [s] : 200\n" + "  initial tests duration [s] : 50\n"
				+ "  mutation duration      [s] : 150\n";
		printer.summarizeDuration(sb, results.getDuration(), initialTestDuration);
		assertEquals(expected, sb.toString());
	}

}
