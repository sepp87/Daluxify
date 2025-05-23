package dlx.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public class Utils {

    public static String readFileAsString(File file) throws IOException {
        String result = "";
        try {
            Path filePath = file.toPath();
            result = Files.readString(filePath);
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    public static List<String> readFileAsStringList(File file) throws IOException {
        List<String> list = new ArrayList<>();
        try ( BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    /**
     *
     * @param any
     * @param fallbackPath
     * @return the app root directory if any object is inside a .jar file
     */
    public static String getAppRootDirectory(Object any, String fallbackPath) {
        try {
            URI uri = any.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            String path = new File(uri).getAbsolutePath();
            String targetSeperatorCharClasses = "target" + File.separatorChar + "classes";
            if (path.endsWith(targetSeperatorCharClasses)) {
                fallbackPath = path.substring(0, path.length() - targetSeperatorCharClasses.length()) + fallbackPath;
            }
            return path.endsWith(".jar") ? path.substring(0, path.lastIndexOf(File.separatorChar) + 1) : fallbackPath;
        } catch (URISyntaxException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fallbackPath;
    }

    public static File createFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    public static File createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
