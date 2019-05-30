package pl.wroc.pwr.judy.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.Map.Entry;

/**
 * Utility class for methods' arguments operations.
 *
 * @author pmiwaszko
 */
public final class Arguments {
	private final static Logger LOGGER = LogManager.getLogger(Arguments.class);
	private static final int MAX_PERMUTATIONS = 120;
	private static final int MAX_ORDERS = 5040;

	private Arguments() {
		// utility class
	}

	/**
	 * Return all possible orders of arguments in a target method call.
	 *
	 * @param mDesc description of original method
	 * @param rDesc description of target method
	 * @return one of:
	 * <ul>
	 * <li>list of orders</li>
	 * <li>list with one empty array inside when target method takes no
	 * arguments</li>
	 * <li>empty list when methods are incompatible</li>
	 * </ul>
	 */
	public static List<int[]> getCompatibleOrders(String mDesc, String rDesc) {
		Type[] mArgs = Type.getArgumentTypes(mDesc);
		Type[] rArgs = Type.getArgumentTypes(rDesc);
		if (mArgs.length >= rArgs.length) {
			Map<String, Integer> mMap = getArgumentsMap(mArgs);
			Map<String, Integer> rMap = getArgumentsMap(rArgs);
			if (isCompatible(mMap, rMap)) {
				Map<String, int[]> mIndexes = getIndexesMap(mArgs, mMap);
				Map<String, List<int[]>> parts = new HashMap<>();
				// collect permutations for each type
				for (Entry<String, Integer> entry : rMap.entrySet()) {
					Permutation perm = new Permutation(mIndexes.get(entry.getKey()), entry.getValue());
					int count = 0;
					List<int[]> typeParts = new LinkedList<>();
					int[] part = perm.next();
					while (count < MAX_PERMUTATIONS && part != null) {
						typeParts.add(part);
						part = perm.next();
						count++;
					}
					if (count >= MAX_PERMUTATIONS) {
						LOGGER.debug("Max number of permutations have been reached.");
					}
					parts.put(entry.getKey(), typeParts);
				}
				// merge permutations of all types
				return mergeParts(parts, rArgs, getIndexesMap(rArgs, rMap), mDesc.equals(rDesc));
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Create a new description of a method for a specified order.
	 */
	public static String getDescForNewOrder(Type[] mArgs, Type ret, int[] newOrder) {
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		for (int i : newOrder) {
			builder.append(mArgs[i].getDescriptor());
		}
		builder.append(')');
		builder.append(ret.getDescriptor());
		return builder.toString();
	}

	/**
	 * Create a map with count of arguments of each type.
	 */
	private static Map<String, Integer> getArgumentsMap(Type[] args) {
		Map<String, Integer> map = new HashMap<>();
		for (Type type : args) {
			String desc = type.getDescriptor();
			Integer val = map.get(desc);
			int count = val == null ? 1 : val + 1;
			map.put(desc, count);
		}
		return map;
	}

	/**
	 * Check is methods are compatible, i.e. replacement's arguments make a
	 * subset of the original method's arguments.
	 */
	private static boolean isCompatible(Map<String, Integer> mMap, Map<String, Integer> rMap) {
		for (Entry<String, Integer> entry : mMap.entrySet()) {
			Integer rCount = rMap.get(entry.getKey());
			if (rCount != null && rCount > entry.getValue()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return map with indexes of arguments of each type.
	 */
	private static Map<String, int[]> getIndexesMap(Type[] mArgs, Map<String, Integer> mMap) {
		Map<String, int[]> map = new HashMap<>();
		Map<String, Integer> counts = new HashMap<>();
		for (int i = 0; i < mArgs.length; i++) {
			String desc = mArgs[i].getDescriptor();
			int[] array = map.get(desc);
			if (array == null) {
				array = new int[mMap.get(desc)];
			}
			Integer val = counts.get(desc);
			int count = val == null ? 1 : val + 1;
			counts.put(desc, count);
			array[count - 1] = i;
			map.put(desc, array);
		}
		return map;
	}

	/**
	 * Merge permutations of all types and store them in a list of orders.
	 */
	private static List<int[]> mergeParts(Map<String, List<int[]>> parts, Type[] rArgs, Map<String, int[]> rIndexes,
										  boolean sameDesc) {
		long count = 1;
		Part partsChain = null;
		for (Entry<String, List<int[]>> entry : parts.entrySet()) {
			// create chain of parts
			Part next = new Part(entry.getKey(), entry.getValue(), partsChain);
			partsChain = next;
			// count number of orders & create
			count *= parts.get(entry.getKey()).size();
			if (count > MAX_ORDERS) {
				count = MAX_ORDERS;
				LOGGER.debug("Max number of orders have been reached.");
			}
		}

		List<int[]> orders = new ArrayList<>((int) count);
		if (partsChain != null) {
			for (int i = 0; i < count; i++) {
				if (sameDesc) {
					// skip order identical to the original one
					int[] temp = makeOrder(partsChain.next(), rArgs, rIndexes);
					if (!isOriginal(temp)) {
						orders.add(temp);
					}
				} else {
					orders.add(makeOrder(partsChain.next(), rArgs, rIndexes));
				}
			}
		} else {
			orders.add(new int[0]); //
		}
		return orders;
	}

	/**
	 * Concatenate parts to create one order array.
	 */
	private static int[] makeOrder(Map<String, int[]> part, Type[] rArgs, Map<String, int[]> rIndexes) {
		int[] order = new int[rArgs.length];
		for (Entry<String, int[]> entry : rIndexes.entrySet()) {
			int[] values = part.get(entry.getKey());
			int i = 0;
			for (int index : entry.getValue()) {
				order[index] = values[i];
				i++;
			}
		}
		return order;
	}

	/**
	 * Check if given order is same as original.
	 */
	private static boolean isOriginal(int[] order) {
		for (int i = 0; i < order.length; i++) {
			if (order[i] != i) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Private class representing possible orders of arguments of one type only.
	 */
	private static class Part {
		public Part(String type, List<int[]> parts, Part next) {
			super();
			this.type = type;
			this.parts = parts;
			this.next = next;
			index = 0;
		}

		public Map<String, int[]> next() {
			Map<String, int[]> map = new HashMap<>();
			get(map);
			inc();
			return map;
		}

		private void inc() {
			index++;
			if (index == parts.size()) {
				index = 0;
				if (next != null) {
					next.inc();
				}
			}
		}

		private void get(Map<String, int[]> map) {
			map.put(type, parts.get(index));
			if (next != null) {
				next.get(map);
			}
		}

		private final String type;
		private final List<int[]> parts;
		private int index;
		private final Part next;
	}
}
