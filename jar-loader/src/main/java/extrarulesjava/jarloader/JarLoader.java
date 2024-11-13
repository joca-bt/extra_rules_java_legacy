package extrarulesjava.jarloader;

import extrarulesjava.jarloader.launch.Archive.Entry;
import extrarulesjava.jarloader.launch.ExecutableArchiveLauncher;

public class JarLoader extends ExecutableArchiveLauncher {
    public JarLoader() throws Exception {}

    @Override
    protected String getEntryPathPrefix() {
        return "jars/";
    }

    @Override
    protected boolean isIncludedOnClassPath(Entry entry) {
        String name = entry.name();
        return name.startsWith("jars/");
    }

	public static void main(String[] args) throws Exception {
        JarLoader jarLoader = new JarLoader();
        jarLoader.launch(args);
    }
}
