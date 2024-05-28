package io.ost.dlx.solutions;

import io.ost.dlx.Config;
import io.ost.dlx.HttpRequest;
import io.ost.dlx.Util;
import io.ost.dlx.api.Attachment;
import io.ost.dlx.api.Project;
import io.ost.dlx.api.Task;
import java.io.File;
import java.util.ArrayList;
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

    public static void download() {
        List<Project> projects = getSelectedProjects();
        for (Project project : projects) {
            Map<String, Task> tasks = getTasksAsMap(project);
            List<Attachment> attachments = Attachment.getAttachments(project);
            Map<String, String> filenames = getUniqueFilenames(attachments);

            String projectDirectory = Config.get().getDoneDirectory() + project.projectName + File.separatorChar;
            Util.createDirectory(new File(projectDirectory));

            System.out.println();
            System.out.println(attachments.size() + " Attachments to download for project " + project.projectName);
            int i = 1;
            for (Attachment attachment : attachments) {
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
                HttpRequest.download(url, target);
                i++;
            }
        }
    }

    private static Map<String, String> getUniqueFilenames(List<Attachment> attachments) {
        Map<String, String> result = new TreeMap<>();
        Set<String> filenames = new HashSet<>();
        for (Attachment attachment : attachments) {
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

    public static List<Project> getSelectedProjects() {
        List<Project> result = new ArrayList<>();
        List<Project> projects = Project.getProjects();
        List<String> toSelect = Config.get().getProjectNames();

        for (Project project : projects) {
            if (toSelect.contains(project.projectName)) {
                result.add(project);
            }
        }
        return result;
    }

    public static Map<String, Task> getTasksAsMap(Project project) {
        Map<String, Task> result = new TreeMap<>();

        List<Task> tasks = Task.getTasks(project);
        for (Task task : tasks) {
            result.put(task.taskId, task);
        }
        return result;
    }
    
}
