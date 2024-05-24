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
public class Company {

    public String companyId;
    public String catalogCompanyId;
    public String name;
    public String vatNumber;
    public String address;
    
    public static List<Company> getCompanies(Project project) {
        String result = HttpRequest.get("/3.0/projects/" + project.projectId + "/companies");
        return deserializeResult(result);
    }

    private static List<Company> deserializeResult(String json) {
        if(json == null) {
            return Collections.emptyList();
        }
        List<Company> list = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray items = gson.fromJson(json, JsonObject.class).getAsJsonArray("items");
        Iterator<JsonElement> iterator = items.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            Company company = gson.fromJson(element, Company.class);
            list.add(company);
        }
        return list;
    }

}
