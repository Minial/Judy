package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.javaspec.collections.ListUsageTestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Mutate(operator = LSF.class, targetClass = ListUsageTestClass.class)
public class LSFTest extends AbstractMutationOperatorTesting {

	private static final List<String> EXPECTED_LIST = Arrays.asList("a", "b", "c", "d", "e", "f");
	List<String> expectedList = new ArrayList<>();

	@Override
	public void init() throws Exception {
		super.init();
		expectedList.add("a");
		expectedList.add("b");
		expectedList.add("c");
		expectedList.add("d");
		expectedList.add("e");
		expectedList.add("f");
	}

	@Test
	public void shouldCountMutationPoints() throws Exception {
		assertMutationPointsCountEquals(7);
	}

	@Test
	public void shouldCountMutants() throws Exception {
		assertMutantsCountEquals(7);
	}

	@Test
	public void shouldReturnOperatorDescription() throws Exception {
		assertEquals("Shuffles the list", new LSF().getDescription());
	}

	@SuppressWarnings("unchecked")
	private void checkMutationResult(int mutantIndex, String methodName) throws Exception {
		assertMethodResultEquals(expectedList, methodName);
		List<String> mutatedResults = (List<String>) getMutatedMethodResult(mutantIndex, methodName);
		assertFalse(mutatedResults.equals(EXPECTED_LIST));
		assertEquals(mutatedResults.size(), EXPECTED_LIST.size());
		assertTrue(mutatedResults.containsAll(EXPECTED_LIST));
	}

	@Test
	public void shouldMutateListLocalVariable() throws Exception {
		checkMutationResult(0, "getMethodLocalList");
	}

	@Test
	public void shouldMutateListFromExternalObject() throws Exception {
		checkMutationResult(1, "getListFromExternalObject");
	}

	@Test
	public void shouldMutateListFromExternalClassStaticField() throws Exception {
		checkMutationResult(2, "getListFromExternalClassStaticField");
	}

	@Test
	public void shouldMutateListFromStaticFieldDirectly() throws Exception {
		checkMutationResult(3, "getListFromStaticFieldDirectly");
	}

	@Test
	public void shouldMutateListFromFieldDirectly() throws Exception {
		checkMutationResult(4, "getListFromFieldDirectly");
	}

	@Test
	public void shouldMutateListFromPrivateMethod() throws Exception {
		checkMutationResult(5, "getListFromPrivateMethod");
	}

	@Test
	public void shouldMutateListFromInterfaceInvocation() throws Exception {
		checkMutationResult(6, "getListFromInterfaceInvocation");
	}
}
