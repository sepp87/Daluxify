package io.ost.dlx.api;

import io.ost.dlx.ApiClient;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.Form;
import io.ost.dlx.model.FormAttachment;
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
