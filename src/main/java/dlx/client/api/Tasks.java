package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.AsyncResponse;
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

    public static AsyncResponse<Task> getTasksAsync(Project project, ApiClient client) {
        AsyncResponse<Task> result = client.getAsync("/5.0/projects/" + project.projectId + "/tasks", Task.class);
        return result;
    }

    public static List<TaskAttachment> getTaskAttachments(Project project, ApiClient client) {
        String result = client.get("/1.1/projects/" + project.projectId + "/tasks/attachments");
        return client.deserializeAndGetNextPage(result, TaskAttachment.class);
    }

    public static AsyncResponse<TaskAttachment> getTaskAttachmentsAsync(Project project, ApiClient client) {
        AsyncResponse<TaskAttachment> result = client.getAsync("/1.1/projects/" + project.projectId + "/tasks/attachments", TaskAttachment.class);
        return result;
    }

    public static List<TaskChange> getTaskChanges(Project project, ApiClient client) {
        String result = client.get("/2.1/projects/" + project.projectId + "/tasks/changes");
        return client.deserializeAndGetNextPage(result, TaskChange.class);
    }

    public static AsyncResponse<TaskChange> getTaskChangesAsync(Project project, ApiClient client) {
        AsyncResponse<TaskChange> result = client.getAsync("/2.1/projects/" + project.projectId + "/tasks/changes", TaskChange.class);
        return result;
    }

}
