package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.javaspec.collections.SortedCollectionsUsageTestClass;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

@Mutate(operator = ORV.class, targetClass = SortedCollectionsUsageTestClass.class)
public class ORVTest extends AbstractMutationOperatorTesting {

	private static final TreeSet<String> NOT_MUTATED_TREE_SET = new TreeSet<>();
	private static final TreeSet<String> EXPECTED_MUTATED_TREE_SET = new TreeSet<>(Arrays.asList("f", "e", "d",
			"c", "b", "a"));
	private static final TreeMap<String, String> BASIC_TREE_MAP = new TreeMap<>();
	private static final TreeMap<String, String> EXPECTED_TREE_MAP = new TreeMap<>();

	static {
		NOT_MUTATED_TREE_SET.add("a");
		NOT_MUTATED_TREE_SET.add("b");
		NOT_MUTATED_TREE_SET.add("c");
		NOT_MUTATED_TREE_SET.add("d");
		NOT_MUTATED_TREE_SET.add("e");
		NOT_MUTATED_TREE_SET.add("f");

		EXPECTED_MUTATED_TREE_SET.add("f");
		EXPECTED_MUTATED_TREE_SET.add("e");
		EXPECTED_MUTATED_TREE_SET.add("d");
		EXPECTED_MUTATED_TREE_SET.add("c");
		EXPECTED_MUTATED_TREE_SET.add("b");
		EXPECTED_MUTATED_TREE_SET.add("a");

		BASIC_TREE_MAP.put("a", "a");
		BASIC_TREE_MAP.put("b", "b");
		BASIC_TREE_MAP.put("c", "c");
		BASIC_TREE_MAP.put("d", "d");
		BASIC_TREE_MAP.put("e", "e");
		BASIC_TREE_MAP.put("f", "f");

		EXPECTED_TREE_MAP.put("a", "a");
		EXPECTED_TREE_MAP.put("b", "b");
		EXPECTED_TREE_MAP.put("c", "c");
		EXPECTED_TREE_MAP.put("d", "d");
		EXPECTED_TREE_MAP.put("e", "e");
		EXPECTED_TREE_MAP.put("f", "f");
	}

	@Test
	public void shouldCountMutationPoints() throws Exception {
		assertMutationPointsCountEquals(14);
	}

	@Test
	public void shouldCountMutants() throws Exception {
		assertMutantsCountEquals(14);
	}

	@Test
	public void shouldReturnOperatorDescription() throws Exception {
		assertEquals("Reverses the order of collections with ordering based on comparator", new ORV().getDescription());
	}

	@Test
	public void shouldMutateTreeSetLocalVariable() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getMethodLocal");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 0, "getMethodLocal");
	}

	@Test
	public void shouldMutateTreeSetFromExternalObject() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromExternalObject");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 1, "getFromExternalObject");
	}

	@Test
	public void shouldMutateTreeSetFromExternalClassStaticField() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromExternalClassStaticField");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 2, "getFromExternalClassStaticField");
	}

	@Test
	public void shouldMutateTreeFromStaticFieldDirectly() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromStaticFieldDirectly");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 3, "getFromStaticFieldDirectly");
	}

	@Test
	public void shouldMutateTreeFromFieldDirectly() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromFieldDirectly");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 4, "getFromFieldDirectly");
	}

	@Test
	public void shouldMutateTreeFromPrivateMethod() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromPrivateMethod");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 5, "getFromPrivateMethod");
	}

	@Test
	public void shouldMutateTreeSetLocalVariableMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getMethodLocalMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 6, "getMethodLocalMap");
	}

	@Test
	public void shouldMutateTreeSetFromExternalObjectMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromExternalObjectMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 7, "getFromExternalObjectMap");
	}

	@Test
	public void shouldMutateTreeSetFromExternalClassStaticFieldMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromExternalClassStaticFieldMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 8, "getFromExternalClassStaticFieldMap");
	}

	@Test
	public void shouldMutateTreeFromStaticFieldDirectlyMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromStaticFieldDirectlyMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 9, "getFromStaticFieldDirectlyMap");
	}

	@Test
	public void shouldMutateTreeFromFieldDirectlyMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromFieldDirectlyMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 10, "getFromFieldDirectlyMap");
	}

	@Test
	public void shouldMutateTreeFromPrivateMethodMap() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromPrivateMethodMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 11, "getFromPrivateMethodMap");
	}

	@Test
	public void shouldMutateTreeFromMapInterfaceInvoacation() throws Exception {
		assertMethodResultEquals(BASIC_TREE_MAP, "getFromInterfaceInvocationMap");
		assertMutatedMethodResultEquals(EXPECTED_TREE_MAP, 11, "getFromInterfaceInvocationMap");
	}

	@Test
	public void shouldMutateTreeSetFromInterfaceInvoacation() throws Exception {
		assertMethodResultEquals(NOT_MUTATED_TREE_SET, "getFromInterfaceInvocationSet");
		assertMutatedMethodResultEquals(EXPECTED_MUTATED_TREE_SET, 12, "getFromInterfaceInvocationSet");
	}
}
