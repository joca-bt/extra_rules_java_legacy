package extrarulesjava.loader;

import extrarulesjava.loader.archive.Archive.Entry;

public class Loader extends ExecutableArchiveLauncher {
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
        Loader loader = new Loader();
        loader.launch(args);
    }
}
