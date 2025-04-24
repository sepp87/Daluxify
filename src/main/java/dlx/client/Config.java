package dlx.client;

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

    public static final boolean LOG_RESPONSE = false;
    public static final boolean LOG_RESPONSE_PRETTY = false;
    public static final boolean LOG_ERRORS = true;
    public static final boolean LOG_EXCEPTIONS = true;
    public static final boolean LOG_PROGRESS = true;

    public static final int API_MAX_TRIES = 3;
    public static final int API_TIMEOUT_LIMIT = 10;
//    public static final int API_MAX_TRIES = 3;
//    public static final int API_TIMEOUT_LIMIT = 6;

    private static final String CONFIG_DIRECTORY = "config" + File.separatorChar;
    private static final String BUILD_DIRECTORY = "build" + File.separatorChar;
    private static final String DONE_DIRECTORY = "done" + File.separatorChar;
    private static final String API_KEY_FILE = CONFIG_DIRECTORY + "api-key.txt";
    private static final String BASE_URL_FILE = CONFIG_DIRECTORY + "base-url.txt";
    private static final String PROJECT_NAMES_FILE = CONFIG_DIRECTORY + "project-names.txt";
    private static final String TASK_TYPES_FILE = CONFIG_DIRECTORY + "task-types.txt";
    private static final String FILE_AREAS_FILE = CONFIG_DIRECTORY + "file-areas.txt";

    private String appRootDirectory;
    private String baseUrl;
    private String apiKey;
    private List<String> projectNames;
    private List<String> taskTypes;
    private List<String> fileAreas;

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
        config.appRootDirectory = Utils.getAppRootDirectory(config, BUILD_DIRECTORY);

        createFilesAndDirectories();

        try {
            config.baseUrl = Utils.readFileAsString(new File(config.appRootDirectory + BASE_URL_FILE));
            config.apiKey = Utils.readFileAsString(new File(config.appRootDirectory + API_KEY_FILE));
            config.projectNames = Utils.readFileAsStringList(new File(config.appRootDirectory + PROJECT_NAMES_FILE));
            config.taskTypes = Utils.readFileAsStringList(new File(config.appRootDirectory + TASK_TYPES_FILE));
            config.fileAreas = Utils.readFileAsStringList(new File(config.appRootDirectory + FILE_AREAS_FILE));

        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            config = null;
        }
    }

    private static void createFilesAndDirectories() {
        Utils.createDirectory(new File(config.appRootDirectory + DONE_DIRECTORY));
        Utils.createDirectory(new File(config.appRootDirectory + CONFIG_DIRECTORY));
        Utils.createFile(new File(config.appRootDirectory + BASE_URL_FILE));
        Utils.createFile(new File(config.appRootDirectory + API_KEY_FILE));
        Utils.createFile(new File(config.appRootDirectory + PROJECT_NAMES_FILE));
        Utils.createFile(new File(config.appRootDirectory + TASK_TYPES_FILE));
        Utils.createFile(new File(config.appRootDirectory + FILE_AREAS_FILE));
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

    public List<String> getTaskTypes() {
        return taskTypes;
    }

}
