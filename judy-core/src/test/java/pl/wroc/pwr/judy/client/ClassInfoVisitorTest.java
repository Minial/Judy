package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.helpers.TestHelper;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ClassInfoVisitorTest {
	@Before
	public void setUp() throws IOException {
		IBytecodeCache cache = TestHelper.getTestsBytecodeCache();

		byte[] bytecode = cache.get("resources/ClassInfoCacheResource");

		ClassReader reader = new ClassReader(bytecode);
		ClassInfoVisitor visitor = new ClassInfoVisitor();
		reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);

		info = visitor.getClassInfo();
	}

	@Test
	public void testGetClassName() {
		assertEquals("resources/ClassInfoCacheResource", info.getClassName());
	}

	@Test
	public void testGetInterfaces() {
		assertArrayEquals(new Object[]{"pl/wroc/pwr/judy/common/IBytecodeCache"}, info.getInterfaces().toArray());
	}

	@Test
	public void testGetDeclaredMethods() {
		assertArrayEquals(new String[]{"<init>", "get", "getURLs"},
				TestHelper.asStringArray(info.getDeclaredMethods()));
	}

	@Test
	public void testGetDeclaredFields() {
		assertArrayEquals(new Object[]{"a", "b", "c"}, TestHelper.asStringArray(info.getDeclaredFields()));
	}

	@Test
	public void testGetSuperClass() {
		assertEquals("java/lang/Object", info.getSuperClassName());
	}

	IClassInfo info;
}
