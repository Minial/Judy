package pl.wroc.pwr.judy.hom;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class BaseHomFactoryTest extends AbstractHomFactoryTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		operators.add(mockMutationOperator("OP1", 111));
		operators.add(mockMutationOperator("OP2", 888));
		operators.add(mockMutationOperator("OP3", 999));

		m1 = prepareMutant(0, 111, operators);
		m2 = prepareMutant(1, 222, operators);
		m3 = prepareMutant(2, 333, operators);
		foms = prepareFoms(m1, m2, m3);

		factory = new BaseHomFactory(statistics);
	}

	@Test
	public void shouldCreateThirdOrderMutant() throws Exception {
		IMutant hom = factory.create(foms, operators);
		assertEquals(3, hom.getOperatorsNames().size());
		assertEquals(Arrays.asList(111, 888, 999), hom.getLinesNumbers());
		assertEquals(Arrays.asList(0, 1, 2), hom.getMutionPointsIndexes());
		assertNotSame(hom, foms.get(0));

		Mockito.verify(statistics, Mockito.times(2)).addMutantGenration(Matchers.anyLong());
	}

	@Test
	public void shouldCreateSecondOrderMutant() throws Exception {
		foms = new ArrayList<>();
		foms.add(m1);
		foms.add(m2);
		IMutant hom = factory.create(foms, operators);
		assertEquals(2, hom.getOperatorsNames().size());
		assertEquals(Arrays.asList(111, 888), hom.getLinesNumbers());
		assertEquals(Arrays.asList(0, 1), hom.getMutionPointsIndexes());
		assertNotSame(hom, foms.get(0));

		Mockito.verify(statistics, Mockito.times(1)).addMutantGenration(Matchers.anyLong());
	}

	@Test(expected = IncompatibleMutationException.class)
	public void shouldFailWithInvalidMutant() throws Exception {
		foms.clear();
		foms.add(m1);
		foms.add(m2);
		operators.clear();
		operators.add(mockMutationOperator("OP1", 1));
		operators.add(mockMutationOperator("OP2", -1));
		factory.create(foms, operators);
	}

	@Test(expected = IncompatibleMutationException.class)
	public void shouldThrowExceptionIfHomCouldNotRecreateAllFoms() throws Exception {
		operators.remove(0);
		operators.add(mockMutationOperatorReturningNullMutant());
		factory.create(foms, operators);
	}

	@Test
	public void shouldAllowSameLineMutations() throws Exception {
		foms.clear();
		foms.add(m1);
		foms.add(m1);

		IMutant hom = factory.create(foms, operators);
		assertEquals(2, hom.getOperatorsNames().size());
		assertEquals(Arrays.asList(111, 111), hom.getLinesNumbers());
		assertEquals(Arrays.asList(0, 0), hom.getMutionPointsIndexes());
	}

}
