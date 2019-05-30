package resources.operators.javaspec.collections;

import java.util.LinkedHashSet;

public class CSTTestClass {
	private static final LinkedHashSet<String> LINKED_HASH_SET = new LinkedHashSet<>();

	static {
		LINKED_HASH_SET.add("b");
		LINKED_HASH_SET.add("a");
		LINKED_HASH_SET.add("c");
		LINKED_HASH_SET.add("d");
		LINKED_HASH_SET.add("e");
		LINKED_HASH_SET.add("f");
	}

	public LinkedHashSet<String> getElementsLinkedHashSet() {
		LinkedHashSet<String> elements = new LinkedHashSet<>();
		elements.addAll(LINKED_HASH_SET);
		return elements;
	}

	public LinkedHashSet<String> getElementsExternalObject() {
		ExternalCallsHelper utl = new ExternalCallsHelper();
		return utl.getLinkedHashSet();
	}

	public LinkedHashSet<String> getElementsFromPrivateMethod() {
		return getElementsPrivateSet();
	}

	private LinkedHashSet<String> getElementsPrivateSet() {
		LinkedHashSet<String> set = new LinkedHashSet<>();
		set.addAll(LINKED_HASH_SET);
		return set;
	}

	public void dummyMethod() {
	}

	public LinkedHashSet<String> getFromStatic() {
		return ExternalCallsHelper.getStaticLinkedHashSet();
	}

	public LinkedHashSet<String> getFromStaticDirectly() {
		return LINKED_HASH_SET;
	}

	public LinkedHashSet<String> getFromInterfaceInvocation() {
		LinkedHashSetReturner returner = new ExternalCallsHelper();
		return returner.returnSomeLinkedHashSet();
	}

}
