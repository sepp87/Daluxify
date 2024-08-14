package dlx.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public class ApiClient {

    private static final Map<String, ApiClient> API_CLIENTS = new TreeMap<>();
    public static final com.google.gson.Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final String apiKey;
    private final String baseUrl;
    private final OkHttpClient client;

    public ApiClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
        API_CLIENTS.put(apiKey, this);
    }

    public String get(String endpoint) {
        return executeRequest(Method.GET, endpoint, null);
    }

    public String post(String endpoint, String json) {
        return executeRequest(Method.POST, endpoint, json);
    }

    public String patch(String endpoint, String json) {
        return executeRequest(Method.PATCH, endpoint, json);
    }

    private String executeRequest(Method method, String endpoint, String json) {
        String url = endpoint.startsWith(baseUrl) ? endpoint : baseUrl + endpoint;

        try {
            Builder builder = new Request.Builder()
                    .header("X-API-KEY", apiKey)
                    .url(url);

            RequestBody body = json == null || json.equals("") ? null : RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            switch (method) {
                case PATCH:
                    builder.patch(body);
                    break;
                case POST:
                    builder.post(body);
                    break;
            }

            Request request = builder.build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                if (App.LOG) {
                    System.out.println(result);
                }
                return result;
            }
            // Print request and response information in case of an unsuccessful call 
            System.out.println(method.toString() + " " + url);
            System.out.println("Request Body: " + json);
            System.out.println("Response Body: " + response.body().string());
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(ApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void download(String endpoint, File target) {

        Request request = new Request.Builder()
                .url(endpoint)
                .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                System.out.println("Download failed: " + response.code() + " " + response.message());
                response.body().close();
                return;
            }
            FileOutputStream fos = new FileOutputStream(target);
            ResponseBody body = response.body();
            fos.write(body.bytes());
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(ApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <T> List<T> deserializeAndGetNextPage(String json, Class<T> classOfT) {
        List<T> list = deserializeList(json, classOfT);
        List<T> next = getNextPage(json, classOfT);
        list.addAll(next);
        return list;
    }

    private <T> List<T> deserializeList(String json, Class<T> classOfT) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        Gson gson = GSON;
        JsonObject responseObject = gson.fromJson(json, JsonObject.class);
        if (!responseObject.get("items").isJsonArray()) {
            return Collections.emptyList();
        }
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonObject object = iterator.next().getAsJsonObject();
            if (object.get("data") != null) {
                object = object.get("data").getAsJsonObject();
            }
            var t = gson.fromJson(object, classOfT);
            list.add(t);
            if (App.LOG) {
                System.out.println(gson.toJson(t));
            }
        }
        return list;
    }

    public <T> T deserializeObject(String json, Class<T> classOfT) {
        Gson gson = GSON;
        return deserializeObject(gson, gson.fromJson(json, JsonObject.class), classOfT);
    }

    private <T> T deserializeObject(Gson gson, JsonObject object, Class<T> classOfT) {
        if (object.get("data") != null) {
            object = object.get("data").getAsJsonObject();
        }
        var t = gson.fromJson(object, classOfT);
        if (App.LOG) {
            System.out.println(gson.toJson(t));
        }
        return t;
    }

    private <T> List<T> getNextPage(String json, Class<T> classOfT) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        String link = getNextPageLink(json);
        if (link != null) {
            String result = this.get(link);
            List<T> ts = deserializeAndGetNextPage(result, classOfT);
            list.addAll(ts);
        }
        return list;
    }

    public String getNextPageLink(String responseBody) {
        Gson gson = GSON;
        JsonArray links = gson.fromJson(responseBody, JsonObject.class).getAsJsonArray("links");
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

}

enum Method {
    GET, POST, PATCH;
}
