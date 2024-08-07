package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class FileAreas {

    public static List<FileArea> getFileAreas(Project project, ApiClient client) {
        String result = client.get("/5.0/projects/" + project.projectId + "/file_areas");
        return client.deserializeAndGetNextPage(result, FileArea.class);
    }

}
