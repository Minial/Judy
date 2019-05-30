package pl.wroc.pwr.judy.helpers;

import java.lang.reflect.Field;

/**
 * ObjectHelper class provides methods for accessing fields of given objects
 * using Java reflection. Use it to modify or get private members values.
 *
 * @author TM
 */
public class ObjectHelper {

	/**
	 * Default constructor. This is utility class thus it is hidden
	 */
	private ObjectHelper() {
	}

	/**
	 * Sets object field value using Java reflection
	 *
	 * @param obj       source object
	 * @param fieldName field to be edited
	 * @param value     value to be set
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		try {
			Class<?> clazz = obj.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException("Cannot change field: " + fieldName + " to value: " + value + " in object: " + obj);
		}
	}

	/**
	 * Gets value of the specified field using Java reflection
	 *
	 * @param obj       source object
	 * @param fieldName field to get
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		try {
			Class<?> clazz = obj.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new RuntimeException("Cannot get field: " + fieldName + " value from object: " + obj);
		}

	}

}
