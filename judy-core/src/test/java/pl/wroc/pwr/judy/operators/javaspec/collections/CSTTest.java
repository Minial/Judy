package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.javaspec.collections.CSTTestClass;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.*;

@Mutate(operator = CST.class, targetClass = CSTTestClass.class)
public class CSTTest extends AbstractMutationOperatorTesting {
	private static final LinkedHashSet<String> LINKED_HASH_SET = new LinkedHashSet<>();
	private static final List<String> ELEMENTS_ORDER = new ArrayList<>();

	static {
		LINKED_HASH_SET.add("b");
		LINKED_HASH_SET.add("a");
		LINKED_HASH_SET.add("c");
		LINKED_HASH_SET.add("d");
		LINKED_HASH_SET.add("e");
		LINKED_HASH_SET.add("f");

		ELEMENTS_ORDER.addAll(LINKED_HASH_SET);
	}

	@Test
	public void shouldCountMutationPoints() throws Exception {
		assertMutationPointsCountEquals(6);
	}

	@Test
	public void shouldCountMutants() throws Exception {
		assertMutantsCountEquals(6);
	}

	@Test
	public void shouldMutateLocalVariable() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getElementsLinkedHashSet");
		assertMutateMethodShuffledLinkedHashSet(0, "getElementsLinkedHashSet");
	}

	@Test
	public void shouldMutateExternalObject() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getElementsExternalObject");
		assertMutateMethodShuffledLinkedHashSet(1, "getElementsExternalObject");
	}

	@Test
	public void shouldMutateFromPrivateMethod() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getElementsFromPrivateMethod");
		assertMutateMethodShuffledLinkedHashSet(2, "getElementsFromPrivateMethod");
	}

	@Test
	public void shouldMutateUsingStatic() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getFromStatic");
		assertMutateMethodShuffledLinkedHashSet(3, "getFromStatic");
	}

	@Test
	public void shouldMutateUsingStaticDirectly() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getFromStaticDirectly");
		assertMutateMethodShuffledLinkedHashSet(4, "getFromStaticDirectly");
	}

	@Test
	public void shouldMutateSetFromInterfaceInvocation() throws Exception {
		assertMethodResultEquals(LINKED_HASH_SET, "getFromInterfaceInvocation");
		assertMutateMethodShuffledLinkedHashSet(5, "getFromInterfaceInvocation");
	}

	@Test
	public void shouldReturnOperatorDescription() throws Exception {
		assertEquals("Changes LinkedHashSet to HashSet to shuffle order of elements in LinkedHashSet",
				new CST().getDescription());
	}

	@SuppressWarnings("unchecked")
	private void assertMutateMethodShuffledLinkedHashSet(int index, String methodName) throws Exception {
		final LinkedHashSet<String> result = (LinkedHashSet<String>) getMutatedMethodResult(index, methodName);
		assertEquals(LINKED_HASH_SET.size(), result.size());
		final List<String> resultAsList = new ArrayList<>(result);
		assertFalse(ELEMENTS_ORDER.equals(resultAsList));
		resultAsList.removeAll(ELEMENTS_ORDER);
		assertTrue(resultAsList.isEmpty());
	}

}
