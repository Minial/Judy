package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IClassInfoCache;
import pl.wroc.pwr.judy.helpers.TestHelper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static pl.wroc.pwr.judy.helpers.TestHelper.asStringArray;
import static pl.wroc.pwr.judy.helpers.TestHelper.sorted;

public class ClassInfoCacheTest {

	@Before
	public void setUp() throws Exception {
		cache = new ClassInfoCache(TestHelper.getTestsBytecodeCache(), 200);
	}

	@Test
	public void testGet_Bytecode() {
		IClassInfo info = cache.get("resources.ClassInfoCacheResource");

		assertEquals("resources/ClassInfoCacheResource", info.getClassName());

		assertArrayEquals(new Object[]{"pl/wroc/pwr/judy/common/IBytecodeCache"}, info.getInterfaces().toArray());

		assertArrayEquals(sorted(new String[]{"<init>", "getURLs", "get"}),
				sorted(asStringArray(info.getDeclaredMethods())));

		assertEquals("java/lang/Object", info.getSuperClassName());
	}

	@Test
	public void testGet_Stream() {
		IClassInfo info = cache.get("java/lang.Object");

		assertEquals("java/lang/Object", info.getClassName());

		assertArrayEquals(sorted(new String[]{"<clinit>", "<init>", "registerNatives", "getClass", "hashCode",
						"equals", "clone", "toString", "notify", "notifyAll", "wait", "wait", "wait", "finalize"}),
				sorted(asStringArray(info.getDeclaredMethods())));

		assertArrayEquals(new Object[0], info.getInterfaces().toArray());

		assertEquals(null, info.getSuperClassName());
	}

	IClassInfoCache cache;
}
