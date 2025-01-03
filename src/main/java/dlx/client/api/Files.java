package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
import dlx.client.model.File;
import dlx.client.model.FileArea;
import dlx.client.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Files {

    public static List<File> getFiles(Project project, FileArea fileArea, ApiClient client) {
        String result = client.get("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/files");
        return client.deserializeAndGetNextPage(result, File.class);
    }

    public static AsyncResponse<File> getFilesAsync(Project project, FileArea fileArea, ApiClient client) {
        AsyncResponse<File> result = client.getAsync("/5.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/files", File.class);
        return result;
    }

}
