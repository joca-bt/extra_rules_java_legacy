package extrarulesjava.testrunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Arguments {
    public static String[] process(String[] args) {
        List<String> arguments = new ArrayList<>();

        arguments.add("execute");

        addDetails(arguments);
        addDisableBanner(arguments);
        addFailIfNoTests(arguments);
        addReportsDir(arguments);
        addSelectors(arguments, args[0]);

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
        Path dir = Path.of(System.getenv("XML_OUTPUT_FILE")).getParent();
        arguments.add("--reports-dir=%s".formatted(dir));
    }

    /**
     * Adds selectors based on the filter expression when the --test_filter option is provided.
     *
     * The --test_filter option specifies which tests to run. This option is provided to the test
     * runner through the TESTBRIDGE_TEST_ONLY environment variable.
     *
     * Limitations:
     *   - Does not support parameterized tests.
     *   - Does not support nested tests.
     *   - Assumes that the filter expression is valid.
     */
    private static void addSelectors(List<String> arguments, String jar) {
        String filterExpression = System.getenv("TESTBRIDGE_TEST_ONLY");

        if (filterExpression == null) {
            arguments.add("--scan-classpath=%s".formatted(jar));
            return;
        }

        filterExpression = adjustForIntellijIdea(filterExpression);

        for (var component : filterExpression.split("\\|(?![^()]+\\))")) {
            String[] names = component.split("#");

            String clazz = names[0];

            if (names.length == 1) {
                arguments.add("--select-class=%s".formatted(clazz));
                continue;
            }

            for (var method : names[1].replaceAll("^\\(|\\)$", "").split("\\|")) {
                arguments.add("--select-method=%s#%s".formatted(clazz, method));
            }
        }
    }

    private static String adjustForIntellijIdea(String filterExpression) {
        return filterExpression.replaceAll("[#$](\\||$)", "$1");
    }
}
