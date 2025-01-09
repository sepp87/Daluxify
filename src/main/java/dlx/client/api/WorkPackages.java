package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
import dlx.client.model.Project;
import dlx.client.model.Workpackage;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Workpackages {

    public static List<Workpackage> getWorkpackages(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/workpackages");
        return client.deserializeAndGetNextPage(result, Workpackage.class);
    }

    public static AsyncResponse<Workpackage> getWorkpackagesAsync(Project project, ApiClient client) {
        AsyncResponse<Workpackage> result = client.getAsync("/1.0/projects/" + project.projectId + "/workpackages", Workpackage.class);
        return result;
    }
}
