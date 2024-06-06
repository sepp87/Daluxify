package io.ost.dlx;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

}
