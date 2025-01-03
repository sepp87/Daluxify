package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
import dlx.client.model.FileArea;
import dlx.client.model.Folder;
import dlx.client.model.Project;
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

    public static AsyncResponse<Folder> getFoldersAsync(Project project, FileArea fileArea, ApiClient client) {
        AsyncResponse<Folder> result = client.getAsync("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/folders", Folder.class);
        return result;
    }
}
