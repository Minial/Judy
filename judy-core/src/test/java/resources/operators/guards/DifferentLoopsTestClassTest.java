package resources.operators.guards;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DifferentLoopsTestClassTest {

	@Test
	public void forLoopTest() throws Exception {
		DifferentLoopsTestClass fl = new DifferentLoopsTestClass();
		fl.basicLoop();
	}

	@Test
	public void listTest() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("A");

		assertEquals(1, list.size());
		assertEquals(0, list.indexOf("A"));
	}
}
