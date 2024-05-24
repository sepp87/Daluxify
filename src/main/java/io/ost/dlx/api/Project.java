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
public class Project {

    public String projectId;
    public String projectName;
    public String type;
    public String projectTemplateId;
    public String address;
    public String number;
    public String created;
    public String closed;
    public List<Module> modules;

    public static List<Project> getProjects() {
        String result = HttpRequest.get("/5.0/projects");
        return deserializeResult(result);
    }

    private static List<Project> deserializeResult(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<Project> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next().getAsJsonObject().get("data");
            Project project = gson.fromJson(element, Project.class);
            list.add(project);
        }
//                System.out.println(gson.toJson(gson.fromJson(json, JsonElement.class)));
        return list;
    }

    public class Module {

        public String type;
        public String tier;
        public String restriction;
    }

}
