package io.ost.dlx.api;

import io.ost.dlx.HttpRequest;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.Form;
import io.ost.dlx.model.FormAttachment;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Forms {

    public static List<Form> getForms(Project project) {
        String result = HttpRequest.get("/2.0/projects/" + project.projectId + "/forms");
        return HttpRequest.deserializeAndGetNextPage(result, Form.class);
    }

    public static List<FormAttachment> getFormAttachments(Project project) {
        String result = HttpRequest.get("/2.0/projects/" + project.projectId + "/forms/attachments");
        return HttpRequest.deserializeAndGetNextPage(result, FormAttachment.class);
    }
}
