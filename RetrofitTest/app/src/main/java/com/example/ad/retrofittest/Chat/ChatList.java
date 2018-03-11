package com.example.ad.retrofittest.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.Retrofit.Post;
import com.example.ad.retrofittest.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatList extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapterUsersChat adapter2;
    private List<Post> contactsUsers;
    private APIService usersListApiService;
    private EditText searchChatList;
    private  int reciverid;
    private String userSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerViewUsers=(RecyclerView) findViewById(R.id.UserRec);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewUsers.setLayoutManager(layoutManager);
        recyclerViewUsers.setHasFixedSize(true);
        usersListApiService = ApiUtils.getAPIService();
        searchChatList = (EditText) findViewById(R.id.searchUserChatListId);
        String userSearch = searchChatList.getText().toString().trim();
        getUsersList(Login.currentUserId);

    }

    public void getUsersList(int user_id) {
        Call<List<Post>> call = usersListApiService.getUsersContactedInterface(user_id);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                Log.i("asdfg", "getting data success." + response.body().toString());
                contactsUsers = response.body();
                adapter2 = new RecyclerAdapterUsersChat(contactsUsers);
                recyclerViewUsers.setAdapter(adapter2);

            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

                Log.e("asssddf", "getting data failed." + t);

            }
        });
    }




        public void searchChatListBt(View v){
         userSearch = searchChatList.getText().toString().trim();
            FutureRetrofit futureRetrofit = new FutureRetrofit(userSearch);
            long i = futureRetrofit.main();
            reciverid = (int) i;
            if(reciverid==0){
                Toast.makeText(this,"nie ma takiego uzytkownika" ,Toast.LENGTH_LONG).show();
            }
            else
            {
                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Chat.class);
                intent.putExtra("IdReciver",reciverid);
                context.startActivity(intent);
                Log.i("usersearch5", "post submitted to API." + reciverid);
            }

    }


}
