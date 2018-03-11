package com.example.ad.retrofittest.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.Retrofit.Post;
import com.example.ad.retrofittest.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapterChat adapter;
    private List<Post> contacts;
    private APIService chatApiService;
    private EditText inputMessage;
    private String login;
    private int idReciver,idReciverDone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inputMessage = (EditText) findViewById(R.id.searchChatId);
        recyclerView=(RecyclerView) findViewById(R.id.messageRec);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        chatApiService = ApiUtils.getAPIService();


        idReciver=getIntent().getIntExtra("IdReciver",0);



        Log.i("liczbaid", "post submitted to API." + idReciver);

        GetAllMessagesCall(idReciver, Login.currentUserId);

    }

    public void SendBt(View v){
        String messages = inputMessage.getText().toString().trim();
        if ( !TextUtils.isEmpty(messages)) {
            SendCall(messages, Login.currentUserId, idReciver);
        }
        else{
            Log.e("blad", "cos jest puste" );
        }
    }

    public void SendCall(String content, int sender_id, int reciver_id) {
        Call<List<Post>> call = chatApiService.putMessageInterface(content, sender_id,reciver_id );
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                Log.i("wyslane", "getting data success." + response.body().toString());
                contacts = response.body();
                adapter = new RecyclerAdapterChat(contacts);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

                Log.e("blad", "getting data failed." + t);

            }
        });
    }



    public void GetAllMessagesCall(int reciver_id, int sender_id) {
        Call<List<Post>> call = chatApiService.getAllMessagesInterface(reciver_id, sender_id);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                Log.i("wyslane", "getting data success." + response.body().toString());
                contacts = response.body();
                adapter = new RecyclerAdapterChat(contacts);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

                Log.e("blad", "getting data failed." + t);

            }
        });
    }




    public void logoutBt(View v){
        startActivity(new Intent(Chat.this, Login.class));
    }
}
