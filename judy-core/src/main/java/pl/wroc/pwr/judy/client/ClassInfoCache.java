package pl.wroc.pwr.judy.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IClassInfoCache;
import pl.wroc.pwr.judy.utils.Cache;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Cached access to information about classes.
 *
 * @author pmiwaszko
 * @see ClassInfo
 */
class ClassInfoCache implements IClassInfoCache {
	private static final Logger LOGGER = LogManager.getLogger(ClassInfoCache.class);
	private final Map<String, IClassInfo> cache;
	private final IBytecodeCache bytecodeCache;

	public ClassInfoCache(IBytecodeCache bytecodeCache, int cacheSize) {
		cache = Collections.synchronizedMap(new Cache<String, IClassInfo>(cacheSize));
		this.bytecodeCache = bytecodeCache;
	}

	/**
	 * Get detailed information about given class. This method uses ASM-based
	 * approach.
	 */
	@Override
	public IClassInfo get(String name) {
		String className = name;
		IClassInfo classInfo = null;
		if (className != null) {
			className = className.replace('.', '/');
			classInfo = cache.get(className);
		}
		if (classInfo == null) {
			classInfo = loadClassInfo(className);
		}

		if (classInfo != null) {
			cache.put(className, classInfo);
		} else {
			classInfo = ClassInfo.EMPTY_CACHE;
		}
		return classInfo;
	}

	/**
	 * Load detailed information about given class.
	 */
	private IClassInfo loadClassInfo(String className) {
		IClassInfo classInfo = null;
		byte[] bytecode = bytecodeCache.get(className);

		if (bytecode != null) {
			classInfo = loadFromBytecode(bytecode);
		} else {
			classInfo = loadFromStream(className);
		}

		return classInfo;
	}

	private IClassInfo loadFromBytecode(byte[] bytecode) {
		ClassReader reader = new ClassReader(bytecode);
		ClassInfoVisitor visitor = new ClassInfoVisitor();
		reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
		return visitor.getClassInfo();
	}

	private IClassInfo loadFromStream(String className) {
		try {
			ClassReader reader = new ClassReader(className);
			ClassInfoVisitor visitor = new ClassInfoVisitor();
			reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
			return visitor.getClassInfo();
		} catch (IOException e) {
			LOGGER.warn("IOException while loading " + className, e);
		}
		return null;
	}
}
