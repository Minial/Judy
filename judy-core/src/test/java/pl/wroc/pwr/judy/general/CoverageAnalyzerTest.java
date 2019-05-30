package pl.wroc.pwr.judy.general;

import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionDataStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CoverageAnalyzerTest {

	private static final int LINE_2 = 2;
	private static final int LINE_1 = 1;
	private static final String SOURCE_CLASS_1 = "com.clazzA";
	private static final String SOURCE_CLASS_2 = "com.clazzB";
	private static final String SOURCE_CLASS_SLASH_1 = "com/clazzA";
	private static final String TEST_METHOD = "testMethod";
	private static final String TEST_CLASS = "testClass";
	private static final byte[] BYTECODE_1 = new byte[]{LINE_1, LINE_2, 3};
	private static final byte[] BYTECODE_2 = new byte[]{4, 5, 6};
	private static final String UNKNOWN_TEST_CLASS = "unknownTestClass";

	private CoverageAnalyzer cut;
	private IBytecodeCache cache;
	private ICoverage coverage;
	protected Analyzer analyzer;
	protected CoverageBuilder builder;
	private Set<String> expectedMethod;

	@Before
	public void setUp() throws Exception {
		coverage = prepareCoverage();
		cache = mockCache();
		analyzer = Mockito.mock(Analyzer.class);
		builder = Mockito.mock(CoverageBuilder.class);

		expectedMethod = new HashSet<>();
		expectedMethod.add(TEST_METHOD);

		cut = new CoverageAnalyzer(cache, coverage) {
			@Override
			protected Analyzer createAnalyzer(ExecutionDataStore executionData, CoverageBuilder coverageBuilder) {
				return analyzer;
			}

			@Override
			protected CoverageBuilder createBuilder() {
				return builder;
			}
		};
	}

	@Test
	public void shouldSaveCoveredLines() throws Exception {
		IClassCoverage cc = mockClassCoverage(SOURCE_CLASS_SLASH_1, mockLine(ICounter.FULLY_COVERED),
				mockLine(ICounter.PARTLY_COVERED));

		Set<String> coverageSourceClass1 = checkAnalysis(cc);
		assertEquals(expectedMethod, coverageSourceClass1);

		verifyAnalyzer();
	}

	@Test
	public void shouldSkipNotCoveredLines() throws Exception {
		IClassCoverage cc = mockClassCoverage(SOURCE_CLASS_SLASH_1, mockLine(ICounter.NOT_COVERED),
				mockLine(ICounter.EMPTY));

		Set<String> result = checkAnalysis(cc);
		assertTrue(result.isEmpty());

		verifyAnalyzer();
	}

	@Test
	public void shouldCreateAnalyzer() throws Exception {
		cut = new CoverageAnalyzer(cache, coverage);
		Analyzer result = cut.createAnalyzer(null, builder);
		assertNotNull(result);
	}

	@Test
	public void shouldCreateCoverageBuilder() throws Exception {
		cut = new CoverageAnalyzer(cache, coverage);
		CoverageBuilder result = cut.createBuilder();
		assertNotNull(result);
	}

	@Test
	public void shouldSkipAnalysisOfUnknownClasses() throws Exception {
		cut.analyze(UNKNOWN_TEST_CLASS, TEST_METHOD, null);
		assertTrue(coverage.getCoveringTestMethods(UNKNOWN_TEST_CLASS, TEST_METHOD, Arrays.asList(LINE_1, LINE_2))
				.isEmpty());
	}

	private ICoverage prepareCoverage() {
		ICoverage coverage = new MapCoverage();
		coverage.addClass(SOURCE_CLASS_1, TEST_CLASS);
		coverage.addClass(SOURCE_CLASS_2, TEST_CLASS);
		coverage.addClass(UNKNOWN_TEST_CLASS, TEST_CLASS);
		return coverage;
	}

	private IBytecodeCache mockCache() {
		IBytecodeCache mock = Mockito.mock(IBytecodeCache.class);
		Mockito.when(mock.get(SOURCE_CLASS_1)).thenReturn(BYTECODE_1);
		Mockito.when(mock.get(SOURCE_CLASS_2)).thenReturn(BYTECODE_2);
		Mockito.when(mock.get(UNKNOWN_TEST_CLASS)).thenReturn(null);
		return mock;
	}

	private void verifyAnalyzer() throws IOException {
		Mockito.verify(analyzer).analyzeClass(BYTECODE_1, SOURCE_CLASS_1);
		Mockito.verify(analyzer).analyzeClass(BYTECODE_2, SOURCE_CLASS_2);
	}

	private Set<String> checkAnalysis(IClassCoverage cc) {
		Mockito.when(builder.getClasses()).thenReturn(Arrays.asList(cc));

		cut.analyze(TEST_CLASS, TEST_METHOD, null);

		Set<String> result = coverage.getCoveringTestMethods(SOURCE_CLASS_1, TEST_CLASS, Arrays.asList(LINE_1, LINE_2));
		return result;
	}

	private IClassCoverage mockClassCoverage(String name, ILine lineA, ILine lineB) {
		IClassCoverage cc = Mockito.mock(IClassCoverage.class);
		Mockito.when(cc.getName()).thenReturn(name);
		Mockito.when(cc.getFirstLine()).thenReturn(LINE_1);
		Mockito.when(cc.getLastLine()).thenReturn(LINE_2);

		Mockito.when(cc.getLine(LINE_1)).thenReturn(lineA);
		Mockito.when(cc.getLine(LINE_2)).thenReturn(lineB);
		return cc;
	}

	private ILine mockLine(int status) {
		ILine line = Mockito.mock(ILine.class);
		Mockito.when(line.getStatus()).thenReturn(status);
		return line;
	}
}
