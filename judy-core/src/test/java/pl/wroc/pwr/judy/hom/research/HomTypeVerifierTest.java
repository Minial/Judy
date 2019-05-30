package pl.wroc.pwr.judy.hom.research;

import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.general.TestResultList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomTypeVerifierTest {
	@Test
	public void testWeaklySubsumingMutantCoupled() throws Exception {
		IMutant hom = mockMutant(prepareMethods("c", "e", "f"));
		IMutant m1 = mockMutant(prepareMethods("a", "b", "c"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertTrue(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testWeaklySubsumingMutantDecoupled() throws Exception {
		IMutant hom = mockMutant(prepareMethods("e", "f", "g"));
		IMutant m1 = mockMutant(prepareMethods("a", "b", "c"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertTrue(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testNonSubsumingAndDecoupled() throws Exception {
		IMutant hom = mockMutant(prepareMethods("e", "f", "g", "h"));
		IMutant m1 = mockMutant(prepareMethods("a", "b"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertFalse(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testNonSubsumingAndCoupled2() throws Exception {
		IMutant hom = mockMutant(prepareMethods("e", "f", "g", "h", "i", "j"));
		IMutant m1 = mockMutant(prepareMethods("a", "b"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertFalse(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testNonSubsumingAndDecoupledEquivalent() throws Exception {
		IMutant hom = mockMutant(prepareMethods());
		IMutant m1 = mockMutant(prepareMethods("a", "b"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertFalse(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testNonSubsumingCoupled() throws Exception {
		IMutant hom = mockMutant(prepareMethods("a", "b", "e", "f", "g"));
		IMutant m1 = mockMutant(prepareMethods("a", "b"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertFalse(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testStronglySubsuming() throws Exception {
		IMutant hom = mockMutant(prepareMethods("c"));
		IMutant m1 = mockMutant(prepareMethods("a", "b", "c"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertTrue(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testStronglySubsumingButNotWeaklySubsuming() throws Exception {
		IMutant hom = mockMutant(prepareMethods("c"));
		IMutant m1 = mockMutant(prepareMethods("c"));
		IMutant m2 = mockMutant(prepareMethods("c"));

		assertFalse(HomTypeVerifier.isSubsuming(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isStronglySubsuming(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isNonIntersecting(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testPartiallyShiftedMutant() throws Exception {
		IMutant hom = mockMutant(prepareMethods("b", "f", "g", "a"));
		IMutant m1 = mockMutant(prepareMethods("a", "b", "c"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertTrue(HomTypeVerifier.isPartiallyShifted(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isTotallyShifted(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testTotallyShiftedMutant() throws Exception {
		IMutant hom = mockMutant(prepareMethods("e", "f", "g"));
		IMutant m1 = mockMutant(prepareMethods("a", "b", "c"));
		IMutant m2 = mockMutant(prepareMethods("c", "d"));

		assertTrue(HomTypeVerifier.isPartiallyShifted(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isTotallyShifted(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testIfShiftingIsCalculatedCorrectly_emptyHomSet() throws Exception {
		IMutant hom = mockMutant(prepareMethods());
		IMutant m1 = mockMutant(prepareMethods("a"));
		IMutant m2 = mockMutant(prepareMethods());

		assertFalse(HomTypeVerifier.isPartiallyShifted(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isTotallyShifted(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testIfShiftingIsCalculatedCorrectly_emptyFomSets() throws Exception {
		IMutant hom = mockMutant(prepareMethods("a"));
		IMutant m1 = mockMutant(prepareMethods());
		IMutant m2 = mockMutant(prepareMethods());

		assertTrue(HomTypeVerifier.isPartiallyShifted(hom, Arrays.asList(m1, m2)));
		assertTrue(HomTypeVerifier.isTotallyShifted(hom, Arrays.asList(m1, m2)));
	}

	@Test
	public void testIfShiftingIsCalculatedCorrectly_emptySets() throws Exception {
		IMutant hom = mockMutant(prepareMethods());
		IMutant m1 = mockMutant(prepareMethods());
		IMutant m2 = mockMutant(prepareMethods());

		assertFalse(HomTypeVerifier.isPartiallyShifted(hom, Arrays.asList(m1, m2)));
		assertFalse(HomTypeVerifier.isTotallyShifted(hom, Arrays.asList(m1, m2)));
	}

	private Set<String> prepareMethods(String... methods) {
		Set<String> result = new HashSet<>();
		addAll(result, methods);
		return result;
	}

	private IMutant mockMutant(Set<String> killingMethods) {
		TestResultList results = mockResults(killingMethods);

		IMutant m = Mockito.mock(IMutant.class);
		Mockito.when(m.getResults()).thenReturn(results);
		return m;
	}

	private TestResultList mockResults(Set<String> killingMethods) {
		TestResultList results = Mockito.mock(TestResultList.class);
		Mockito.when(results.getFailingTestMethods()).thenReturn(killingMethods);
		return results;
	}

}
