package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.Task;
import io.ost.dlx.model.TaskAttachment;
import io.ost.dlx.model.TaskChange;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Tasks {

    public static List<Task> getTasks(Project project) {
        String result = HttpRequest.get("/5.0/projects/" + project.projectId + "/tasks");
        return HttpRequest.deserializeAndGetNextPage(result, Task.class);
    }

    public static List<TaskAttachment> getTaskAttachments(Project project) {
        String result = HttpRequest.get("/1.0/projects/" + project.projectId + "/tasks/attachments");
        return HttpRequest.deserializeAndGetNextPage(result, TaskAttachment.class);
    }

    public static List<TaskChange> getTaskChanges(Project project) {
        String result = HttpRequest.get("/2.1/projects/" + project.projectId + "/tasks/changes");
        return HttpRequest.deserializeAndGetNextPage(result, TaskChange.class);
    }

}
