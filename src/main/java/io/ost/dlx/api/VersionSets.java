package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
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

    public static List<VersionSet> getVersionSets(Project project) {
        String result = HttpRequest.get("/2.0/projects/" + project.projectId + "/version_sets");
        return HttpRequest.deserializeAndGetNextPage(result, VersionSet.class);
    }

    public static List<VersionSet> getVersionSets(Project project, FileArea fileArea) {
        String result = HttpRequest.get("/2.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/version_sets");
        return HttpRequest.deserializeAndGetNextPage(result, VersionSet.class);
    }

    public static List<File> getVersionSetFiles(Project project, FileArea fileArea) {
        String result = HttpRequest.get("/2.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/files");
        return HttpRequest.deserializeAndGetNextPage(result, File.class);
    }
}
