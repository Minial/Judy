package pl.wroc.pwr.judy.cli.argsparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PropertiesTest {

	private Properties properties;

	@Before
	public void setUp() throws IOException {
		properties = new Properties();
		properties.load("src/test/resources/properties.txt");
	}

	@Test
	public void testGet() {
		assertEquals("A;B;CD", properties.get("prop1"));
		assertEquals("A B C D", properties.get("prop2"));
		assertEquals("A;B;C;D;E;F;G", properties.get("prop3"));
		assertEquals("", properties.get("prop4"));
		Assert.assertNull(properties.get("prop5"));
	}

	@Test
	public void testGetDefault() {
		assertEquals("A;B;CD", properties.get("prop1", "default"));
		assertEquals("", properties.get("prop4", "default"));
		assertEquals("default", properties.get("prop5", "default"));
	}

	@Test
	public void testGetList() {
		assertArrayEquals(new String[]{"A", "B", "CD"}, properties.getList("prop1").toArray());
		assertArrayEquals(new String[]{"A B C D"}, properties.getList("prop2").toArray());
		assertArrayEquals(new String[]{"A", "B", "C", "D", "E", "F", "G"}, properties.getList("prop3").toArray());
		assertArrayEquals(new String[]{""}, properties.getList("prop4").toArray());
		assertTrue(properties.getList("prop5").isEmpty());
	}

}
