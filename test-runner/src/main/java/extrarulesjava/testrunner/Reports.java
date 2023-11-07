package extrarulesjava.testrunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.platform.commons.JUnitException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static javax.xml.transform.OutputKeys.INDENT;

class Reports {
    private static final String XML_OUTPUT_FILE = "XML_OUTPUT_FILE";

    public static void process() {
        try {
            Path file = Path.of(System.getenv(XML_OUTPUT_FILE));
            Document document = parseAndMergeReports(file.getParent());
            writeReport(file, document);
        } catch (Exception exception) {
            String message = "Error processing reports.";
            throw new JUnitException(message, exception);
        }
    }

    /*
     * Add the display name to the name, but only when it starts with [, otherwise IntelliJ IDEA's
     * navigation will break. Remove parentheses and everything after from the name for the same
     * reason.
     */
    private static void fixTestcaseName(Element testcase) {
        String name = testcase.getAttribute("name");
        String displayName = getTestcaseDisplayName(testcase);

        name = name.substring(0, name.indexOf('('));

        if (!name.equals(displayName) && displayName.startsWith("[")) {
            name = "%s %s".formatted(name, displayName);
        }

        testcase.setAttribute("name", name);
    }

    /*
     * <system-out><![CDATA[
     * unique-id: ...
     * display-name: ...
     * ]]></system-out>
     */
    private static String getTestcaseDisplayName(Element testcase) {
        Element systemOut = (Element) testcase.getElementsByTagName("system-out").item(0);
        String[] properties = systemOut.getFirstChild().getNodeValue().trim().split("\n", -1);
        return properties[1].substring(14);
    }

    private static Document parseAndMergeReports(Path dir) throws Exception {
        List<Path> files;

        try (var stream = Files.list(dir)) {
            files = stream.filter(file -> file.getFileName().toString().matches("^TEST-junit-.+\\.xml$"))
                .toList();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().newDocument();
        Element root = document.createElement("testsuites");

        document.appendChild(root);

        for (var file : files) {
            Document subdocument = factory.newDocumentBuilder().parse(file.toFile());
            NodeList testsuites = subdocument.getElementsByTagName("testsuite");

            for (int i = 0, sizei = testsuites.getLength(); i < sizei; i++) {
                Element testsuite = (Element) testsuites.item(i);
                NodeList testcases = testsuite.getElementsByTagName("testcase");

                for (int j = 0, sizej = testcases.getLength(); j < sizej; j++) {
                    Element testcase = (Element) testcases.item(j);
                    fixTestcaseName(testcase);
                }

                root.appendChild(document.adoptNode(testsuite));
            }
        }

        return document;
    }

    private static void writeReport(Path file, Document document) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file.toFile());

        transformer.setOutputProperty(INDENT, "yes");
        transformer.transform(source, result);
    }
}
