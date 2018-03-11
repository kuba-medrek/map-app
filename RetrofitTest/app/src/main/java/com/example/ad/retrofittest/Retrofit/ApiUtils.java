package com.example.ad.retrofittest.Retrofit;

/**
 * Created by ad on 2018-01-27.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://adrian.kubahaha.tk/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);


    }
}