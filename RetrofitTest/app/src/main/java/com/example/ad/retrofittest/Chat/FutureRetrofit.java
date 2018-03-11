package com.example.ad.retrofittest.Chat;

import android.util.Log;

import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;

public class FutureRetrofit {
    public String name;

    public FutureRetrofit(String name) {
        this.name = name;
    }





    public long main() {

        final ExecutorService threadpool = Executors.newSingleThreadExecutor();
        Future future = threadpool.submit(new RetrofitCall(name));

        long i = -1;

        try {
            i = (long) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("testfuture2", "post submitted to API." + i);
        return i;
    }

    class RetrofitCall implements Callable {
        private String user;
        private APIService apiServiceStart;
        private long reciverid;

        RetrofitCall (String user) {
            this.user = user;
        }


        @Override
        public Long call() {
            apiServiceStart = ApiUtils.getAPIService();
            Call<Post> call = apiServiceStart.getUserIdInterface(user);
            Response response = null;

            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            reciverid = (long)((Post)response.body()).getUser_id();
            Log.i("return2", "post submitted to API." + reciverid);
            return reciverid;
        }
    }
}