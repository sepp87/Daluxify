package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.Company;
import io.ost.dlx.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Companies {

    public static List<Company> getCompanies(Project project, ApiClient client) {
        String result = client.get("/3.0/projects/" + project.projectId + "/companies");
        return client.deserializeAndGetNextPage(result, Company.class);
    }

//    public static Company createCompany(Project project, Company company, ApiClient client) {
//        String result = client.post("/3.0/projects/" + project.projectId + "/companies", "");
//        return client.deserializeObject(result, Company.class);
//    }
//
//    public static Company updateCompany(Project project, Company company, ApiClient client) {
//        String result = client.post("/3.0/projects/" + project.projectId + "/companies/" + company.companyId, "");
//        return client.deserializeObject(result, Company.class);
//    }
}
