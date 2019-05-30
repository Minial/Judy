package pl.wroc.pwr.judy.hom.som;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.helpers.ObjectHelper;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.operators.arithmetic.AIR_Add;
import pl.wroc.pwr.judy.operators.encapsulation.AMC;
import pl.wroc.pwr.judy.operators.inheritance.IOR;
import pl.wroc.pwr.judy.operators.javaspec.EMM;
import pl.wroc.pwr.judy.operators.javaspec.JTI;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RandomMixStrategyTest extends SomTest {
	private RandomMixStrategy cut;

	@Before
	public void setUp() throws Exception {
		cut = new RandomMixStrategy();
	}

	private void checkMutantGeneration(Random random) {
		ObjectHelper.setFieldValue(cut, "rnd", random);

		IMutantBytecode bytecode = new MutantBytecode(new byte[10], DUMMY_LINE_NUMBER);
		List<IMutationOperator> operators = recordOperatorMocks(bytecode, new EMM(), new JTI(), new AIR_Add(),
				new AMC(), new IOR());
		List<IMutant> foms = prepareFoms(bytecode, operators);

		List<IMutant> result = cut.create(foms, operators);
		assertNotNull(result);

		assertEquals(3, result.size());

		checkMutant(result.get(0), operators.get(0), operators.get(3));
		checkMutant(result.get(1), operators.get(1), operators.get(2));
		checkMutant(result.get(2), operators.get(4), operators.get(4));
	}

	@Test
	public void shouldGenerateMutantsOperators() {
		checkMutantGeneration(new Random(1));
	}

	@Test(expected = AssertionError.class)
	public void shouldFailBecauseOfModifiedRandomSeed() throws Exception {
		checkMutantGeneration(new Random(500));
	}
}
