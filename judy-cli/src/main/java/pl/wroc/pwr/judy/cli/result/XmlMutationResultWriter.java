package pl.wroc.pwr.judy.cli.result;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.IMutationResultWriter;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.WriterException;
import pl.wroc.pwr.judy.utils.ClassKind;
import pl.wroc.pwr.judy.utils.SourceCodeCache;
import pl.wroc.pwr.judy.utils.SourceCodeCache.SourceCode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Mutation result writer saving it as an XML file.
 * <p/>
 * TODO refactoring of XmlMutationResultWriter needed, use jdom
 *
 * @author pmiwaszko
 */
public class XmlMutationResultWriter implements IMutationResultWriter {

	private final String path;
	private final String xsltPath;
	private final SourceCodeCache cache;
	private static final Logger LOGGER = getLogger(XmlMutationResultWriter.class);

	/**
	 * <code>XmlMutationResultWriter</code> constructor.
	 *
	 * @param path       path to output file
	 * @param xsltPath   path to XSLT file
	 * @param workspace  workspace path
	 * @param sourcepath list of source directories path inside the workspace
	 */
	public XmlMutationResultWriter(String path, String xsltPath, String workspace, List<String> sourcepath) {
		this.path = path;
		this.xsltPath = xsltPath;
		cache = new SourceCodeCache(workspace, sourcepath);
	}

	/**
	 * <code>XmlMutationResultWriter</code> constructor.
	 */
	public XmlMutationResultWriter(String path) {
		this(path, null, null, null);
	}

