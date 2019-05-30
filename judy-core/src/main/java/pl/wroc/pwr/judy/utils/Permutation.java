package pl.wroc.pwr.judy.utils;

import java.util.Arrays;

/**
 * Simple permutation algorithm.
 */
public class Permutation {
	/**
	 * <code>Permutation</code> constructor.
	 */
	public Permutation(int[] array) {
		this(array, array.length);
	}

	/**
	 * <code>Permutation</code> constructor.
	 */
	public Permutation(int[] array, int k) {
		if (array != null) {
			this.array = array.clone();
		}
		swap = new int[k];
		for (int i = 0; i < swap.length; i++) {
			swap[i] = i;
		}
	}

	/**
	 * Generate next permutation.
	 *
	 * @return next permutation or <code>null</code> after the last permutation
	 * was generated
	 */
	public int[] next() {
		if (array == null) {
			return null;
		}
		int[] res = Arrays.copyOf(array, swap.length);
		int i = swap.length - 1;
		while (i >= 0 && swap[i] == array.length - 1) {
			swap(i, swap[i]);
			swap[i] = i;
			i--;
		}
		if (i < 0) {
			array = null;
		} else {
			int prev = swap[i];
			swap(i, prev);
			int next = prev + 1;
			swap[i] = next;
			swap(i, next);
		}
		return res;
	}

	private void swap(int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	private int[] array;
	private final int[] swap;
}
