package extrarulesjava.testrunner;

import java.io.PrintWriter;
import java.nio.file.Path;

import org.junit.platform.console.ConsoleLauncher;
import org.junit.platform.console.options.CommandResult;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestRunner {
    public static void main(String[] args) {
        PrintWriter out = new PrintWriter(System.out, true, UTF_8);
        PrintWriter err = new PrintWriter(System.err, true, UTF_8);

        String testFilter = System.getenv("TESTBRIDGE_TEST_ONLY");
        Path jar = Path.of(args[0]);
        Path testReport = Path.of(System.getenv("XML_OUTPUT_FILE"));

        String[] arguments = Arguments.getArguments(testFilter, jar, testReport);
        CommandResult<?> result = ConsoleLauncher.run(out, err, arguments);
        Reports.generateReport(testReport);

        System.exit(result.getExitCode());
    }
}
