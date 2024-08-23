package dlx.client.solutions;

import dlx.client.ApiClient;
import dlx.client.api.Projects;
import dlx.client.model.Project;
import java.util.ArrayList;

/**
 *
 * @author Joost
 */
public class ProjectCreator {

    public static void testCreateProject(ApiClient client) {
        Project project = new Project();
        project.projectName = "API Test Sandbox2";
        project.type = "building";
        project.number = "1234";
        project.modules = new ArrayList<>();
        dlx.client.model.Module m1 = new dlx.client.model.Module();
        m1.type = "field";
        m1.tier = "pro";
        m1.restriction = "sandbox";
        dlx.client.model.Module m2 = new dlx.client.model.Module();
        m2.type = "field";
        m2.tier = "pro";
        m2.restriction = "sandbox";
        project.modules.add(m1);
        project.modules.add(m2);
        Project result = Projects.createProject(project, "jme@dalux.com", client);
    }

}
