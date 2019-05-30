package pl.wroc.pwr.judy.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IClassHierarchy;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.helpers.TestHelper;
import pl.wroc.pwr.judy.utils.Accesses;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentTest {
	private IClassHierarchy m_hierarchy;
	private IEnvironment m_env;

	@Before
	public void setUp() throws IOException {
		Map<String, String> inheritance = new HashMap<>();
		inheritance.put("resources/EnvironmentParentResource", "java/lang/Object");
		inheritance.put("resources/EnvironmentChild1Resource", "resources/EnvironmentParentResource");
		inheritance.put("resources/EnvironmentChild2Resource", "resources/EnvironmentParentResource");
		m_env = new EnvironmentFactory(inheritance).create(TestHelper.getBytecodeCache());
		m_hierarchy = new ClassHierarchy(inheritance);
	}

	@Test
	public void testGetDirectSubclasses() {
		Assert.assertEquals(m_hierarchy.getDirectSubclasses("resources/EnvironmentParentResource"),
				m_env.getDirectSubclasses("resources/EnvironmentParentResource"));
	}

	@Test
	public void testGetAllSuperclasses() {
		Assert.assertEquals(m_hierarchy.getAllSuperclasses("resources/EnvironmentChild2Resource"),
				m_env.getAllSuperclasses("resources/EnvironmentChild2Resource"));
	}

	@Test
	public void testGetClassInfo() {
		IClassInfo info = m_env.getClassInfo("resources/EnvironmentChild1Resource");
		Assert.assertNotNull(info);

		Assert.assertEquals("resources/EnvironmentChild1Resource", info.getClassName());
		Assert.assertEquals("resources/EnvironmentParentResource", info.getSuperClassName());
		Assert.assertTrue(Accesses.isDefault(info.getAccess()));
		Assert.assertEquals(TestHelper.list("java/io/Serializable", "java/lang/Cloneable"),
				TestHelper.sorted(info.getInterfaces()));
	}

	@Test
	public void testGetAllMethods() {
		List<IMethodInfo> list = m_env.getAllMethods("resources/EnvironmentChild1Resource");
		Assert.assertNotNull(list);
		Assert.assertEquals(2 + 3 + 14, list.size());
	}

	@Test
	public void testIsHidingField() {
		Assert.assertTrue(m_env.isHidingField("resources/EnvironmentChild1Resource", "a", "I"));
		Assert.assertFalse(m_env.isHidingField("resources/EnvironmentChild1Resource", "b", "I"));
	}

	@Test
	public void testIsOverridingMethod() {
		Assert.assertTrue(m_env.isOverridingMethod("resources/EnvironmentChild2Resource", "getA", "()I"));
		Assert.assertFalse(m_env.isOverridingMethod("resources/EnvironmentChild2Resource", "getB", "()I"));
	}
}
