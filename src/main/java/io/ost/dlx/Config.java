package io.ost.dlx;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public class Config {

    private static Config config;

    private String appRootDirectory;
    private String baseUrl;
    private String apiKey;
    private List<String> projectNames;

    private static final String CONFIG_DIRECTORY = "config" + File.separatorChar;
    private static final String BUILD_DIRECTORY = "build" + File.separatorChar;
    private static final String DONE_DIRECTORY = "done" + File.separatorChar;
    private static final String API_KEY_FILE = CONFIG_DIRECTORY + "api-key.txt";
    private static final String BASE_URL_FILE = CONFIG_DIRECTORY + "base-url.txt";
    private static final String PROJECT_NAMES_FILE = CONFIG_DIRECTORY + "project-names.txt";

    private Config() {
    }

    public static Config get() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    private static void loadConfig() {
        config = new Config();
        config.appRootDirectory = Util.getAppRootDirectory(config, BUILD_DIRECTORY);

        createFilesAndDirectories();

        try {
            config.baseUrl = Util.readFileAsString(new File(config.appRootDirectory + BASE_URL_FILE));
            config.apiKey = Util.readFileAsString(new File(config.appRootDirectory + API_KEY_FILE));
            config.projectNames = Util.readFileAsStringList(new File(config.appRootDirectory + PROJECT_NAMES_FILE));

        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            config = null;
        }
    }

    private static void createFilesAndDirectories() {
        createFile(new File(config.appRootDirectory + CONFIG_DIRECTORY));
        createFile(new File(config.appRootDirectory + BASE_URL_FILE));
        createFile(new File(config.appRootDirectory + API_KEY_FILE));
        createFile(new File(config.appRootDirectory + PROJECT_NAMES_FILE));
        createFile(new File(config.appRootDirectory + DONE_DIRECTORY));
    }

    private static void createFile(File file) {
        if (!file.exists()) {
            try {
                if (file.isDirectory()) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                }
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getAppRootDirectory() {
        return appRootDirectory;
    }

    public String getDoneDirectory() {
        return appRootDirectory + DONE_DIRECTORY;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<String> getProjectNames() {
        return projectNames;
    }

}
