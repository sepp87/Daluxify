package io.ost.dlx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import io.ost.dlx.model.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public class HttpRequest {

    public static String get(String endpoint) {

        String baseUrl = Config.get().getBaseUrl();
        String apiKey = Config.get().getApiKey();
        String url = endpoint.startsWith(baseUrl) ? endpoint : baseUrl + endpoint;

        Request request = new Request.Builder()
                .header("X-API-KEY", apiKey)
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            return response.isSuccessful() ? response.body().string() : null;
        } catch (IOException ex) {
            Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void download(String endpoint, File target) {

        Request request = new Request.Builder()
                .url(endpoint)
                .build();

        OkHttpClient client = new OkHttpClient();
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
            Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static <T> List<T> deserializeAndGetNextPage(String json, Class<T> classOfT) {
        List<T> list = deserialize(json, classOfT);
        List<T> next = getNextPage(json, classOfT);
        list.addAll(next);
        return list;
    }

    private static <T> List<T> deserialize(String json, Class<T> classOfT) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

    private static <T> List<T> getNextPage(String json, Class<T> classOfT) {
        if (json == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        String link = getNextPageLink(json);
        if (link != null) {
            String result = HttpRequest.get(link);
            List<T> ts = deserializeAndGetNextPage(result, classOfT);
            list.addAll(ts);
        }
        return list;
    }

    public static String getNextPageLink(String responseBody) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
