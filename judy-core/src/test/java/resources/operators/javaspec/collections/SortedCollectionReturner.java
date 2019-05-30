package resources.operators.javaspec.collections;

import java.util.TreeMap;
import java.util.TreeSet;

public interface SortedCollectionReturner {
	TreeSet<String> returnSomeTreeSet();

	TreeMap<String, String> returnSomeTreeMap();
}
