package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Folder;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Folders {

    public static List<Folder> getFolders(Project project, FileArea fileArea, ApiClient client) {
        String result = client.get("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/folders");
        return client.deserializeAndGetNextPage(result, Folder.class);
    }
}
