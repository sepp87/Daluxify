package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.User;
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
