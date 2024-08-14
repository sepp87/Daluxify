package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.Project;
import dlx.client.model.WorkPackage;
import java.util.List;

/**
 *
 * @author Joost
 */
public class WorkPackages {
    
    public static List<WorkPackage> getWorkPackages(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/workpackages");
        return client.deserializeAndGetNextPage(result, WorkPackage.class);
    }
}
