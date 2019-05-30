package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IClassInfoCache;
import pl.wroc.pwr.judy.common.IFieldInfo;
import pl.wroc.pwr.judy.helpers.TestHelper;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FieldInfoTest {

	@Before
	public void setUp() throws Exception {
		IBytecodeCache bcache = TestHelper.getTestsBytecodeCache();
		IClassInfoCache cache = new ClassInfoCache(bcache, 100);
		IClassInfo cinfo = cache.get("resources/FieldInfoResource");
		infos = cinfo.getDeclaredFields();
	}

	@Test
	public void testGetAccess() {
		assertEquals(Opcodes.ACC_PRIVATE, infos.get(0).getAccess());
	}

	@Test
	public void testGetName() {
		assertEquals("a", infos.get(0).getName());
	}

	@Test
	public void testGetDesc() {
		assertEquals("I", infos.get(0).getDescription());
	}

	@Test
	public void testGetSignature() {
		assertEquals(null, infos.get(0).getSignature());
	}

	@Test
	public void testGetValue() {
		assertEquals(null, infos.get(0).getValue());
	}

	private List<IFieldInfo> infos;
}
