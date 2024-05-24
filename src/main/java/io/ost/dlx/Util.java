package io.ost.dlx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public class Util {

    public static String readFileAsString(File file) throws IOException {
        String allLines = "";
        try ( BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines += line;
            }
        }
        return allLines;
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
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fallbackPath;
    }
}
