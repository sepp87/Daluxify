package dlx.client.solutions;

import dlx.client.ApiClient;
import dlx.client.api.Companies;
import dlx.client.api.Projects;
import dlx.client.api.Users;
import dlx.client.model.Company;
import dlx.client.model.Project;
import dlx.client.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Joost
 */
public class ProjectUsersExporter {

    public static void print(ApiClient client) {
        // TODO Method not completely implemented due to missing phone numbers

        Project project = Projects.getProjects(client).get(0);
        List<User> users = Users.getUsers(project, client);
        List<Company> companies = Companies.getCompanies(project, client);

        Map<String, Company> companyMap = new TreeMap<>();
        for (Company company : companies) {
            companyMap.put(company.companyId, company);
        }

        List<String[]> rows = new ArrayList<>();

        for (User user : users) {
            Company company = companyMap.get(user.companyId);

            String[] row = new String[6];
            row[0] = user.firstName;
            row[1] = user.lastName;
            row[2] = user.email;

            row[3] = company.address;
            row[4] = company.name;
            row[5] = company.vatNumber;
            rows.add(row);
        }

    }
}
