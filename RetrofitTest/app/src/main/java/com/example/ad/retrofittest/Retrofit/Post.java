package com.example.ad.retrofittest.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {

    public String getLastName() {
        return lastName;
    }

    public String getDescription() {
        return description;
    }

    @SerializedName("description_of_activity")
    @Expose
    private String description_of_activity;

    @SerializedName("kind_of_activity")
    @Expose
    private String kind_of_activity;

    public String getDescription_of_activity() {
        return description_of_activity;
    }

    public String getKind_of_activity() {
        return kind_of_activity;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lng")
    @Expose
    private Double lng;

    @SerializedName("lastname")
    @Expose
    private String lastName;






    public int getActivity_id() {
        return activity_id;
    }

    @SerializedName("activity_id")
    @Expose
    private int activity_id;

    public int getSender_id() {
        return sender_id;
    }

    public int getReciver_id() {
        return reciver_id;
    }

    @SerializedName("sender_id")
    @Expose
    private int sender_id;

    @SerializedName("reciver_id")
    @Expose
    private int reciver_id;

    @SerializedName("description")
    @Expose
    private String description;

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private Date Date;

    @SerializedName("time")
    @Expose
    private java.sql.Time Time;

    public String getContent(){
        return content;
    }

    public Date getDate(){
        return Date;
    }




    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("email")
    @Expose
    private String Email;

    public String getName(){
        return Name;
    }

    public String getEmail(){
        return Email;
    }

    @SerializedName("login")
    @Expose
    private String login;

    public String getLogins() {
        return logins;
    }

    public int getIds() {
        return ids;
    }

    @SerializedName("logins")
    @Expose
    private String logins;

    @SerializedName("ids")
    @Expose
    private int ids;

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("user_id")
    @Expose
    private int user_id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setId(Integer id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", currentUserId=" + user_id +
                '}';
    }


    public String getNameOfActivity() {
        return nameOfActivity;
    }

    //ponizej nowe
    @SerializedName("activity_name")
    @Expose
   private String nameOfActivity;

}