	/**
	 * <code>XmlMutationResultWriter</code> constructor.
	 */
	public XmlMutationResultWriter(String path, String workspace, List<String> sourcepath) {
		this(path, null, workspace, sourcepath);
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void write(IMutationResult mutationResult) throws WriterException {
		write(mutationResult, false);
	}

	@Override
	public void write(IMutationResult mutationResult, boolean showKilled) throws WriterException {
		if (mutationResult != null) {
			try {
				Writer writer;
				try {
					writer = new PrintWriter(path, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					writer = new PrintWriter(path);
				}
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

				Document doc = documentBuilder.newDocument();
				Element content = createContent(doc, mutationResult, showKilled);
				doc.appendChild(content);
				doc.normalizeDocument();

				// create transformation of prepared document into XML file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				transformer.transform(new DOMSource(doc), new StreamResult(writer));

				// flush & close stream
				writer.flush();
				writer.close();
			} catch (Exception e) {
				throw new WriterException(e);
			}
		}
	}

	/**
	 * Create document content.
	 */
	protected Element createContent(Document doc, IMutationResult mutationResult, boolean showKilled) {
		Statistics statistics = new Statistics();
		Element classes = null;
		if (mutationResult.getResults() != null) {
			classes = createClassesSection(doc, mutationResult, showKilled, statistics);
		}
		Element tests = createTestsSection(doc, mutationResult, statistics);
		Element summary = createSummarySection(doc, mutationResult, statistics);
		Element operators = createOperatorsSection(doc, mutationResult, statistics, showKilled);

		addXsltPath(doc);
		Element root = doc.createElement("result");

		root.appendChild(doc.createComment(" summary "));
		root.appendChild(summary);

		if (mutationResult.getOperators().size() > 0) {
			root.appendChild(doc.createComment(" mutation operators "));
			root.appendChild(operators);
		}

		root.appendChild(doc.createComment(" initial tests "));
		root.appendChild(tests);

		if (classes != null) {
			root.appendChild(doc.createComment(" evaluated classes "));
			root.appendChild(classes);
		}
		return root;
	}

	/**
	 * Create summary section.
	 */
	private Element createSummarySection(Document doc, IMutationResult mutationResult, Statistics statistics) {
		Element section = doc.createElement("summary");

		appendTextElement(doc, section, "date", formatDate(mutationResult.getDate().getTime()));
		appendTextElement(doc, section, "time", formatTime(mutationResult.getDate()));
		appendTextElement(doc, section, "duration", formatDouble(mutationResult.getDuration() / 1000d));
		appendTextElement(doc, section, "testsCount", mutationResult.getTests().size());

		if (mutationResult.getResults() != null) {
			appendTextElement(doc, section, "evaluatedClassesCount", mutationResult.getResults().size());
			appendTextElement(doc, section, "mutatedClassesCount", statistics.getMutatedClassesCount());
			appendTextElement(doc, section, "score", formatDouble(statistics.getMutationScore()));
			appendTextElement(doc, section, "allKilledMutantsCount", statistics.getKilledMutantsCount());
			appendTextElement(doc, section, "allMutantsCount", statistics.getMutantsCount());
			appendTextElement(doc, section, "testsRuns", statistics.getTestsRunsCount());
			appendTextElement(doc, section, "testsDuration", formatDouble(statistics.getTestsDuration() / 1000d));
		}

		return section;
	}

	/**
	 * Create initial tests section.
	 */
	private Element createTestsSection(Document doc, IMutationResult mutationResult, Statistics statistics) {
		Element section = doc.createElement("tests");

		for (ITestResult testResult : mutationResult.getTests()) {
			statistics.addTestResult(testResult);

			Element test = doc.createElement("test");

			appendTextElement(doc, test, "name", testResult.getClassName());
			appendTextElement(doc, test, "duration", testResult.getDuration());
			appendTextElement(doc, test, "result", testResult.passed() ? "passed" : "failed");

			if (testResult.getThrownExceptions().size() > 0) {
				Element exceptions = doc.createElement("exceptions");

				for (Throwable throwable : testResult.getThrownExceptions()) {
					String exception = throwable.getClass().getCanonicalName() + " " + throwable.getMessage();
					appendTextElement(doc, exceptions, "exception", exception);

					LOGGER.debug("Exceptions thrown during initial test run of: " + testResult.getClassName());
					LOGGER.error(testResult.getClassName(), throwable);
				}
				test.appendChild(exceptions);
			}
			section.appendChild(test);
		}
		return section;
	}

	/**
	 * Create summary section.
	 */
	private Element createOperatorsSection(Document doc, IMutationResult mutationResult, Statistics statistics, boolean showKilled) {
		Element section = doc.createElement("operators");

		for (IDescriptable mutationOperator : mutationResult.getOperators()) {
			Element operator = doc.createElement("operator");
			appendTextElement(doc, operator, "name", mutationOperator.getName());
			appendTextElement(doc, operator, "description", mutationOperator.getDescription());
			if (mutationResult.getResults() != null && showKilled) {
				OperatorStatistic statistic = statistics.get(mutationOperator.getName());
				if (statistic != null) {
					appendTextElement(doc, operator, "killedMutantsCount", statistic.getKilledMutantsCount());
					appendTextElement(doc, operator, "mutantsCount", statistic.getMutantsCount());
				}
			}
			section.appendChild(operator);
		}
		return section;
	}

	/**
	 * Create evaluated classes section.
	 */
	private Element createClassesSection(Document doc, IMutationResult mutationResult, boolean showKilled,
										 Statistics statistics) {
		Element section = doc.createElement("classes");

		// TODO: sort class results
		for (IClassMutationResult classMutationResult : mutationResult.getResults()) {
			if (classMutationResult != null) {
				statistics.addClassResult(classMutationResult);

				String className = classMutationResult.getClassName();
				Element clazz = doc.createElement("class");
				appendTextElement(doc, clazz, "name", classMutationResult.getClassName());
				if (classMutationResult.getClassKind() != ClassKind.NORMAL) {
					appendTypeElement(doc, clazz, classMutationResult.getClassKind());
				} else {
					appendTextElement(doc, clazz, "score", formatDouble(classMutationResult.getMutationScore()));
					appendTextElement(doc, clazz, "mutantsCount", classMutationResult.getMutantsCount());
					appendTextElement(doc, clazz, "mutantsKilledCount", classMutationResult.getKilledMutantsCount());

					Element alive = doc.createElement("notKilledMutants");
					Element killed = doc.createElement("killedMutants");

					// killed mutants
					for (IMutant mutant : classMutationResult.getKilledMutants()) {
						statistics.addKilledMutant(mutant);
						if (showKilled) {
							appendKilledMutantElement(doc, killed, mutant, className);
						}
					}
					if (showKilled && !classMutationResult.getKilledMutants().isEmpty()) {
						clazz.appendChild(killed);
					}

					// alive mutants
					// TODO: sort mutants
					for (IMutant mutant : classMutationResult.getAliveMutants()) {
						statistics.addAliveMutant(mutant);
						appendAliveMutantElement(doc, alive, mutant, className);
					}
					if (!classMutationResult.getAliveMutants().isEmpty()) {
						clazz.appendChild(alive);
					}
				}
				section.appendChild(clazz);
			}
		}
		return section;
	}

	private void appendKilledMutantElement(Document doc, Element parent, IMutant mutant, String className) {
		Element element = createMutantElement(doc, mutant, className);
		// TODO add thrown exceptions
		parent.appendChild(element);
	}

	private void appendAliveMutantElement(Document doc, Element parent, IMutant mutant, String className) {
		Element element = createMutantElement(doc, mutant, className);
		parent.appendChild(element);
	}

	private Element createMutantElement(Document doc, IMutant mutant, String className) {
		Element element = doc.createElement("mutant");

		Element operators = doc.createElement("operators");
		for (String opName : mutant.getOperatorsNames()) {
			appendTextElement(doc, operators, "operator", opName);
		}
		element.appendChild(operators);

		Element points = doc.createElement("points");
		for (int point : mutant.getMutionPointsIndexes()) {
			appendTextElement(doc, points, "point", point);
		}
		element.appendChild(points);

		Element indexes = doc.createElement("indexes");
		for (int index : mutant.getMutationIndexes()) {
			appendTextElement(doc, indexes, "indexes", index);
		}
		element.appendChild(indexes);

		Element lines = doc.createElement("lines");
		for (int line : mutant.getLinesNumbers()) {
			if (line > 0) {
				appendTextElement(doc, lines, "line", line);
			} else {
				appendTextElement(doc, lines, "line", "unknown");
			}
		}
		element.appendChild(lines);

		if (mutant.getDescription() != null) {
			appendTextElement(doc, element, "details", mutant.getDescription());
		}

		// appendSourceCodeElement(doc, element, mutant.getLinesNumbers(),
		// className);
		return element;
	}

	/**
	 * Specify the processing instruction for an XSLT transformation.
	 */
	private void addXsltPath(Document doc) {
		if (xsltPath != null) {
			doc.appendChild(doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"" + xsltPath
					+ "\""));
		}
	}

	private String formatDate(Long date) {
		return DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(date));
	}

