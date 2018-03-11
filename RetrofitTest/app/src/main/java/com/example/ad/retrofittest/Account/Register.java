package com.example.ad.retrofittest.Account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;
import com.example.ad.retrofittest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

private EditText loginEt, passwordEt, rePasswordEt, emailEt;
private TextView answerTv;
private APIService apiServiceRegister;
    private String errorMng="zero";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginEt = (EditText) findViewById(R.id.loginEtIdReg);
        passwordEt = (EditText) findViewById(R.id.passwordEtIdReg);
        rePasswordEt = (EditText) findViewById(R.id.repasswordEtIdReg);
        emailEt = (EditText) findViewById(R.id.emailEtIdReg);
        answerTv = (TextView) findViewById(R.id.answerTvIdReg);
        apiServiceRegister = ApiUtils.getAPIService();
    }

    public void registerBt(View v){
        String loginString = loginEt.getText().toString().trim();
        String passwordString = passwordEt.getText().toString().trim();
        String rePasswordString=rePasswordEt.getText().toString().trim();
        String emailString=emailEt.getText().toString().trim();
        if (!TextUtils.isEmpty(loginString) && !TextUtils.isEmpty(passwordString)
                && !TextUtils.isEmpty(rePasswordString) && !TextUtils.isEmpty(emailString)) {
            if(passwordString.equals(rePasswordString)) {
                registerCall(loginString, passwordString, emailString);
            } else {
                showResponse("hasla sa rozne");
            }
        } else {
            showResponse("wypelnij wszystkie pola");
        }
    }

    public void registerCall(String login, String password, String email) {
        Call<Post> call = apiServiceRegister.registerInterface(login, password, email);
        call.enqueue(new Callback<Post>() {

            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if(response.isSuccessful()) {
                    Intent i = new Intent(Register.this, Login.class);
                    startActivity(i);
                } else {
                    System.out.println(response.errorBody().toString());
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string());
                        String error = jObj.getString("code");
                        String errorText ="Ogólny błąd";
                        if(error.equals("emaildupl")){
                            errorText = "Jest już konto z takim e-mailem";
                        } else if(error.equals("logindupl")){
                            errorText = "Jest już konto z takim loginem";
                        } else if(error.equals("serverdown")){
                            errorText = "serwer chwilowo nie odpowiada";
                        } else if(error.equals("badreq")) {
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
                showResponse("cos poszlo nie tak");
                Log.e("blad", "Unable to submit post to API." + t);
            }
        });
    }

    public void showResponse(String response) {
        if (answerTv.getVisibility() == View.GONE) {
            answerTv.setVisibility(View.VISIBLE);
        }
        answerTv.setText(response);
    }

}
