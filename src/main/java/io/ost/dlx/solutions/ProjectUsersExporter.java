package io.ost.dlx.solutions;

import io.ost.dlx.api.Company;
import io.ost.dlx.api.Project;
import io.ost.dlx.api.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Joost
 */
public class ProjectUsersExporter {

    public static void print() {
        // TODO Method not completely implemented due to missing phone numbers
        
        Project project = Project.getProjects().get(0);
        List<User> users = User.getUsers(project);
        List<Company> companies = Company.getCompanies(project);

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
