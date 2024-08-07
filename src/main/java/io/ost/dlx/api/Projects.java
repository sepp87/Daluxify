package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.ProjectMetadata;
import io.ost.dlx.model.ProjectMetadata.ProjectMetadataValue;
import io.ost.dlx.model.ProjectMetadataMapping;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Projects {

    public static List<Project> getProjects(ApiClient client) {
        String result = client.get("/5.0/projects");
        return client.deserializeAndGetNextPage(result, Project.class);
    }

//    public static Project createProject(Project project, ApiClient client) {
//        String result = client.post("/5.0/projects/", "");
//        return client.deserializeObject(result, Project.class);
//    }
//
//    public static Project updateProject(Project project, ApiClient client) {
//        String result = client.patch("/5.0/projects/" + project.projectId, "");
//        return client.deserializeObject(result, Project.class);
//    }

    public static List<ProjectMetadata> getProjectMetadata(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/metadata");
        return client.deserializeAndGetNextPage(result, ProjectMetadata.class);
    }

    public static List<ProjectMetadataMapping> getProjectMetadataToCreate(ApiClient client) {
        String result = client.get("/1.0/projects/metadata/1.0/mappings");
        return client.deserializeAndGetNextPage(result, ProjectMetadataMapping.class);

    }

    public static List<ProjectMetadataValue> getProjectMetadataValuesToCreate(ProjectMetadataMapping mapping, ApiClient client) {
        String result = client.get("/1.0/projects/metadata/1.0/mappings/" + mapping.key + "/values");
        return client.deserializeAndGetNextPage(result, ProjectMetadataValue.class);

    }

    public static List<ProjectMetadataMapping> getProjectMetadataToUpdate(Project project, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "metadata/1.0/mappings");
        return client.deserializeAndGetNextPage(result, ProjectMetadataMapping.class);

    }

    public static List<ProjectMetadataValue> getProjectMetadataValuesToUpdate(Project project, ProjectMetadataMapping mapping, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "metadata/1.0/mappings/" + mapping.key + "/values");
        return client.deserializeAndGetNextPage(result, ProjectMetadataValue.class);
    }
}
