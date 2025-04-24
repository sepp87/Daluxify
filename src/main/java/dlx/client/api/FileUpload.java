package dlx.client.api;

import com.google.gson.JsonParser;
import dlx.client.ApiClient;
import dlx.client.model.FileArea;
import dlx.client.model.Project;

/**
 *
 * @author Joost
 */
public class FileUpload {

    public static String createUpload(Project project, FileArea fileArea, ApiClient client) {
        String result = client.post("/1.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/upload", ".");
        String uploadGuid = JsonParser.parseString(result).getAsJsonObject().get("data").getAsJsonObject().get("uploadGuid").getAsString();
        return uploadGuid;
    }

    public static boolean uploadFile(Project project, FileArea fileArea, String uploadGuid, java.io.File file, ApiClient client) {
        return client.upload("/1.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/upload/" + uploadGuid, file, 5242880);
    }

    public static String finishUpload(Project project, FileArea fileArea, String uploadGuid, dlx.client.model.File file, ApiClient client) {
        String body = ApiClient.GSON.toJson(file, dlx.client.model.File.class);
        String result = client.post("/2.0/projects/" + project.projectId + "/file_areas/" + fileArea.fileAreaId + "/upload/" + uploadGuid + "/finalize", body);
        return result;
    }

}
