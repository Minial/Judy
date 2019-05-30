package pl.wroc.pwr.judy.cli.argsparser;

import joptsimple.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionParserCreatorTest {

	private static final String ARGUMENT = "argument";
	private static OptionParser parser;
	private static String helpDescription;

	@BeforeClass
	public static void setUp() throws IOException {
		parser = OptionParserCreator.createOptionParser();

		// formatting output to avoid unexpected chars
		final HelpFormatter helpFormatter = new BuiltinHelpFormatter(800, 10);
		parser.formatHelpWith(helpFormatter);

		final StringWriter writer = new StringWriter();
		parser.printHelpOn(writer);

		helpDescription = writer.toString();
	}

	@Test
	public void testHelpParam() throws IOException {
		final OptionSet options = parser.parse("-h");
		assertTrue(options.has("help"));
		assertTrue(options.has("h"));
		assertTrue(options.has("?"));
		assertTrue(helpDescription.contains("Print help."));
	}

	@Test
	public void testKilledParam() {
		final OptionSet options = parser.parse("-killed");
		assertTrue(options.has("killed"));
		assertTrue(helpDescription.contains("Show killed mutants in report."));
	}

	@Test
	public void testClusterParam() {
		final OptionSet options = parser.parse("-C");
		assertTrue(options.has("C"));
		assertTrue(options.has("cluster"));
		assertTrue(helpDescription.contains("Enable distributed computations."));
	}

	@Test
	public void testSomParam() {
		final OptionSet options = parser.parse("-som", "thisissomArgument");
		assertTrue(options.has("S"));
		assertTrue(options.has("som"));
		assertTrue(options.hasArgument("som"));
		assertTrue(helpDescription.contains("Enable Second Order Mutants generation."));
	}

	@Test(expected = OptionException.class)
	public void testSomParamNoArgument() {
		parser.parse("-som");
	}

	@Test(expected = OptionException.class)
	public void testHomParamNoArgument() throws Exception {
		parser.parse("--hom");
	}

	@Test
	public void testHomParam() {
		final OptionSet options = parser.parse("-hom", ARGUMENT);
		assertTrue(options.has("hom"));
		assertEquals(ARGUMENT, options.valueOf("hom"));
		assertTrue(helpDescription.contains("Enable High Order Mutants generation."));
	}

	@Test
	public void testWorkspaceParam() {
		final OptionSet options = parser.parse("-w", ARGUMENT);
		assertTrue(options.has("w"));
		assertTrue(options.has("workspace"));
		assertEquals(ARGUMENT, options.valueOf("w"));
		assertTrue(helpDescription.contains("Set absolute path to workspace directory."));
	}

	@Test(expected = OptionException.class)
	public void testWorkspaceParamNoArgument() {
		parser.parse("--workspace");
	}

	@Test
	public void testClassesParam() {
		final OptionSet options = parser.parse("-c", ARGUMENT);
		assertTrue(options.has("c"));
		assertTrue(options.has("classes"));
		assertEquals(ARGUMENT, options.valueOf("c"));
		assertTrue(helpDescription.contains("Set relative path to classes directory."));
	}

	@Test(expected = OptionException.class)
	public void testClassesParamNoArgument() {
		parser.parse("--classes");
	}

	@Test
	public void testTestClassesParam() {
		final OptionSet options = parser.parse("-t", ARGUMENT);
		assertTrue(options.has("t"));
		assertTrue(options.has("test-classes"));
		assertEquals(ARGUMENT, options.valueOf("t"));
		assertTrue(helpDescription.contains("Set relative path to test classes directory."));
	}

	@Test(expected = OptionException.class)
	public void testTestClassesParamNoArgument() {
		parser.parse("--test-classes");
	}

	@Test
	public void testLibsClassesParam() {
		final OptionSet options = parser.parse("-l", ARGUMENT);
		assertTrue(options.has("l"));
		assertTrue(options.has("libs"));
		assertEquals(ARGUMENT, options.valueOf("libs"));
		assertTrue(helpDescription.contains("Set relative path to libs directory or to single jar file."));
	}

	@Test(expected = OptionException.class)
	public void testLibsClassesParamNoArgument() {
		parser.parse("--libs");
	}

	@Test
	public void testSourceClassesParam() {
		final OptionSet options = parser.parse("-s", ARGUMENT);
		assertTrue(options.has("s"));
		assertTrue(options.has("sources"));
		assertEquals(ARGUMENT, options.valueOf("s"));
		assertTrue(helpDescription.contains("Set relative path to source code directory."));
	}

	@Test(expected = OptionException.class)
	public void testSourceClassesParamNoArgument() {
		parser.parse("--sources");
	}

	@Test
	public void testResulthPathParam() {
		final OptionSet options = parser.parse("-r", ARGUMENT);
		assertTrue(options.has("r"));
		assertTrue(options.has("result-path"));
		assertEquals(ARGUMENT, options.valueOf("r"));
		assertTrue(helpDescription.contains("Set absolute path to write results."));
	}

	@Test(expected = OptionException.class)
	public void testResulthPathParamNoArgument() {
		parser.parse("-r");
	}

	@Test
	public void testPropertiesFilePathParam() {
		final OptionSet options = parser.parse("-p", ARGUMENT);
		assertTrue(options.has("p"));
		assertTrue(options.has("properties"));
		assertEquals(ARGUMENT, options.valueOf("p"));
		assertTrue(helpDescription.contains("Set absolute path to properties file."));
	}

	@Test(expected = OptionException.class)
	public void testProperitesFilePathParamNoArgument() {
		parser.parse("-p");
	}

	@Test
	public void testNumberOfThreadsParam() {
		final OptionSet options = parser.parse("-x", ARGUMENT);
		assertTrue(options.has("x"));
		assertTrue(options.has("threads"));
		assertEquals(ARGUMENT, options.valueOf("x"));
		assertTrue(helpDescription.contains("Set number of threads."));
	}

	@Test
	public void testInfiniteLoopBreakTimeParam() {
		final OptionSet options = parser.parse("-i", ARGUMENT);
		assertTrue(options.has("i"));
		assertTrue(options.has("maxInfiniteLoopGuardTime"));
		assertEquals(ARGUMENT, options.valueOf("maxInfiniteLoopGuardTime"));
		assertTrue(helpDescription
				.contains("Set maximum waiting time for Judy's Infinite Loop Guard (in miliseconds, default 5000)."));
	}

	@Test(expected = OptionException.class)
	public void testNumberOfThreadsParamNoArgument() {
		parser.parse("-x");
	}

	@Test
	public void testRunTestsInParallelParam() {
		final OptionSet optionSet = parser.parse("-runTestsInParallel");
		assertTrue(optionSet.has("runTestsInParallel"));
	}

}
