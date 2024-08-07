package io.ost.dlx.solutions;

import io.ost.dlx.Config;
import io.ost.dlx.ApiClient;
import io.ost.dlx.App;
import io.ost.dlx.Util;
import io.ost.dlx.api.Projects;
import io.ost.dlx.model.Project;
import io.ost.dlx.api.Tasks;
import io.ost.dlx.model.Nameable;
import io.ost.dlx.model.TaskAttachment;
import io.ost.dlx.model.Task;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Joost
 */
public class TaskAttachmentsDownloader {

    public static void download(ApiClient client) {
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());

        for (Project project : projects) {
            Map<String, Task> tasks = getTasksAsMap(project, client);
            List<TaskAttachment> attachments = Tasks.getTaskAttachments(project, client);
            attachments = filterAttachmentsByAvailableTaskIds(attachments, tasks.keySet());
            Map<String, String> filenames = getUniqueFilenames(attachments);

            String projectDirectory = Config.get().getDoneDirectory() + project.projectName + File.separatorChar;
            if (!attachments.isEmpty()) {
                Util.createDirectory(new File(projectDirectory));
            }

            System.out.println();
            System.out.println(attachments.size() + " Attachments to download for project " + project.projectName);
            int i = 1;
            for (TaskAttachment attachment : attachments) {
                if (i == 10) {
//                break;
                }
                String url = attachment.mediaFile.fileDownload;
                Task task = tasks.get(attachment.taskId);
                String taskDirectory = projectDirectory + task.number + File.separatorChar;
                Util.createDirectory(new File(taskDirectory));
                String targetPath = taskDirectory + filenames.get(url);
                File target = new File(targetPath);
                System.out.println(i + " Downloading " + targetPath);
                client.download(url, target);
                i++;
            }
        }
    }

    private static Map<String, String> getUniqueFilenames(List<TaskAttachment> attachments) {
        Map<String, String> result = new TreeMap<>();
        Set<String> filenames = new HashSet<>();
        for (TaskAttachment attachment : attachments) {
            String name = attachment.mediaFile.name;
            String url = attachment.mediaFile.fileDownload;
            String nameAndTaskIdCombo = name + attachment.taskId;
            if (filenames.contains(nameAndTaskIdCombo)) {
                name = renameFilename(name);
                result.put(url, name);
            } else {
                result.put(url, name);
            }
            filenames.add(nameAndTaskIdCombo);
        }
        return result;
    }

    private static String renameFilename(String name) {
        int index = name.lastIndexOf(".");
        String extension = name.substring(index);
        return name.substring(0, index) + "(2)" + extension;
    }

    public static Map<String, Task> getTasksAsMap(Project project, ApiClient client) {
        Map<String, Task> result = new TreeMap<>();

        List<Task> tasks = Tasks.getTasks(project, client);
        tasks = filterTasksByType(tasks);
        for (Task task : tasks) {
            result.put(task.taskId, task);
        }
        return result;
    }

    private static List<Task> filterTasksByType(List<Task> tasks) {
        List<String> types = Config.get().getTaskTypes();
        if (types.isEmpty()) {
            return tasks;
        }

        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (types.contains(task.type.name)) {
                result.add(task);
            }
        }
        return result;
    }

    private static List<TaskAttachment> filterAttachmentsByAvailableTaskIds(Collection<TaskAttachment> attachments, Collection<String> taskIds) {
        List<TaskAttachment> result = new ArrayList<>();
        for (TaskAttachment attachment : attachments) {
            if (taskIds.contains(attachment.taskId)) {
                result.add(attachment);
            }
        }
        return result;
    }

}
