package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.File;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Files {

    public static List<File> getFiles(Project project, FileArea fileArea) {
        String result = HttpRequest.get("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/files");
        return HttpRequest.deserializeAndGetNextPage(result, File.class);
    }

}
