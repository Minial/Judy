package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.*;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.LinkedList;
import java.util.List;

/**
 * Environment implementation.
 *
 * @author pmiwaszko
 */
public class Environment implements IEnvironment {
	/**
	 * Default size of cache with classes information.
	 */
	public static final int DEAFULT_CLASSINFO_CACHE_SIZE = 300;

	private final IClassHierarchy hierarchy;
	private final IClassInfoCache cache; // TODO should this cache be
	// centralized?

	Environment(IBytecodeCache cache, IClassHierarchy hierarchy) {
		this.hierarchy = hierarchy;
		this.cache = new ClassInfoCache(cache, DEAFULT_CLASSINFO_CACHE_SIZE);
	}

	@Override
	public List<String> getDirectSubclasses(String className) {
		// TODO: add caching in Environment implementation
		return hierarchy.getDirectSubclasses(className);
	}

	@Override
	public List<String> getAllSuperclasses(String className) {
		return hierarchy.getAllSuperclasses(className);
	}

	@Override
	public IClassInfo getClassInfo(String className) {
		return cache.get(className);
	}

	@Override
	public List<IMethodInfo> getAllMethods(String className) {
		// TODO what about overridden methods and their names?
		List<IMethodInfo> methods = new LinkedList<>();
		methods.addAll(cache.get(className).getDeclaredMethods());
		List<String> superClasses = getAllSuperclasses(className);
		for (String name : superClasses) {
			methods.addAll(cache.get(name).getDeclaredMethods());
		}
		return methods;
	}

	@Override
	public boolean isHidingField(String className, String fieldName, String fieldDesc) {
		IClassInfo classInfo = cache.get(className);
		IFieldInfo fieldInfo = null;
		for (IFieldInfo f : classInfo.getDeclaredFields()) {
			if (f.getName().equals(fieldName) && f.getDescription().equals(fieldDesc)) {
				fieldInfo = f;
				break;
			}
		}
		if (fieldInfo != null) {
			IClassInfo superClassInfo = cache.get(classInfo.getSuperClassName());
			for (IFieldInfo f : superClassInfo.getDeclaredFields()) {
				if (f.getName().equals(fieldName) && f.getDescription().equals(fieldDesc)
						// mind members with default access and super class form
						// different package
						&& checkAccess(f.getAccess(), fieldInfo.getAccess(), superClassInfo.getClassName(), className)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isOverridingMethod(String className, String methodName, String methodDesc) {
		IClassInfo classInfo = cache.get(className);
		IMethodInfo methodInfo = null;
		for (IMethodInfo m : classInfo.getDeclaredMethods()) {
			if (m.getName().equals(methodName) && m.getDesc().equals(methodDesc)) {
				methodInfo = m;
				break;
			}
		}
		if (methodInfo != null) {
			IClassInfo superClassInfo = cache.get(classInfo.getSuperClassName());
			for (IMethodInfo m : superClassInfo.getDeclaredMethods()) {
				if (m.getName().equals(methodName) && m.getDesc().equals(methodDesc)
						// mind members with default access and super class form
						// different package
						&& checkAccess(m.getAccess(), methodInfo.getAccess(), superClassInfo.getClassName(), className)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if given accesses are compatible.
	 *
	 * @param superAccess    access of member in super class
	 * @param access         access of member in subclass
	 * @param superClassName name of super class
	 * @param className      name of subclass
	 */
	private boolean checkAccess(int superAccess, int access, String superClassName, String className) {
		return !Accesses.isPrivate(superAccess)
				&& Accesses.isStatic(superAccess) == Accesses.isStatic(access)
				&& !(Accesses.isDefault(superAccess) && !getPackageName(superClassName).equals(
				getPackageName(className)));
	}

	/**
	 * Get package name of given class.
	 */
	private String getPackageName(String className) {
		int index = className.lastIndexOf('/');
		return index == -1 ? className : className.substring(0, index);
	}
}
