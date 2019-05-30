package pl.wroc.pwr.judy.hom;

import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.addAll;

public class AbstractHomFactoryTest {

	protected HomFactory factory;
	protected List<IMutant> foms;
	protected List<IMutationOperator> operators;
	protected IDurationStatistic statistics;
	protected IMutant m1;
	protected IMutant m2;
	protected IMutant m3;

	protected void setUp() throws Exception {
		operators = new ArrayList<>();
		statistics = Mockito.mock(IDurationStatistic.class);
	}

	protected LinkedList<IMutant> prepareFoms(IMutant... mutants) {
		LinkedList<IMutant> foms = new LinkedList<>();
		addAll(foms, mutants);
		return foms;
	}

	protected IMutationOperator mockMutationOperator(String name, int line) {
		IMutationOperator operator = Mockito.mock(IMutationOperator.class);
		Mockito.when(operator.getName()).thenReturn(name);
		IMutantBytecode mutantBytecode = new MutantBytecode(new byte[]{2, 2, 2}, line);
		Mockito.when(operator.mutate(Matchers.any(byte[].class), Matchers.anyInt(), Matchers.anyInt())).thenReturn(
				mutantBytecode);
		return operator;
	}

	protected Mutant prepareMutant(int index, int lineNumber, List<IMutationOperator> operators) {
		return new Mutant(operators.get(index).getName(), index, index, "", lineNumber, "", new MutantBytecode(
				new byte[]{0, 0, 0}, lineNumber), index);
	}

	protected IMutationOperator mockMutationOperatorReturningNullMutant() {
		IMutationOperator operator = Mockito.mock(IMutationOperator.class);
		Mockito.when(operator.mutate(Matchers.any(byte[].class), Matchers.anyInt(), Matchers.anyInt()))
				.thenReturn(null);
		return operator;
	}
}
