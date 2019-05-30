package pl.wroc.pwr.judy.hom;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.helpers.ObjectHelper;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CachingFomFactoryTest {

	private static final String OPERATOR_NAME = "OPERATOR";
	private static final String TARGET_CLASS_NAME = "target.class.name";
	private CachingFomFactory factory;
	private byte[] bytecode;
	private IMutantBytecode mb1;
	private List<IMutationOperator> operators;

	private IMutantCache cache;
	private IDurationStatistic statistics;

	@Before
	public void setUp() throws Exception {
		bytecode = new byte[]{1, 2, 3};

		mb1 = new MutantBytecode(new byte[]{1}, 1);

		operators = new ArrayList<>();

		cache = Mockito.mock(IMutantCache.class);

		statistics = Mockito.mock(IDurationStatistic.class);

		factory = new CachingFomFactory(bytecode, TARGET_CLASS_NAME, operators, statistics);

		ObjectHelper.setFieldValue(factory, "cache", cache);
	}

	@Test
	public void shouldCacheMutantsAndReuseThem() throws Exception {
		IMutationOperator operator = mockOperatorBasics(OPERATOR_NAME);
		MutationInfo info = new MutationInfo(operator, 0, 0);
		mockReturnOfMutantBytecodes(operator, info, mb1);

		IMutant mutant = new Mutant(OPERATOR_NAME, info.getMutationPointIndex(), info.getMutantIndex(),
				TARGET_CLASS_NAME, 1, null, mb1, 0);
		Mockito.when(cache.get(info)).thenReturn(null, mutant);

		IMutant before = factory.createFom(info);
		IMutant after = factory.createFom(info);

		assertEquals(before, after);

		Mockito.verify(operator, Mockito.times(1))
				.mutate(bytecode, info.getMutationPointIndex(), info.getMutantIndex());
		Mockito.verify(statistics, Mockito.times(1)).addMutantGenration(Matchers.anyLong());
	}

	private IMutationOperator mockOperatorBasics(String name) {
		IMutationOperator operator = Mockito.mock(IMutationOperator.class);
		Mockito.when(operator.getName()).thenReturn(name);

		operators.add(operator);

		return operator;
	}

	private IMutationOperator mockReturnOfMutantBytecodes(IMutationOperator operator, MutationInfo info,
														  IMutantBytecode mutantBytecode) {
		Mockito.when(operator.mutate(bytecode, info.getMutationPointIndex(), info.getMutantIndex())).thenReturn(
				mutantBytecode);
		return operator;
	}
}
