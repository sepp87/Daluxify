package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Company;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Companies {

    public static List<Company> getCompanies(Project project) {
        String result = HttpRequest.get("/3.0/projects/" + project.projectId + "/companies");
        return HttpRequest.deserializeAndGetNextPage(result, Company.class);
    }
}
