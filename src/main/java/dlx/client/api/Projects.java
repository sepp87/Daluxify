package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.Project;
import dlx.client.model.ProjectMetadata;
import dlx.client.model.ProjectMetadataValue;
import dlx.client.model.ProjectMetadataMapping;
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

    public static Project createProject(Project project, String adminEmail, ApiClient client) {
        String body = ApiClient.GSON.toJson(project);

        String result = client.post("/5.0/projects?adminEmail=" + adminEmail, body);
        return client.deserializeObject(result, Project.class);
    }

    public static Project updateProject(Project project, ApiClient client) {
        String body = ApiClient.GSON.toJson(project);

        String result = client.patch("/5.0/projects/" + project.projectId, body);
        return client.deserializeObject(result, Project.class);
    }

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
        String result = client.get("/1.0/projects/" + project.projectId + "/metadata/1.0/mappings");
        return client.deserializeAndGetNextPage(result, ProjectMetadataMapping.class);

    }

    public static List<ProjectMetadataValue> getProjectMetadataValuesToUpdate(Project project, ProjectMetadataMapping mapping, ApiClient client) {
        String result = client.get("/1.0/projects/" + project.projectId + "/metadata/1.0/mappings/" + mapping.key + "/values");
        return client.deserializeAndGetNextPage(result, ProjectMetadataValue.class);
    }
}
