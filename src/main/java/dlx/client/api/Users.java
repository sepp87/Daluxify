package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
import dlx.client.model.Project;
import dlx.client.model.User;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Users {

    public static List<User> getUsers(Project project, ApiClient client) {
        String result = client.get("/1.1/projects/" + project.projectId + "/users");
        return client.deserializeAndGetNextPage(result, User.class);
    }

    public static AsyncResponse<User> getUsersAsync(Project project, ApiClient client) {
        AsyncResponse<User> result = client.getAsync("/1.1/projects/" + project.projectId + "/users", User.class);
        return result;
    }

    public static User getUser(String userId, ApiClient client) {
        String result = client.get("/1.0/users/" + userId);
        return client.deserializeObject(result, User.class);
    }
    
}
