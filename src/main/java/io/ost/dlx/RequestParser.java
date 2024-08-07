package io.ost.dlx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Joost
 */
public class RequestParser {

    private static final com.google.gson.Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private RequestParser() {
    }

    public static Gson getGson() {
        return gson;
    }
}
