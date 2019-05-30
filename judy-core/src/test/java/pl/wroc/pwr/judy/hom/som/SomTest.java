package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.operators.MutationPoint;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class SomTest {
	public static final int DUMMY_LINE_NUMBER = 123;
	public static final int DUMMY_POINT = 2;
	public static final String DUMMY_CLASS = "targetClassName";

	protected IMutationOperator recordOperator(Class<?> clazz, byte[] bytecode, int... points) {
		IMutationOperator operator = mock(IMutationOperator.class);
		List<IMutationPoint> mutationPoints = new LinkedList<>();
		for (int point : points) {
			mutationPoints.add(new MutationPoint(point));
		}
		when(operator.getMutationPoints(bytecode)).thenReturn(mutationPoints);
		when(operator.getName()).thenReturn(clazz.getSimpleName());

		MutantBytecode mutant = new MutantBytecode(bytecode, DUMMY_LINE_NUMBER);
		List<IMutantBytecode> mutantBytecodes = new ArrayList<>();
		mutantBytecodes.add(mutant);
		when(operator.mutate((byte[]) any(), (IMutationPoint) any())).thenReturn(mutantBytecodes);

		return operator;
	}

	protected List<IMutant> prepareFoms(IMutantBytecode bytecode, List<IMutationOperator> operators) {
		List<IMutant> foms = new ArrayList<>();
		for (int i = 0; i < operators.size(); i++) {
			IMutant mutant = new Mutant(operators.get(i).getName(), DUMMY_POINT, i, DUMMY_CLASS, DUMMY_LINE_NUMBER, i);
			mutant.setBytecode(bytecode);
			foms.add(mutant);
		}
		return foms;
	}

	protected List<IMutationOperator> recordOperatorMocks(IMutantBytecode bytecode, IMutationOperator... operators) {
		List<IMutationOperator> mocks = new ArrayList<>();

		for (int i = 0; i < operators.length; i++) {
			mocks.add(recordOperator(operators[i].getClass(), bytecode.getBytecode(), i));
		}
		return mocks;
	}

	protected void checkMutant(IMutant mutant, IMutationOperator operatorA, IMutationOperator operatorB) {
		assertEquals(2, mutant.getOperatorsNames().size());
		assertTrue(mutant.getOperatorsNames().contains(operatorA.getName()));
		assertTrue(mutant.getOperatorsNames().contains(operatorB.getName()));
	}

}
