package pl.wroc.pwr.judy.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessesTest {

	@Test
	public void testAdd() {
		int access = Accesses.ACC_PROTECTED | Accesses.ACC_FINAL;

		assertTrue(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertFalse(Accesses.isStatic(access));

		access = Accesses.add(access, Accesses.ACC_STATIC);
		assertTrue(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));

		access = Accesses.add(access, Accesses.ACC_FINAL);
		assertTrue(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));

		access = Accesses.add(access, Accesses.ACC_PRIVATE);
		assertTrue(Accesses.isPrivate(access));
		assertTrue(Accesses.isProtected(access)); // be careful!
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));
	}

	@Test
	public void testRemove() {
		int access = Accesses.ACC_PROTECTED | Accesses.ACC_FINAL | Accesses.ACC_STATIC;
		assertTrue(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));

		access = Accesses.remove(access, Accesses.ACC_PROTECTED);
		assertFalse(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));

		assertFalse(Accesses.isPrivate(access));
		access = Accesses.add(access, Accesses.ACC_PRIVATE);
		assertTrue(Accesses.isPrivate(access));
		assertFalse(Accesses.isProtected(access));
		assertTrue(Accesses.isFinal(access));
		assertTrue(Accesses.isStatic(access));
	}
}
