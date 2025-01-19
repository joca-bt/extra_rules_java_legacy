package extrarulesjava.testrunner;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.platform.commons.JUnitException;

class Arguments {
    public static String[] getArguments(Path jar, String filter, Path report) {
        List<String> arguments = new ArrayList<>();

        arguments.add("execute");
        arguments.add("--details=none");
        arguments.add("--disable-banner");
        arguments.add("--fail-if-no-tests");
        arguments.add("--reports-dir=%s".formatted(report.getParent()));
        addSelectors(arguments, jar, filter);

        return arguments.toArray(new String[0]);
    }

    /**
     * Adds selectors based on the --test_filter option.
     *
     * The --test_filter option specifies which tests to run. This option is provided to the test
     * runner through the TESTBRIDGE_TEST_ONLY environment variable.
     */
    private static void addSelectors(List<String> arguments, Path jar, String filter) {
        if (filter == null) {
            arguments.add("--scan-classpath=%s".formatted(jar));
            return;
        }

        // Normalize.
        filter = filter.replaceAll("\\$(\\||$)", "$1");

        for (var component : filter.split("\\|(?![^()]+\\))")) {
            String[] names = component.split("#");

            Class<?> clazz = findClass(names[0]);

            if (names.length == 1) {
                arguments.add("--select-class=%s".formatted(clazz.getName()));
                continue;
            }

            for (var spec : names[1].split("\\|")) {
                // Normalize.
                spec = spec.replaceAll("^\\(|\\)$|\\\\|(?<=]).+", "");

                String[] tokens = spec.split("(?=\\[)");

                Method method = findMethod(clazz, tokens[0]);
                String parameters = Arrays.stream(method.getParameterTypes())
                    .map(Class::getName)
                    .collect(Collectors.joining(","));

                if (tokens.length == 1) {
                    arguments.add("--select-method=%s#%s(%s)".formatted(clazz.getName(), method.getName(), parameters));
                    continue;
                }

                String iterations = Pattern.compile("\\d+")
                    .matcher(tokens[1])
                    .replaceAll(match -> Integer.toString(Integer.parseInt(match.group()) - 1));

                arguments.add("--select-iteration=method:%s#%s(%s)%s".formatted(clazz.getName(), method.getName(), parameters, iterations));
            }
        }
    }

    private static Class<?> findClass(String className) {
        while (true) {
            try {
                return Class.forName(className);
            } catch (Exception exception) {
                int i = className.lastIndexOf('.');

                if (i == -1) {
                    String message = "Could not find class %s.".formatted(className.replace('$', '.'));
                    throw new JUnitException(message);
                }

                className = "%s$%s".formatted(className.substring(0, i), className.substring(i + 1));
            }
        }
    }

    private static Method findMethod(Class<?> clazz, String methodName) {
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.getName().equals(methodName))
            .toList();

        if (methods.size() != 1) {
            String message = "Could not find method %s#%s.".formatted(clazz.getName().replace('$', '.'), methodName);
            throw new JUnitException(message);
        }

        return methods.getFirst();
    }
}
