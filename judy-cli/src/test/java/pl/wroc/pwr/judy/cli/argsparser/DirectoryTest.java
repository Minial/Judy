package pl.wroc.pwr.judy.cli.argsparser;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DirectoryTest {

	@Test
	public void testListJars() throws Exception {
		final List<String> expected = new LinkedList<>();
		expected.add("lib" + File.separator + "test1.jar");
		expected.add("lib" + File.separator + "test2.jar");

		final Directory dir = new Directory(new File("src/test/resources").getCanonicalPath(), "lib");
		Assert.assertTrue(dir.listJars().containsAll(expected));
	}

	@Test
	public void testListClasses1() throws Exception {
		final List<String> expected = new LinkedList<>();
		expected.add("pl.wroc.Class1");
		expected.add("pl.wroc.Class2");
		expected.add("pl.wroc.pwr.Class3");

		final Directory dir = new Directory(new File("src/test/resources").getCanonicalPath(), "classes", "^.*(?<!Test)$");
		Assert.assertEquals(sorted(expected), sorted(dir.listClasses()));
	}

	@Test
	public void testListClasses2() throws Exception {
		final List<String> expected = new LinkedList<>();
		expected.add("pl.wroc.pwr.ClassTest");

		final Directory dir = new Directory(new File("src/test/resources").getCanonicalPath(), "classes", ".*Test");
		Assert.assertEquals(sorted(expected), sorted(dir.listClasses()));
	}

	private List<String> sorted(final List<String> list) {
		Collections.sort(list);
		return list;
	}

}
