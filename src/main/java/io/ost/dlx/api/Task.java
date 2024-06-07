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
public class Task {

    public String taskId;
    public String subject;
    public String usage;
    public Type type;
    public String number;
    public String created;
    public CreatedBy createdBy;
    public Workflow workflow;
    public Location location;

    public static List<Task> getTasks(Project project) {
        String result = HttpRequest.get("/5.0/projects/" + project.projectId + "/tasks");
        return deserializeAndGetNextPage(result);
    }

    private static List<Task> deserializeAndGetNextPage(String json) {
        List<Task> list = deserialize(json);
        List<Task> next = getNextPage(json);
        list.addAll(next);
        return list;
    }

    private static List<Task> deserialize(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<Task> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next().getAsJsonObject().get("data");
            Task task = gson.fromJson(element, Task.class);
            list.add(task);
//            System.out.println(gson.toJson(task));
        }
        return list;
    }

    private static List<Task> getNextPage(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<Task> list = new ArrayList<>();
        String link = HttpRequest.getNextPageLink(json);
        if (link != null) {
            String result = HttpRequest.get(link);
            list.addAll(deserializeAndGetNextPage(result));
        }
        return list;
    }

    public class Type {

        public String typeId;
        public String name;
    }

    public class CreatedBy {

        public String userId;
    }

    public class Workflow {

        public String name;
    }

    public class Location {

        public Coordinate coordinate;
        public Level level;
        public Room room;
        public Building building;
        public Drawing drawing;
        public BimObject bimObject;
        public List<LocationImage> locationImages;

        public class Coordinate {

            public Xyz xyz;
            public Gps gps;

            public class Xyz {

                public String x;
                public String y;
                public String z;
            }

            public class Gps {

                public String lat;
                public String lng;
            }
        }

        public class Level {

            public String name;
        }

        public class Room {

            public String name;
        }

        public class Building {

            public String name;
        }

        public class Drawing {

            public String name;
        }

        public class BimObject {

            public String name;
            public String categoryName;
        }

        public class LocationImage {

            public String name;
            public String fileDownload;
        }

        public class Zones {
        }

        public class UserDefinedFields {
        }

    }

}
