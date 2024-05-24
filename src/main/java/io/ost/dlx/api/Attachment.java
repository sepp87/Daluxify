package io.ost.dlx.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.ost.dlx.HttpRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Joost
 */
public class Attachment {

    public String taskId;
    public MediaFile mediaFile;

    public static List<Attachment> getAttachments(Project project) {
        String result = HttpRequest.get("/1.0/projects/" + project.projectId + "/tasks/attachments");
        return deserializeAndGetNextPage(result);
    }

    private static List<Attachment> deserializeAndGetNextPage(String json) {
        List<Attachment> list = deserialize(json);
        List<Attachment> next = getNextPage(json);
        list.addAll(next);
        return list;
    }

    private static List<Attachment> deserialize(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<Attachment> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next().getAsJsonObject();
            Attachment attachment = gson.fromJson(element, Attachment.class);
            list.add(attachment);
//            System.out.println(gson.toJson(attachment));
        }
        return list;
    }

    private static List<Attachment> getNextPage(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<Attachment> list = new ArrayList<>();
        String link = getNextPageLink(json);
        if (link != null) {
            String result = HttpRequest.get(link);
            list.addAll(deserializeAndGetNextPage(result));
        }
        return list;
    }

    private static String getNextPageLink(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray links = gson.fromJson(json, JsonObject.class).getAsJsonArray("links");
        Iterator<JsonElement> iterator = links.iterator();

        String self = "";
        String nextPage = "";

        while (iterator.hasNext()) {
            JsonObject link = iterator.next().getAsJsonObject();
            String rel = link.get("rel").getAsString();
            if (rel.equals("self")) {
                self = link.get("href").getAsString();
            } else if (rel.equals("nextPage")) {
                nextPage = link.get("href").getAsString();
            }
        }

        if (nextPage.equals("") || nextPage.equals(self)) {
            return null;
        }
        return nextPage;
    }

    public class MediaFile {

        public String name;
        public String fileDownload;
    }

}
