package pl.wroc.pwr.judy.hom.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.BitSet;

import static org.junit.Assert.assertTrue;

public class CustomBinaryVariableInitializationTest {

	private Problem problem;
	private int populationSize = 1;
	private int fomsSize = 250;
	private int maxMutationOrder = 5;

	@Before
	public void init() {
		problem = Mockito.mock(Problem.class);
	}

	@Test
	public void shouldChooseNoMoreBitsThanMaxMutationOrder() {
		// given
		Solution solution = new Solution(1, 1);
		BinaryVariable binaryVariable = new BinaryVariable(fomsSize);
		solution.setVariable(0, binaryVariable);
		Mockito.when(problem.newSolution()).thenReturn(solution);
		CustomBinaryVariableInitialization initialization = new CustomBinaryVariableInitialization(problem,
				populationSize, maxMutationOrder);
		// when
		initialization.initialize();
		// assert
		BitSet bs = EncodingUtils.getBitSet(solution.getVariable(0));
		assertTrue(bs.cardinality() <= maxMutationOrder);
	}
}
