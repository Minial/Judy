package pl.wroc.pwr.judy.hom;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class UniqueLinesHomFactoryTest extends AbstractHomFactoryTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		operators.add(mockMutationOperator("OP1", 1));
		operators.add(mockMutationOperator("OP2", 2));
		operators.add(mockMutationOperator("OP3", 3));

		m1 = prepareMutant(0, 1, operators);
		m2 = prepareMutant(1, 2, operators);
		m3 = prepareMutant(2, 3, operators);

		factory = new UniqueLinesHomFactory(statistics);
	}

	@Test(expected = IncompatibleMutationException.class)
	public void shouldFailOnMutationsInSameLine() throws Exception {
		foms = prepareFoms(m1, m1);
		factory.create(foms, operators);
	}

	@Test
	public void shouldAllowMutationsInUniqueLines() throws Exception {
		foms = prepareFoms(m1, m2, m3);
		IMutant hom = factory.create(foms, operators);
		assertEquals(3, hom.getOperatorsNames().size());
		assertEquals(Arrays.asList(1, 2, 3), hom.getLinesNumbers());
		assertEquals(Arrays.asList(0, 1, 2), hom.getMutionPointsIndexes());
	}
}
