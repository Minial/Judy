package resources.operators.javaspec.collections;

import java.util.ArrayList;
import java.util.List;

public class ListUsageTestClass {
	private static final List<String> BASIC_LIST = new ArrayList<>();
	private final List<String> listField = BASIC_LIST;

	static {
		BASIC_LIST.add("a");
		BASIC_LIST.add("b");
		BASIC_LIST.add("c");
		BASIC_LIST.add("d");
		BASIC_LIST.add("e");
		BASIC_LIST.add("f");
	}

	public List<String> getMethodLocalList() {
		List<String> elements = new ArrayList<>();
		elements.addAll(BASIC_LIST);
		return elements;
	}

	public ArrayList<String> getListFromExternalObject() {
		ExternalCallsHelper utl = new ExternalCallsHelper();
		return utl.getStrings();
	}

	public void dummyMethod() {
	}

	public List<String> getListFromExternalClassStaticField() {
		return ExternalCallsHelper.getStaticList();
	}

	public List<String> getListFromStaticFieldDirectly() {
		return BASIC_LIST;
	}

	public List<String> getListFromFieldDirectly() {
		return listField;
	}

	private List<String> somePrivateMethod() {
		return listField;
	}

	public List<String> getListFromPrivateMethod() {
		return somePrivateMethod();
	}

	public List<String> getListFromInterfaceInvocation() {
		ListReturner returner = new ExternalCallsHelper();
		return returner.returnSomeList();
	}

}