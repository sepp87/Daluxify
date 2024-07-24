package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.User;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Users {

    public static List<User> getUsers(Project project) {
        String result = HttpRequest.get("/1.0/projects/" + project.projectId + "/users");
        return HttpRequest.deserializeAndGetNextPage(result, User.class);
    }

}
