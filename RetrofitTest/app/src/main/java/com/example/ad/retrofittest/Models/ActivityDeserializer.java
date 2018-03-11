package com.example.ad.retrofittest.Models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class ActivityDeserializer implements JsonDeserializer {
    @Override
    public Activity deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) {
        JsonElement activity = je.getAsJsonObject().get("activity");
        return new Gson().fromJson(activity, Activity.class);
    }
}
