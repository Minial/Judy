package pl.wroc.pwr.judy.research;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.client.EnvironmentFactory;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.general.ICoverage;
import pl.wroc.pwr.judy.utils.ClassKind;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;

public final class ResearchHelper {
	private static String project;
	private static boolean dumpEnabled;
	private static final Logger LOGGER = LogManager.getLogger(ResearchHelper.class);

	private ResearchHelper() {
	}

	public static void setDump(boolean dump, String projectName) {
		dumpEnabled = dump;
		project = projectName;
	}

	public static void dump(IClientConfig config, IInitialTestsRun tests) {
		if (dumpEnabled) {
			if (tests.passed()) {
				dumpCoverage(config, tests);
				dumpTestSuite3(config, tests);
				dumpTestSuite3Bytecode(config, tests);
				dumpTestSuite4(config, tests);
				System.exit(0);
			} else {
				LOGGER.error("## Cannot dump coverage information, initial tests failed! ##");
			}
		}
	}

	public static void dumpCoverage(IClientConfig config, IInitialTestsRun tests) {
		IBytecodeCache cache = config.getInitialTestRunner().getBytecodeCache();
		ICoverage coverage = tests.getCoverage();
		IEnvironment env = new EnvironmentFactory(tests.getInheritance()).create(cache);

		StringBuilder sb = new StringBuilder();
		for (String name : config.getTargetClasses()) {
			byte[] bytecode = cache.get(name);
			ClassKind kind = getClassKind(name, bytecode, coverage, env);
			if (kind.equals(ClassKind.NORMAL)) {
				sb.append(name);
				sb.append(" \"");
				for (String test : coverage.getCoveringTestClasses(name)) {
					sb.append(test);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\"");
				sb.append("\n");
			}
		}

		saveFile(sb, project + "-coverage.txt");
	}

	public static void dumpTestSuite3(IClientConfig config, IInitialTestsRun tests) {
		StringBuilder sb = new StringBuilder();

		sb.append("import junit.framework.Test;\n");
		sb.append("import junit.framework.TestCase;\n");
		sb.append("import junit.framework.TestSuite;\n\n");

		sb.append("public class ");
		sb.append(project);
		sb.append("TestSuite extends TestCase {\n\n");

		sb.append("public static Test suite() {\n");
		sb.append("\tTestSuite suite = new TestSuite(\"Judy Test Suite\");\n\n");

		for (String test : config.getTestClasses()) {
			sb.append("\tsuite.addTestSuite(");
			sb.append(test);
			sb.append(".class);\n");
		}

		sb.append("\n\treturn suite;\n}\n}\n");

		saveFile(sb, project + "TestSuite.java");
	}

	public static void dumpTestSuite3Bytecode(IClientConfig config, IInitialTestsRun tests) {
		try {
			byte[] bytecode = ResearchTestSuiteGenerator.createSuite(project, config.getTestClasses());

			FileOutputStream fos = new FileOutputStream(project + "TestSuite.class");
			fos.write(bytecode);
			fos.close();
		} catch (Exception e) {
			LOGGER.error("## Cannot create bytecode for test suite " + project + "! ##");
		}
	}

	public static void dumpTestSuite4(IClientConfig config, IInitialTestsRun tests) {
		StringBuilder sb = new StringBuilder();

		sb.append("import org.junit.runner.RunWith;\n");
		sb.append("import org.junit.runners.Suite;\n\n");

		sb.append("@RunWith(Suite.class)\n");
		sb.append("@Suite.SuiteClasses( {\n");

		for (String test : config.getTestClasses()) {
			sb.append("\t");
			sb.append(test);
			sb.append(".class,\n");
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);

		sb.append("})\n");
		sb.append("public class ");
		sb.append(project);
		sb.append("TestSuite {\n}\n");

		saveFile(sb, project + "TestSuite4.java");
	}

	private static void saveFile(StringBuilder sb, String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			LOGGER.error("## Cannot save file " + path + "! ##");
		}
	}

	/**
	 * Get kind of given class.
	 */
	private static ClassKind getClassKind(String name, byte[] bytecode, ICoverage coverage, IEnvironment env) {
		return ClassKind.getKind(name, bytecode, env.getAllSuperclasses(name), !coverage.isCovered(name));
	}
}
