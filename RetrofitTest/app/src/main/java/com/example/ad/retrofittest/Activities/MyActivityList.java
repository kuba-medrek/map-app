package com.example.ad.retrofittest.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActivityList extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapterMyActivities adapter2;
    private List<Post> activitiesList;
    private APIService activitiesListApiService;
    private EditText searchActivitiesList;
    private List<Post> activityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_of_activities_list);

        recyclerViewUsers=(RecyclerView) findViewById(R.id.ActivitiesRec);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewUsers.setLayoutManager(layoutManager);
        recyclerViewUsers.setHasFixedSize(true);
        activitiesListApiService = ApiUtils.getAPIService();
        searchActivitiesList = (EditText) findViewById(R.id.searchActivitiesListId);

        getMyListActivities(Login.currentUserId, "guest");
    }


    public void getMyListActivities(int user_id, String mode) {
        Call<List<Post>> call = activitiesListApiService.getActivitiesInterface(user_id, mode);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if(response.isSuccessful()) {
                    Log.i("asggdfg", "getting data success." + response.body().toString());
                    activityList = response.body();
                    adapter2 = new RecyclerAdapterMyActivities(activityList);
                    recyclerViewUsers.setAdapter(adapter2);

                } else {
                    System.out.println(response.errorBody().toString());
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string());
                        String error = jObj.getString("code");
                        String errorText ="Ogólny błąd";
                          if(error.equals("serverdown")){
                            errorText = "serwer chwilowo nie odpowiada";
                        } else if(error.equals("badreq")) {
                            errorText = "uzupelnij wszystkie pola";
                        }

                        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "Fatal server error. Not a JSON on a server.", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getBaseContext(), "Error parsing JSON", Toast.LENGTH_LONG).show();

                    }
                }



            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

                Log.e("assrtsddf", "getting data failed." + t);

            }
        });
    }
}
