package pl.wroc.pwr.judy.utils;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;

public class ArgumentsTest {

	@Test
	public void testGetCompatibleOrders() {
		Assert.assertTrue(Arguments.getCompatibleOrders("(IID)F", "(DD)F").isEmpty());
		Assert.assertEquals(0, Arguments.getCompatibleOrders("(IID)F", "()F").get(0).length);
		Assert.assertEquals(6, Arguments.getCompatibleOrders("(IIID)F", "(DII)F").size());
	}

	@Test
	public void testGetDescForNewOrder() {
		Type[] types = new Type[]{Type.CHAR_TYPE, Type.INT_TYPE, Type.DOUBLE_TYPE};

		Assert.assertEquals("(IDC)F", Arguments.getDescForNewOrder(types, Type.FLOAT_TYPE, new int[]{1, 2, 0}));
		Assert.assertEquals("(D)F", Arguments.getDescForNewOrder(types, Type.FLOAT_TYPE, new int[]{2}));
		Assert.assertEquals("(II)F", Arguments.getDescForNewOrder(types, Type.FLOAT_TYPE, new int[]{1, 1}));
		Assert.assertEquals("(DIC)F", Arguments.getDescForNewOrder(types, Type.FLOAT_TYPE, new int[]{2, 1, 0}));
	}
}
