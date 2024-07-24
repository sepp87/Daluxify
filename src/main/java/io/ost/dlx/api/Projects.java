package io.ost.dlx.api;

import io.ost.dlx.Config;
import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Project;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Projects {

    public static List<Project> getProjects() {
        String result = HttpRequest.get("/5.0/projects");
        return HttpRequest.deserializeAndGetNextPage(result, Project.class);
    }

    public static List<Project> getSelectedProjects() {
        List<Project> result = new ArrayList<>();
        List<Project> projects = Projects.getProjects();
        List<String> toSelect = Config.get().getProjectNames();

        if (toSelect.isEmpty()) {
            return projects;
        }

        for (Project project : projects) {
            if (toSelect.contains(project.projectName)) {
                result.add(project);
            }
        }
        return result;
    }
}
