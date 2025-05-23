package dlx.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.BufferedSink;

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
    private static final int MAX_CONCURRENT_REQUESTS = 3;

    public ApiClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;

        this.client = new OkHttpClient();
        this.client.setConnectTimeout(Config.API_TIMEOUT_LIMIT, TimeUnit.SECONDS);
        this.client.setReadTimeout(Config.API_TIMEOUT_LIMIT, TimeUnit.SECONDS);
        this.client.setWriteTimeout(Config.API_TIMEOUT_LIMIT, TimeUnit.SECONDS);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(MAX_CONCURRENT_REQUESTS);
        dispatcher.setMaxRequestsPerHost(MAX_CONCURRENT_REQUESTS);
        this.client.setDispatcher(dispatcher);

        API_CLIENTS.put(apiKey, this);
    }

    public String get(String endpoint) {
        return buildRequestAndExecute(Method.GET, endpoint, null);
    }

    public String post(String endpoint, String json) {
        return buildRequestAndExecute(Method.POST, endpoint, json);
    }

    public String patch(String endpoint, String json) {
        return buildRequestAndExecute(Method.PATCH, endpoint, json);
    }

    private String buildRequestAndExecute(Method method, String endpoint, String json) {
        // Prepare request
        String url = endpoint.startsWith(baseUrl) ? endpoint : baseUrl + endpoint;
        RequestBody body = json == null || json.equals("") ? null : RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        // Build request
        Builder builder = new Request.Builder().header("X-API-KEY", apiKey).url(url);
        switch (method) {
            case PATCH:
                builder.patch(body);
                break;
            case POST:
                builder.post(body);
                break;
        }
        Request request = builder.build();

        return executeRequest(request, json, 0);
    }

    String executeRequest(Request request, String body, int attempt) {
        while (attempt < Config.API_MAX_TRIES) {
            attempt++;
            try {
                Call call = client.newCall(request);
                Response response = call.execute();
                try ( ResponseBody responseBody = response.body()) {
                    if (responseBody == null) {
                        return null;
                    }
                    String result = responseBody.string();
                    if (response.isSuccessful()) {
                        if (Config.LOG_RESPONSE) {
                            System.out.println(result);
                        }
                        return result;
                    }
                    // Log unsuccessful response
                    if (Config.LOG_ERRORS) {
                        System.out.println(request.method() + " " + request.urlString());
                        System.out.println("Request Body: " + body);
                        System.out.println("Response Body: " + result);
                    }
                }
            } catch (SocketTimeoutException ex) {
                System.err.println("Timeout occurred: " + ex.getMessage() + " on attempt " + attempt);
                if (attempt == Config.API_MAX_TRIES) {
                    // TODO Bench this request for later
                }
            } catch (IOException | IllegalArgumentException ex) {
                System.err.println("Request failed: " + ex.getMessage() + " on attempt " + attempt);
            }
        }
        // Log final failure after all attempts
        if (Config.LOG_ERRORS) {
            System.err.println("All attempts failed for request: " + request.method() + " " + request.url().toString());
        }
        return null;
    }

    public <T> AsyncResponse<T> getAsync(String endpoint, Class T) {
        return executeRequestAsync(Method.GET, endpoint, null, T);
    }

    private <T> AsyncResponse<T> executeRequestAsync(Method method, String endpoint, String json, Class T) {
        String url = endpoint.startsWith(baseUrl) ? endpoint : baseUrl + endpoint;

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
        AsyncResponse<T> asyncResponse = new AsyncResponse(method, url, json, T, this, App.pendingCalls, App.latch);
        client.newCall(request).enqueue(asyncResponse);
        return asyncResponse;
    }

    /**
     * Uploads a file in parts
     *
     * @param endpoint The upload endpoint
     * @param file The file to upload
     * @param chunkSizeLimit Maximum chunk size in bytes is 5_242_880
     *
     */
    public boolean upload(String endpoint, java.io.File file, int chunkSizeLimit) {

        String url = endpoint.startsWith(baseUrl) ? endpoint : baseUrl + endpoint;
        long totalBytes = file.length();

        try ( FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[chunkSizeLimit];
            int bytesRead;
            long bytesUploaded = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                final int chunkLength = bytesRead;

                long start = bytesUploaded;
                long end = bytesUploaded + chunkLength - 1;

                System.out.printf("Uploading bytes %d–%d of %d%n", start, end, totalBytes);

                RequestBody body = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse("application/octet-stream");
                    }

                    @Override
                    public long contentLength() {
                        return chunkLength;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.write(buffer, 0, chunkLength);
                    }
                };

                String filename = file.getName();  // e.g. "Umlaute üöäß ÖÄÜ.pdf"
                String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());

                // URLEncoder turns spaces into '+', so convert those back to %20:
                encoded = encoded.replace("+", "%20");
                String contentDisposition = "form-data; filename*=UTF-8''" + encoded; // Use RFC 5987 “extended” filename*

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("X-API-KEY", apiKey)
                        .addHeader("Content-Disposition", contentDisposition)
                        .addHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, totalBytes))
                        .build();

                Response response = client.newCall(request).execute();

                if (response.code() == 202) {
                    if (Config.LOG_RESPONSE) {
                        String message = String.format("Chunk %d–%d uploaded: HTTP %d", start, end, response.code());
                        System.out.println(message);
                    }
                } else {
                    if (Config.LOG_ERRORS) {
                        String message = String.format("Chunk %d–%d failed: HTTP %d - %s", start, end, response.code(), response.body().string());
                        System.out.println(message);
                    }
                    return false;
                }

                bytesUploaded += chunkLength;
                response.body().close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ApiClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
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
        if (json == null) {
            return Collections.emptyList();
        }
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
            if (Config.LOG_RESPONSE_PRETTY) {
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
        if (Config.LOG_RESPONSE_PRETTY) {
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
            String result = this.get(link); // GET next page results
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
