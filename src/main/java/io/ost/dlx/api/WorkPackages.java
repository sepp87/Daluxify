package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.WorkPackage;
import java.util.List;

/**
 *
 * @author Joost
 */
public class WorkPackages {
    
    public static List<WorkPackage> getWorkPackages(Project project) {
        String result = HttpRequest.get("/1.0/projects/" + project.projectId + "/workpackages");
        return HttpRequest.deserializeAndGetNextPage(result, WorkPackage.class);
    }
}
