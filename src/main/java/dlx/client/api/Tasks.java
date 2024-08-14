package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.Project;
import dlx.client.model.Task;
import dlx.client.model.TaskAttachment;
import dlx.client.model.TaskChange;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Tasks {

    public static List<Task> getTasks(Project project, ApiClient client) {
        String result = client.get("/5.0/projects/" + project.projectId + "/tasks");
        return client.deserializeAndGetNextPage(result, Task.class);
    }

    public static List<TaskAttachment> getTaskAttachments(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/tasks/attachments");
        return client.deserializeAndGetNextPage(result, TaskAttachment.class);
    }

    public static List<TaskChange> getTaskChanges(Project project, ApiClient client) {
        String result = client.get("/2.1/projects/" + project.projectId + "/tasks/changes");
        return client.deserializeAndGetNextPage(result, TaskChange.class);
    }

}
