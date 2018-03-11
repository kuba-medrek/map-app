package com.example.ad.retrofittest.Models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.List;

public class ActivitiesDeserializer implements JsonDeserializer {
    @Override
    public List<Activity> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) {
        JsonElement activities = je.getAsJsonObject().get("activity");
        return new Gson().fromJson(activities, List.class);
    }
}