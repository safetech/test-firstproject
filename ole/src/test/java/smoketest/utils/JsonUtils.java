package smoketest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class JsonUtils {
    public static JsonObject createJsonFromString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JsonElement.class).getAsJsonObject();
    }


    public static JsonArray createJsonArrayFromString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JsonArray.class).getAsJsonArray();
    }

    public static String getJsonStringValue(JsonObject json, String key) {
        if (!json.has(key)) {
            return null;
        }
        return json.get(key).getAsString();
    }

    public static String getJsonBigDecimalAsString(JsonObject json, String key) {
        if (!json.has(key)) {
            return null;
        }

        return json.get(key).getAsBigDecimal().toString();
    }
}