	private String formatTime(Date date) {
		return DateFormat.getTimeInstance(DateFormat.MEDIUM).format(date);
	}

	private String formatDouble(double value) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		return format.format(value);
	}

	private void appendTypeElement(Document doc, Element parent, ClassKind classKind) {
		Element eType = doc.createElement("type");
		switch (classKind) {
			case INTERFACE:
				eType.setTextContent("Interface");
				break;
			case ABSTRACT:
				eType.setTextContent("Abstract class");
				break;
			case APPLET:
				eType.setTextContent("Applet");
				break;
			case GUI:
				eType.setTextContent("GUI class");
				break;
			case NO_TESTS:
				eType.setTextContent("Class not covered by tests");
				break;
			default:
				eType.setTextContent("Unknown");
				break;
		}
		parent.appendChild(eType);
	}

	private void appendSourceCodeElement(Document doc, Element parent, List<Integer> lines, String className) {
		for (Integer lineNumber : lines) {
			SourceCode sourceCode = cache.get(className, lineNumber);
			if (sourceCode != null && sourceCode.getLines().size() > 0) {
				Element eSourceCode = doc.createElement("source");
				// Element newPath = doc.createElement("path");
				// try {
				// newPath.appendChild(doc.createCDATASection(sourceCode.getFile().toURI().toURL().toString()));
				// } catch (MalformedURLException e) {
				// newPath.setTextContent("unknown");
				// }
				// eSourceCode.appendChild(newPath);

				if (lineNumber > 0) {
					for (SourceCodeCache.Line line : sourceCode.getLines()) {
						Element l = doc.createElement("ln");
						l.setAttribute("nr", Integer.toString(line.getNumber()));
						l.appendChild(doc.createCDATASection(line.getContent()));
						eSourceCode.appendChild(l);
					}
				}
				parent.appendChild(eSourceCode);
			}
		}
	}

	private void appendTextElement(Document doc, Element parent, String name, String value) {
		Element element = doc.createElement(name);
		element.setTextContent(value);
		parent.appendChild(element);
	}

	private void appendTextElement(Document doc, Element parent, String name, long value) {
		appendTextElement(doc, parent, name, String.valueOf(value));
	}

	private static class Statistics {

		private int testsRuns;
		private long testsDuration;
		private int mutants;
		private int killed;
		private int mutated;
		private final Map<String, OperatorStatistic> statistics = new HashMap<>();

		public void addClassResult(IClassMutationResult result) {
			testsDuration += result.getStatistic().getTestsDuration();
			testsRuns += result.getStatistic().getTestMethods();
			killed += result.getKilledMutantsCount();
			mutants += result.getMutantsCount();
			if (result.getClassKind() == ClassKind.NORMAL) {
				mutated++;
			}
		}

		public void addTestResult(ITestResult result) {
			testsDuration += result.getDuration();
			testsRuns++;
		}

		public void addAliveMutant(IMutant mutant) {
			for (String operatorName : mutant.getOperatorsNames()) {
				OperatorStatistic statistic = statistics.get(operatorName);
				if (statistic == null) {
					statistic = new OperatorStatistic();
					statistics.put(operatorName, statistic);
				}
				statistic.addAliveMutant();
			}
		}

		public void addKilledMutant(IMutant mutant) {
			for (String operatorName : mutant.getOperatorsNames()) {
				OperatorStatistic statistic = statistics.get(operatorName);
				if (statistic == null) {
					statistic = new OperatorStatistic();
					statistics.put(operatorName, statistic);
				}
				statistic.addKilledMutant();
			}
		}

		public int getMutatedClassesCount() {
			return mutated;
		}

		public double getMutationScore() {
			return (double) killed / (double) mutants;
		}

		public long getTestsDuration() {
			return testsDuration;
		}

		public int getTestsRunsCount() {
			return testsRuns;
		}

		public OperatorStatistic get(String name) {
			return statistics.get(name);
		}

		public int getMutantsCount() {
			return mutants;
		}

		public int getKilledMutantsCount() {
			return killed;
		}

	}

	private static class OperatorStatistic {

		private int mutants;
		private int killed;

		public int getKilledMutantsCount() {
			return killed;
		}

		public void addKilledMutant() {
			mutants++;
			killed++;
		}

		public void addAliveMutant() {
			mutants++;
		}

		public int getMutantsCount() {
			return mutants;
		}
	}

}
