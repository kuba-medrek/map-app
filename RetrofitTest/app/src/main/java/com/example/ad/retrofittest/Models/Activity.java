package com.example.ad.retrofittest.Models;

import android.app.ActivityOptions;

public class Activity {
    private int activityId;
    private int creatorId;
    private String name;
    private String kindOfActivity;
    private String description;
    private double lat;
    private double lng;
    private String startDate;

    public Activity(int activityId, int creatorId, String name, String kindOfActivity, String description, double lat, double lng, String startDate) {
        this.activityId = activityId;
        this.creatorId = creatorId;
        this.name = name;
        this.kindOfActivity = kindOfActivity;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.startDate = startDate;
    }

    public int getActivityId() {
        return activityId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getName() {
        return name;
    }

    public String getKindOfActivity() {
        return kindOfActivity;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getStartDate() {
        return startDate;
    }
}
