package resources.operators.guards;

public class DifferentLoopsTestClass {
	long timeout;

	public int basicLoop() {
		int x = 1;
		for (long i = 0; i < 10; i++) {
			sleeping();
			x++;
		}

		return x;
	}

	public int whileLoop() {
		int i = 0;
		while (i < 10) {
			sleeping();
			i++;
		}
		return i;
	}

	public int foreachLoop() {
		String all = "";
		String[] sss = new String[]{"A", "B", "C", "D", "E", "F"};
		for (String s : sss) {
			all += s;
			sleeping();
		}
		return all.length();
	}

	public int dowhileLoop() {
		int i = 0;
		do {
			sleeping();
			i++;
		} while (i < 10);
		return i;
	}

	public int nestedLoop() {
		int x = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 3; j++) {
				sleeping();
				x++;
			}
		}
		return x;
	}

	public int nestedLoopMethodLoop() {
		int x = 0;
		for (int i = 0; i < 10; i++) {
			basicLoop();
			sleeping();
		}
		return x;
	}

	private void sleeping() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
