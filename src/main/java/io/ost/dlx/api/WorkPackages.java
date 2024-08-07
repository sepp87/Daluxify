package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.WorkPackage;
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
