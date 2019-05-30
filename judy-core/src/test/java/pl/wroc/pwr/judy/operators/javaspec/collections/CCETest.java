package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.javaspec.collections.CCETestClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Mutate(operator = CCE.class, targetClass = CCETestClass.class)
public class CCETest extends AbstractMutationOperatorTesting {

	List<String> expectedList = new ArrayList<>();
	Set<Integer> expectedSet = new HashSet<>();

	@Override
	public void init() throws Exception {
		super.init();

		expectedList.add("a");
		expectedList.add("b");
		expectedList.add("c");
		expectedList.add("d");
		expectedList.add("e");
		expectedList.add("f");

		expectedSet.add(1);
		expectedSet.add(2);
	}

	@Test
	public void shouldCountMutationPoints() throws Exception {
		assertMutationPointsCountEquals(8);
	}

	@Test
	public void shouldCountMutants() throws Exception {
		assertMutantsCountEquals(8);
	}

	@Test
	public void shouldMutateListLocalVariable() throws Exception {
		assertMethodResultEquals(expectedList, "getElementsList");
		assertMutatedMethodResultEquals(new ArrayList<String>(), 0, "getElementsList");
	}

	@Test
	public void shouldMutateSetLocalVariable() throws Exception {
		assertMethodResultEquals(expectedSet, "getElementsSet");
		assertMutatedMethodResultEquals(new HashSet<String>(), 1, "getElementsSet");
	}

	@Test
	public void shouldMutateCollectionClassMember() throws Exception {
		assertMethodResultEquals(expectedSet, "getElementsCollection");
		assertMutatedMethodResultEquals(new HashSet<Integer>(), 2, "getElementsCollection");
	}

	@Test
	public void shouldMutateArrayListExternalObject() throws Exception {
		assertMethodResultEquals(expectedList, "getElementsExternalObject");
		assertMutatedMethodResultEquals(new ArrayList<String>(), 3, "getElementsExternalObject");
	}

	@Test
	public void shouldMutateArrayListFromPrivateMethod() throws Exception {
		assertMethodResultEquals(expectedSet, "getElementsFromPrivateMethod");
		assertMutatedMethodResultEquals(new HashSet<String>(), 4, "getElementsFromPrivateMethod");
	}

	@Test
	public void shouldMutateArrayListUsingStatic() throws Exception {
		assertMethodResultEquals(expectedList, "getFromStatic");
		assertMutatedMethodResultEquals(new ArrayList<String>(), 5, "getFromStatic");
	}

	@Test
	public void shouldMutateArrayListUsingStaticDirectly() throws Exception {
		assertMethodResultEquals(expectedList, "getFromStaticDirectly");
		assertMutatedMethodResultEquals(new ArrayList<String>(), 6, "getFromStaticDirectly");
	}

	@Test
	public void shouldMutateListFromInterfaceInvocation() throws Exception {
		assertMethodResultEquals(expectedSet, "getFromInterfaceInvocation");
		assertMutatedMethodResultEquals(new HashSet<String>(), 7, "getFromInterfaceInvocation");
	}

	@Test
	public void shouldReturnOperatorDescription() throws Exception {
		assertEquals("Clears the collection", new CCE().getDescription());
	}
}
