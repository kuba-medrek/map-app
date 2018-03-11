package com.example.ad.retrofittest.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Maps.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    public static int currentUserId;
    private EditText loginEt, passwordEt;
    private TextView ResponseTv;
    private APIService apiServiceLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEt = findViewById(R.id.loginEtIdLog);
        passwordEt = findViewById(R.id.passwordEtIdLog);
        ResponseTv = findViewById(R.id.answerTvIdLog);
        apiServiceLogin = ApiUtils.getAPIService();
    }

    public void registerActivityBt(View v) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }

    public void loginBt(View v2) {
        String login = loginEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            loginCall(login, password);
        } else {
            showResponse("Wypełnij wszystkie pola");
        }

    }

    public void loginCall(String login, String password) {
        Call<Post> call = apiServiceLogin.loginInterface(login, password);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    currentUserId = response.body().getUser_id();
                    Log.i("api_login_success", "post submitted to API." + currentUserId);

                    Intent i = new Intent(Login.this, StartActivity.class);
                    startActivity(i);
                } else {
                    System.out.println(response.errorBody().toString());
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string());
                        String error = jObj.getString("code");
                        String errorText = "Ogólny błąd";
                        if (error.equals("badpass")) {
                            errorText = "Błędne hasło";
                        } else if (error.equals("noacc")) {
                            errorText = "Nie ma takiego konta";
                        } else if (error.equals("serverdown")) {
                            errorText = "serwer chwilowo nie odpowiada";
                        } else if (error.equals("badreq")) {
                            errorText = "uzupelnij wszystkie pola";
                        }
                        showResponse(errorText);
                    } catch (JSONException e) {
                        showResponse("Fatal server error. Not a JSON on a server.");
                    } catch (IOException e) {
                        showResponse("Error parsing JSON");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                showResponse("Zle haslo lub login");
                Log.e("api_login_failed", "getting data failed." + t);

            }
        });
    }


    public void showResponse(String response) {
        if (ResponseTv.getVisibility() == View.GONE) {
            ResponseTv.setVisibility(View.VISIBLE);
        }
        ResponseTv.setText(response);
    }


}
