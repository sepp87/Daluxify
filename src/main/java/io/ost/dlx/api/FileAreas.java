package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class FileAreas {

    public static List<FileArea> getFileAreas(Project project) {
        String result = HttpRequest.get("/5.0/projects/" + project.projectId + "/file_areas");
        return HttpRequest.deserializeAndGetNextPage(result, FileArea.class);
    }

}
