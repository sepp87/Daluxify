package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.File;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.VersionSet;
import java.util.List;

/**
 *
 * @author Joost
 */
public class VersionSets {

    public static List<VersionSet> getVersionSets(Project project, ApiClient client) {
        String result = client.get("/2.0/projects/" + project.projectId + "/version_sets");
        return client.deserializeAndGetNextPage(result, VersionSet.class);
    }

    public static List<VersionSet> getVersionSets(Project project, FileArea fileArea, ApiClient client) {
        String result = client.get("/2.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/version_sets");
        return client.deserializeAndGetNextPage(result, VersionSet.class);
    }

    public static List<File> getVersionSetFiles(Project project, FileArea fileArea, ApiClient client) {
        String result = client.get("/2.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/files");
        return client.deserializeAndGetNextPage(result, File.class);
    }
}
