package resources.operators.javaspec.collections;

import java.util.TreeMap;
import java.util.TreeSet;

public class SortedCollectionsUsageTestClass {
	private static final TreeSet<String> BASIC_TREE_SET = new TreeSet<>();
	private static final TreeMap<String, String> BASIC_TREE_MAP = new TreeMap<>();
	private final TreeSet<String> treeSetField = BASIC_TREE_SET;
	private final TreeMap<String, String> treeMapField = BASIC_TREE_MAP;

	static {
		BASIC_TREE_SET.add("a");
		BASIC_TREE_SET.add("b");
		BASIC_TREE_SET.add("c");
		BASIC_TREE_SET.add("d");
		BASIC_TREE_SET.add("e");
		BASIC_TREE_SET.add("f");

		BASIC_TREE_MAP.put("a", "a");
		BASIC_TREE_MAP.put("b", "b");
		BASIC_TREE_MAP.put("c", "c");
		BASIC_TREE_MAP.put("d", "d");
		BASIC_TREE_MAP.put("e", "e");
		BASIC_TREE_MAP.put("f", "f");
	}

	public TreeSet<String> getMethodLocal() {
		TreeSet<String> elements = new TreeSet<>();
		elements.addAll(BASIC_TREE_SET);
		return elements;
	}

	public TreeSet<String> getFromExternalObject() {
		ExternalCallsHelper utl = new ExternalCallsHelper();
		return utl.getTreeSet();
	}

	public void dummyMethod() {
	}

	public TreeSet<String> getFromExternalClassStaticField() {
		return ExternalCallsHelper.getStaticTreeSet();
	}

	public TreeSet<String> getFromStaticFieldDirectly() {
		return BASIC_TREE_SET;
	}

	public TreeSet<String> getFromFieldDirectly() {
		return treeSetField;
	}

	private TreeSet<String> somePrivateMethod() {
		return treeSetField;
	}

	public TreeSet<String> getFromPrivateMethod() {
		return somePrivateMethod();
	}

	public TreeMap<String, String> getMethodLocalMap() {
		TreeMap<String, String> elements = new TreeMap<>();
		elements.putAll(BASIC_TREE_MAP);
		return elements;
	}

	public TreeMap<String, String> getFromExternalObjectMap() {
		ExternalCallsHelper utl = new ExternalCallsHelper();
		return utl.getTreeMap();
	}

	public void dummyMethodMap() {
	}

	public TreeMap<String, String> getFromExternalClassStaticFieldMap() {
		return ExternalCallsHelper.getStaticTreeMap();
	}

	public TreeMap<String, String> getFromStaticFieldDirectlyMap() {
		return BASIC_TREE_MAP;
	}

	public TreeMap<String, String> getFromFieldDirectlyMap() {
		return treeMapField;
	}

	private TreeMap<String, String> somePrivateMethodMap() {
		return treeMapField;
	}

	public TreeMap<String, String> getFromPrivateMethodMap() {
		return somePrivateMethodMap();
	}

	public TreeMap<String, String> getFromInterfaceInvocationMap() {
		SortedCollectionReturner returner = new ExternalCallsHelper();
		return returner.returnSomeTreeMap();
	}

	public TreeSet<String> getFromInterfaceInvocationSet() {
		SortedCollectionReturner returner = new ExternalCallsHelper();
		return returner.returnSomeTreeSet();
	}

}
