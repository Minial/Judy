package pl.wroc.pwr.judy.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PermutationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNext() {
		Permutation perm = new Permutation(new int[]{1, 2, 3});
		Assert.assertArrayEquals(new int[]{1, 2, 3}, perm.next());
		Assert.assertArrayEquals(new int[]{1, 3, 2}, perm.next());
		Assert.assertArrayEquals(new int[]{2, 1, 3}, perm.next());
		Assert.assertArrayEquals(new int[]{2, 3, 1}, perm.next());
		Assert.assertArrayEquals(new int[]{3, 2, 1}, perm.next());
		Assert.assertArrayEquals(new int[]{3, 1, 2}, perm.next());
		Assert.assertNull(perm.next());
		Assert.assertNull(perm.next());
		Assert.assertNull(perm.next());
	}
}
