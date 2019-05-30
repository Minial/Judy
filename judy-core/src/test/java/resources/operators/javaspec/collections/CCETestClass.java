package resources.operators.javaspec.collections;

import java.util.*;

public class CCETestClass {
	private Collection<Integer> integers = new HashSet<>();
	private static final Map<Integer, String> BASIC_MAP = new HashMap<>();
	private static final List<String> BASIC_LIST = new ArrayList<>();
	private static final Set<Integer> BASIC_SET = new HashSet<>();

	static {
		BASIC_LIST.add("a");
		BASIC_LIST.add("b");
		BASIC_LIST.add("c");
		BASIC_LIST.add("d");
		BASIC_LIST.add("e");
		BASIC_LIST.add("f");

		BASIC_SET.add(1);
		BASIC_SET.add(2);

		BASIC_MAP.put(1, "a");
		BASIC_MAP.put(2, "b");

	}

	public List<String> getElementsList() {
		List<String> elements = new ArrayList<>();
		elements.addAll(BASIC_LIST);
		return elements;
	}

	@SuppressWarnings("rawtypes")
	public Set getElementsSet() {
		Set<Integer> elements = new HashSet<>();
		elements.addAll(BASIC_SET);
		return elements;
	}

	public Collection<Integer> getElementsCollection() {
		integers.addAll(BASIC_SET);
		return integers;
	}

	public ArrayList<String> getElementsExternalObject() {
		ExternalCallsHelper utl = new ExternalCallsHelper();
		return utl.getStrings();
	}

	public Set<Integer> getElementsFromPrivateMethod() {
		return getElementsPrivateSet();
	}

	private Set<Integer> getElementsPrivateSet() {
		Set<Integer> set = new HashSet<>();
		set.addAll(BASIC_SET);
		return set;
	}

	public void dummyMethod() {
	}

	public List<String> getFromStatic() {
		return ExternalCallsHelper.getStaticList();
	}

	public List<String> getFromStaticDirectly() {
		return BASIC_LIST;
	}

	public Set<Integer> getFromInterfaceInvocation() {
		return BASIC_MAP.keySet();
	}
}