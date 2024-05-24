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
public class User {

    public String userId;
    public String email;
    public String firstName;
    public String lastName;
    public String companyId;

    public static List<User> getUsers(Project project) {
        String result = HttpRequest.get("/1.0/projects/" + project.projectId + "/users");
        return deserialize(result);
    }

    public static List<User> getUsers() {
        String result = HttpRequest.get("/1.0/users/users");
        return deserialize(result);
    }

    private static List<User> deserialize(String json) {
        if(json == null) {
            return Collections.emptyList();
        }
        List<User> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            User user = gson.fromJson(element, User.class);
            list.add(user);
        }
//                System.out.println(gson.toJson(gson.fromJson(json, JsonElement.class)));
        return list;
    }

}
