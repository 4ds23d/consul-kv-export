package org.example.consul;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class KeyDeserializer implements JsonDeserializer<Key> {

    @Override
    public Key deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        } else if (json.isJsonPrimitive()) {
            var primitive = json.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new Key(primitive.getAsString());
            }
        }

        throw new JsonParseException("Cannot Deserialize %s to %s".formatted(json.toString(), Key.class.getSimpleName()));
    }
}