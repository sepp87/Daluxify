package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.FileArea;
import dlx.client.model.Project;
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
