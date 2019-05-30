package pl.wroc.pwr.judy.hom.som;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.operators.arithmetic.AIR_Add;
import pl.wroc.pwr.judy.operators.arithmetic.AIR_Div;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class AbstractSomStrategyTest extends SomTest {

	private AbstractSomStrategy cut;

	@Before
	public void setUp() throws Exception {
		cut = new AbstractSomStrategy() {
			@Override
			public List<IMutant> create(List<IMutant> foms, List<IMutationOperator> operators) {
				return null;
			}
		};
	}

	@Test
	public void testFindNearestMutationPoint() throws Exception {
		IMutationOperator operatorA = recordOperator(AIR_Add.class, new byte[10], 0, 1);
		IMutationOperator operatorB = recordOperator(AIR_Div.class, new byte[10], 1, 2);
		List<IMutationOperator> operators = new LinkedList<>();
		operators.add(operatorA);
		operators.add(operatorB);

		IMutant fom = new Mutant("AIR_Add", 5, 0, DUMMY_CLASS, DUMMY_LINE_NUMBER, 0);
		MutationInfo result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(1, operatorA, 0, result);

		fom = new Mutant("AIR_Add", 0, 0, DUMMY_CLASS, DUMMY_LINE_NUMBER, 0);
		result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(0, operatorA, 0, result);
	}

	@Test
	public void testFindNearestMutationPointForwardOperatorIteration() throws Exception {
		IMutationOperator operatorA = recordOperator(AIR_Add.class, new byte[10]);
		IMutationOperator operatorB = recordOperator(AIR_Div.class, new byte[10], 1, 2, 3);
		List<IMutationOperator> operators = new LinkedList<>();
		operators.add(operatorA);
		operators.add(operatorB);

		IMutant fom = new Mutant("AIR_Add", 5, 0, DUMMY_CLASS, DUMMY_LINE_NUMBER, 0);
		MutationInfo result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(0, operatorB, 0, result);

		fom = new Mutant("AIR_Add", 0, 0, DUMMY_CLASS, DUMMY_LINE_NUMBER, 0);
		result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(0, operatorB, 0, result);
	}

	@Test
	public void testFindNearestMutationPointBackwardOperatorIteration() throws Exception {
		IMutationOperator operatorA = recordOperator(AIR_Div.class, new byte[10], 1, 2, 3);
		IMutationOperator operatorB = recordOperator(AIR_Add.class, new byte[10]);
		List<IMutationOperator> operators = new LinkedList<>();
		operators.add(operatorA);
		operators.add(operatorB);

		Mutant fom = new Mutant("AIR_Add", 0, 5, DUMMY_CLASS, DUMMY_LINE_NUMBER, 1);
		MutationInfo result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(0, operatorA, 0, result);

		fom = new Mutant("AIR_Add", 0, 0, DUMMY_CLASS, DUMMY_LINE_NUMBER, 1);
		result = cut.findNearestMutationPoint(fom, new byte[10], operators);
		verifyMutationInfo(0, operatorA, 0, result);
	}

	@Test(expected = NoSuchElementException.class)
	public void testFindNearestMutationPointNoPointsForwardOperatorIteration() {
		List<IMutationOperator> operators = new LinkedList<>();
		operators.add(recordOperator(AIR_Add.class, new byte[10]));
		operators.add(recordOperator(AIR_Div.class, new byte[10]));

		Mutant fom = new Mutant("AIR_Add", 0, 5, DUMMY_CLASS, DUMMY_LINE_NUMBER, 0);
		cut.findNearestMutationPoint(fom, new byte[10], operators);
	}

	@Test(expected = NoSuchElementException.class)
	public void testFindNearestMutationPointNoPointsBackwardOperatorIteration() {
		List<IMutationOperator> operators = new LinkedList<>();
		operators.add(recordOperator(AIR_Div.class, new byte[10]));
		operators.add(recordOperator(AIR_Add.class, new byte[10]));

		Mutant fom = new Mutant("AIR_Add", 0, 5, DUMMY_CLASS, DUMMY_LINE_NUMBER, 1);
		cut.findNearestMutationPoint(fom, new byte[10], operators);
	}

	private void verifyMutationInfo(int pointIndex, IMutationOperator operator, int mutantIndex, MutationInfo result) {
		assertEquals(pointIndex, result.getMutationPointIndex());
		assertEquals(operator, result.getOperator());
		assertEquals(mutantIndex, result.getMutantIndex());
	}
}
