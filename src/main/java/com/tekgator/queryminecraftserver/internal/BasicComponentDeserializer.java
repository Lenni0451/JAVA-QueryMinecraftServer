package com.tekgator.queryminecraftserver.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic component deserializer which ignores formattings and just give you the raw text
 */
public class BasicComponentDeserializer {

    public static String deserialize(final JsonElement element) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else if (element.isJsonArray()) {
            final JsonArray array = element.getAsJsonArray();
            StringBuilder text = new StringBuilder();
            for (JsonElement jsonElement : array) {
                text.append(deserialize(jsonElement));
            }
            return text.toString();
        } else if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            StringBuilder text = new StringBuilder();

            if (object.has("text")) {
                text.append(object.get("text").getAsString());
            } else if (object.has("translate")) {
                Object[] arguments = new Object[0];
                if (object.has("with")) {
                    List<String> argumentsList = new ArrayList<>();
                    final JsonArray with = object.getAsJsonArray("with");
                    for (JsonElement jsonElement : with) {
                        argumentsList.add(deserialize(jsonElement));
                    }
                    arguments = argumentsList.toArray(new String[0]);
                }
                text.append(String.format(object.get("translate").getAsString(), arguments));
            } else if (object.has("score")) {
                if (object.has("value")) {
                    text.append(object.get("value"));
                }
            } else if (object.has("selector")) {
                text.append(object.get("selector"));
            } else if (object.has("keybind")) {
                text.append(object.get("keybind"));
            }
            if (object.has("extra")) {
                JsonArray extra = object.get("extra").getAsJsonArray();
                for (JsonElement jsonElement : extra) {
                    text.append(deserialize(jsonElement));
                }
            }

            return text.toString();
        } else {
            throw new UnsupportedOperationException("Unable to deserialize '" + element + "'");
        }
    }

}
