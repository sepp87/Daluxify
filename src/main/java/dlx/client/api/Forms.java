package dlx.client.api;

import dlx.client.ApiClient;
import dlx.client.model.Project;
import dlx.client.model.Form;
import dlx.client.model.FormAttachment;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Forms {

    public static List<Form> getForms(Project project, ApiClient client) {
        String result = client.get("/2.0/projects/" + project.projectId + "/forms");
        return client.deserializeAndGetNextPage(result, Form.class);
    }

    public static List<FormAttachment> getFormAttachments(Project project, ApiClient client) {
        String result = client.get("/2.0/projects/" + project.projectId + "/forms/attachments");
        return client.deserializeAndGetNextPage(result, FormAttachment.class);
    }
}
