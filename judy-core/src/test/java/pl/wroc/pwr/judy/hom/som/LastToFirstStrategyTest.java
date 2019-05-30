package pl.wroc.pwr.judy.hom.som;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.operators.javaspec.EMM;
import pl.wroc.pwr.judy.operators.javaspec.EOC;
import pl.wroc.pwr.judy.operators.javaspec.JTI;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LastToFirstStrategyTest extends SomTest {

	private LastToFirstStrategy cut;

	@Before
	public void setUp() throws Exception {
		cut = new LastToFirstStrategy();
	}

	@Test
	public void testGenerateMutantsOperatorsSizeOdd() {
		IMutantBytecode bytecode = new MutantBytecode(new byte[10], DUMMY_LINE_NUMBER);
		List<IMutationOperator> operators = recordOperatorMocks(bytecode, new EMM(), new JTI(), new EOC());
		List<IMutant> foms = prepareFoms(bytecode, operators);

		List<IMutant> result = cut.create(foms, operators);
		assertNotNull(result);
		assertEquals(2, result.size());

		checkMutant(result.get(0), operators.get(0), operators.get(2));
		checkMutant(result.get(1), operators.get(1), operators.get(2));
	}

	@Test
	public void testGenerateMutantsOperatorsSizeEven() {
		IMutantBytecode bytecode = new MutantBytecode(new byte[10], DUMMY_LINE_NUMBER);
		List<IMutationOperator> operators = recordOperatorMocks(bytecode, new EMM(), new JTI());
		List<IMutant> foms = prepareFoms(bytecode, operators);

		List<IMutant> result = cut.create(foms, operators);
		assertNotNull(result);

		assertEquals(1, result.size());
		checkMutant(result.get(0), operators.get(0), operators.get(1));
	}
}
