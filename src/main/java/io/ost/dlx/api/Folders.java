package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Folder;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Folders {

    public static List<Folder> getFolders(Project project, FileArea fileArea) {
        String result = HttpRequest.get("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/folders");
        return HttpRequest.deserializeAndGetNextPage(result, Folder.class);
    }
}
