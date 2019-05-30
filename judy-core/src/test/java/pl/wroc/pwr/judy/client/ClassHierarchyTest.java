package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.helpers.TestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ClassHierarchyTest {
	private final Map<String, String> inheritance = new HashMap<>();

	@Before
	public void setUp() {
		inheritance.put("parent2", "parent3");
		inheritance.put("parent1", "parent2");
		inheritance.put("child1", "parent1");
		inheritance.put("child2", "parent1");
		inheritance.put("child3", "parent1");
		inheritance.put("child4", "parent2");
	}

	@Test
	public void testGetDirectSubclasses() {
		final ClassHierarchy hierarchy = new ClassHierarchy(inheritance);

		assertEquals(TestHelper.list("child1", "child2", "child3"),
				TestHelper.sorted(hierarchy.getDirectSubclasses("parent1")));
		assertEquals(TestHelper.list("child4", "parent1"), TestHelper.sorted(hierarchy.getDirectSubclasses("parent2")));
		assertEquals(TestHelper.<String>list(), TestHelper.sorted(hierarchy.getDirectSubclasses("other")));
	}

	@Test
	public void testGetAllSuperclasses() {
		final ClassHierarchy hierarchy = new ClassHierarchy(inheritance);

		assertEquals(TestHelper.list("parent1", "parent2", "parent3"),
				TestHelper.sorted(hierarchy.getAllSuperclasses("child3")));
		assertEquals(TestHelper.list("parent2", "parent3"), TestHelper.sorted(hierarchy.getAllSuperclasses("parent1")));
		assertEquals(TestHelper.<String>list(), TestHelper.sorted(hierarchy.getAllSuperclasses("parent3")));
		assertEquals(TestHelper.<String>list(), TestHelper.sorted(hierarchy.getDirectSubclasses("other")));
	}
}
