package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.javaspec.collections.ListUsageTestClass;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Mutate(operator = DUL.class, targetClass = ListUsageTestClass.class)
public class DULTest extends AbstractMutationOperatorTesting {

	private static final List<String> NOT_MUTATED_LIST = Arrays.asList("a", "b", "c", "d", "e", "f");

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
		assertEquals("Duplicates element in the list", new DUL().getDescription());
	}

	@SuppressWarnings("unchecked")
	private void checkMutationResult(int mutantIndex, String methodName) throws Exception {
		assertMethodResultEquals(NOT_MUTATED_LIST, methodName);
		List<String> mutatedResults = (List<String>) getMutatedMethodResult(mutantIndex, methodName);
		assertFalse(mutatedResults.equals(NOT_MUTATED_LIST));
		assertTrue(mutatedResults.containsAll(NOT_MUTATED_LIST));
		assertOneOfTheElementsIsDuplicated(mutatedResults);
	}

	private void assertOneOfTheElementsIsDuplicated(List<String> mutatedResults) {
		for (String string : NOT_MUTATED_LIST) { // because removeAll removes
			// duplicates too
			mutatedResults.remove(string);
		}
		assertEquals(1, mutatedResults.size());
		assertTrue(NOT_MUTATED_LIST.contains(mutatedResults.get(0)));
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
