package com.example.ad.retrofittest.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    private TextView loginTv, nameTv, lastNameTv, birthdayTv, descriptionTv;
    private APIService apiServiceProfile;
    private List<Post> profileList;
    private int idReciver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loginTv = (TextView) findViewById(R.id.loginTvIdProfile);
        nameTv = (TextView) findViewById(R.id.nameTvIdProfile);
        lastNameTv = (TextView) findViewById(R.id.lastNameTvIdProfile);
        birthdayTv = (TextView) findViewById(R.id.birthdayTvIdProfile);
        descriptionTv = (TextView) findViewById(R.id.descriptonTvIdProfile);

        apiServiceProfile= ApiUtils.getAPIService();

        idReciver=getIntent().getIntExtra("idProfil",0);
        if(idReciver==0)
        getProfile(Login.currentUserId);
        else
            getProfile(idReciver);

    }

    public void getProfile(int user_id) {
        Call<List<Post>> call = apiServiceProfile.getProfileInterface(user_id);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                profileList=response.body();

                loginTv.setText("Nick: " + profileList.get(0).getLogin());
                nameTv.setText("Imie: " + profileList.get(0).getName());
                lastNameTv.setText("Nazwisko: " + profileList.get(0).getLastName());
                descriptionTv.setText("Opis: " + profileList.get(0).getDescription());
                Log.i("wyslane", "getting data success." + response.body().toString());

            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

                Log.e("blad", "getting data failed." + t);

            }
        });
    }

    public void showResponse(String response) {
        if (loginTv.getVisibility() == View.GONE) {
            loginTv.setVisibility(View.VISIBLE);
        }
        loginTv.setText(response);
    }
}
