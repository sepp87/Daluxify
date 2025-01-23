package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
import dlx.client.model.Company;
import dlx.client.model.Project;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Companies {

    public static List<Company> getCompanies(Project project, ApiClient client) {
        String result = client.get("/3.1/projects/" + project.projectId + "/companies");
        return client.deserializeAndGetNextPage(result, Company.class);
    }

    public static AsyncResponse<Company> getCompaniesAsync(Project project, ApiClient client) {
        AsyncResponse<Company> result = client.getAsync("/3.1/projects/" + project.projectId + "/companies", Company.class);
        return result;
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
