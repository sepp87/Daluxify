package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.Project;
import dlx.client.model.User;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Users {

    public static List<User> getUsers(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/users");
        return client.deserializeAndGetNextPage(result, User.class);
    }

}
