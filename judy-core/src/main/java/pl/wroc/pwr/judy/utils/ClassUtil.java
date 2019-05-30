package pl.wroc.pwr.judy.utils;

/**
 * @author mnowak
 */
public class ClassUtil {
	/**
	 * @param owner - internal name of the specified class
	 * @return The Class object for the class with the specified name.
	 */
	public static Class<?> getClass(String owner) {
		String className = owner.replace("/", ".");

		Class<?> cl = null;
		try {
			cl = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
		return cl;
	}
}
