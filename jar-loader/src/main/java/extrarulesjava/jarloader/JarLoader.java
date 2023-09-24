package extrarulesjava.jarloader;

import extrarulesjava.jarloader.archive.Archive.Entry;

public class JarLoader extends ExecutableArchiveLauncher {
    private static final String APP_DIR = "app/";
    private static final String LIB_DIR = "lib/";

    @Override
    protected boolean isNestedArchive(Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals(APP_DIR);
        }

        return entry.getName().startsWith(LIB_DIR);
    }

    @Override
    protected boolean isPostProcessingClassPathArchives() {
        return false;
    }

    public static void main(String[] args) throws Exception {
        JarLoader loader = new JarLoader();
        loader.launch(args);
    }
}
