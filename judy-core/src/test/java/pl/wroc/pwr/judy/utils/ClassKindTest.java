package pl.wroc.pwr.judy.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClassKindTest {

	@Test
	public void shouldRecognizeNormalClassAsMutable() throws Exception {
		assertTrue(ClassKind.NORMAL.isMutable());
	}

	@Test
	public void shouldRecognizeOtherClassKindsAsNotMutable() throws Exception {
		assertFalse(ClassKind.ABSTRACT.isMutable());
		assertFalse(ClassKind.APPLET.isMutable());
		assertFalse(ClassKind.GUI.isMutable());
		assertFalse(ClassKind.INTERFACE.isMutable());
		assertFalse(ClassKind.NO_TESTS.isMutable());
		assertFalse(ClassKind.UNKNOWN.isMutable());
	}

}
