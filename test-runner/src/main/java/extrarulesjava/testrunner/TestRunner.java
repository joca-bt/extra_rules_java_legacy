package extrarulesjava.testrunner;

import java.io.PrintWriter;

import org.junit.platform.console.ConsoleLauncher;
import org.junit.platform.console.options.CommandResult;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestRunner {
    public static void main(String[] args) {
        PrintWriter out = new PrintWriter(System.out, true, UTF_8);
        PrintWriter err = new PrintWriter(System.err, true, UTF_8);

        args = Arguments.process(args);

        CommandResult<?> result = ConsoleLauncher.run(out, err, args);

        Reports.process();

        System.exit(result.getExitCode());
    }
}
