package extrarulesjava.testrunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Arguments {
    private static final String TESTBRIDGE_TEST_ONLY = "TESTBRIDGE_TEST_ONLY";
    private static final String XML_OUTPUT_FILE = "XML_OUTPUT_FILE";

    public static String[] process(String[] args) {
        List<String> arguments = new ArrayList<>();

        addDetails(arguments);
        addDisableBanner(arguments);
        addFailIfNoTests(arguments);
        addReportsDir(arguments);
        addSelector(arguments, args[0]);

        return arguments.toArray(new String[arguments.size()]);
    }

    private static void addDetails(List<String> arguments) {
        arguments.add("--details=none");
    }

    private static void addDisableBanner(List<String> arguments) {
        arguments.add("--disable-banner");
    }

    private static void addFailIfNoTests(List<String> arguments) {
        arguments.add("--fail-if-no-tests");
    }

    private static void addReportsDir(List<String> arguments) {
        Path file = Path.of(System.getenv(XML_OUTPUT_FILE));
        arguments.add("--reports-dir=" + file.getParent());
    }

    /*
     * Bazel's --test_filter option specifies which tests to run. This option is provided to the
     * test runner through the environment variable TESTBRIDGE_TEST_ONLY.
     *
     * Add a selector based on the filter expression when --test_filter is provided. Supported
     * filter expressions:
     *   - package,
     *   - package.class,
     *   - package.class#method.
     */
    private static void addSelector(List<String> arguments, String jar) {
        String filterExpression = System.getenv(TESTBRIDGE_TEST_ONLY);

        if (filterExpression != null) {
            if (filterExpression.contains("#")) {
                arguments.add("--select-method=" + filterExpression);
            } else if (isClass(filterExpression)) {
                arguments.add("--select-class=" + filterExpression);
            } else {
                arguments.add("--select-package=" + filterExpression);
            }
        } else {
            arguments.add("--scan-classpath=" + jar);
        }
    }

    private static boolean isClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
