package pl.wroc.pwr.judy.hom.som;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.operators.arithmetic.AIR_Add;
import pl.wroc.pwr.judy.operators.arithmetic.AIR_Div;
import pl.wroc.pwr.judy.operators.javaspec.EOC;
import pl.wroc.pwr.judy.operators.javaspec.JTD;
import pl.wroc.pwr.judy.operators.logical.LIR_Or;

import java.util.List;

import static org.junit.Assert.*;

public class DifferentOperatorsStrategyTest extends SomTest {

	private DifferentOperatorsStrategy cut;

	@Before
	public void setUp() throws Exception {
		cut = new DifferentOperatorsStrategy();
	}

	private void testGenerateMutantsSame(IMutationOperator operatorA, IMutationOperator operatorB,
										 boolean expectedEmptyResult) {
		IMutantBytecode bytecode = new MutantBytecode(new byte[10], DUMMY_LINE_NUMBER);

		List<IMutationOperator> operators = recordOperatorMocks(bytecode, operatorA, operatorB);
		List<IMutant> foms = prepareFoms(bytecode, operators);

		List<IMutant> result = cut.create(foms, operators);
		assertNotNull(result);

		if (!expectedEmptyResult) {
			assertEquals(1, result.size());
			checkMutant(result.get(0), operatorA, operatorB);
		} else {
			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testGenerateMutantsClassicOperators() throws Exception {
		testGenerateMutantsSame(new AIR_Add(), new LIR_Or(), false);
	}

	@Test
	public void testGenerateMutantsMixedClassicOperators() throws Exception {
		testGenerateMutantsSame(new AIR_Add(), new AIR_Div(), true);
	}

	@Test
	public void testGenerateMutantsMixedOperators() throws Exception {
		testGenerateMutantsSame(new AIR_Add(), new EOC(), false);
	}

	@Test
	public void testGenerateMutantsJavaOperators() throws Exception {
		testGenerateMutantsSame(new JTD(), new EOC(), false);
	}

	@Test
	public void testGenerateMutantsSameClassicOperators() throws Exception {
		testGenerateMutantsSame(new AIR_Add(), new AIR_Add(), true);
	}

	@Test
	public void testGenerateMutantsSameJavaOperators() throws Exception {
		testGenerateMutantsSame(new JTD(), new JTD(), true);
	}
}
