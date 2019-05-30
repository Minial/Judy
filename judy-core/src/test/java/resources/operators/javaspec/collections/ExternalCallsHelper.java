package resources.operators.javaspec.collections;

import java.util.*;

public class ExternalCallsHelper implements ListReturner, SortedCollectionReturner, LinkedHashSetReturner {
	public ArrayList<String> getStrings() {
		ArrayList<String> strings = new ArrayList<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	public static List<String> getStaticList() {
		ArrayList<String> strings = new ArrayList<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	public TreeSet<String> getTreeSet() {
		TreeSet<String> strings = new TreeSet<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	public static TreeSet<String> getStaticTreeSet() {
		TreeSet<String> strings = new TreeSet<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	public TreeMap<String, String> getTreeMap() {
		TreeMap<String, String> strings = new TreeMap<>();
		strings.put("a", "a");
		strings.put("b", "b");
		strings.put("c", "c");
		strings.put("d", "d");
		strings.put("e", "e");
		strings.put("f", "f");
		return strings;
	}

	public static TreeMap<String, String> getStaticTreeMap() {
		TreeMap<String, String> strings = new TreeMap<>();
		strings.put("a", "a");
		strings.put("b", "b");
		strings.put("c", "c");
		strings.put("d", "d");
		strings.put("e", "e");
		strings.put("f", "f");
		return strings;
	}

	public static LinkedHashSet<String> getStaticLinkedHashSet() {
		LinkedHashSet<String> strings = new LinkedHashSet<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	public LinkedHashSet<String> getLinkedHashSet() {
		LinkedHashSet<String> strings = new LinkedHashSet<>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		strings.add("d");
		strings.add("e");
		strings.add("f");
		return strings;
	}

	@Override
	public List<String> returnSomeList() {
		return getStrings();
	}

	@Override
	public LinkedHashSet<String> returnSomeLinkedHashSet() {
		return getLinkedHashSet();
	}

	@Override
	public TreeSet<String> returnSomeTreeSet() {
		return getTreeSet();
	}

	@Override
	public TreeMap<String, String> returnSomeTreeMap() {
		return getTreeMap();
	}
}
