package com.example.ad.retrofittest.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetails extends AppCompatActivity {
	private TextView creatorTv, typeOfActivityTv, descriptionTv;
	private APIService activityDetailsApiService;
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	private RecyclerAdapterParticipantsOfActivity adapter;
	private List<Post> DetailsList, UsersList;
	private int idOfActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		creatorTv = findViewById(R.id.creatorActivityDetailsTvId);
		typeOfActivityTv = findViewById(R.id.typeActivityDetailsTvId);
		descriptionTv = findViewById(R.id.descriptionActivityDetailsTvId);

		recyclerView = findViewById(R.id.UsersOfActivityId);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		activityDetailsApiService = ApiUtils.getAPIService();

		idOfActivity = getIntent().getIntExtra("idOfActivity", 0);
		getActivityDetails(Login.currentUserId, idOfActivity);
		getUsersOfActivity(idOfActivity, "users");
		Log.i("checking_test1", "getting data success." + Login.currentUserId + " " + idOfActivity);

	}

	public void getActivityDetails(int user_id, int activity_id) {
		Call<List<Post>> call = activityDetailsApiService.joinActivityInterface(user_id, activity_id);
		call.enqueue(new Callback<List<Post>>() {
			@Override
			public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                String rawResponse;

                if (response.isSuccessful()) {
					rawResponse = response.toString();
                    try {
                        DetailsList = (List<Post>) new JSONObject(rawResponse).get("activity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("error", "Error parsing JSON. " + rawResponse.toString());
                    }

                    creatorTv.setText(DetailsList.get(0).getLogin());
					typeOfActivityTv.setText(DetailsList.get(0).getKind_of_activity());
					// adressTv.setText(DetailsList.get(0).get());
					descriptionTv.setText(DetailsList.get(0).getDescription_of_activity());
					Log.i("wyslane", "getting data success." + response.body().toString());


				} else {
					System.out.println(response.errorBody().toString());
					try {
						JSONObject jObj = new JSONObject(response.errorBody().string());
						String error = jObj.getString("code");
						String errorText = "Ogólny błąd";
						if (error.equals("serverdown")) {
							errorText = "serwer chwilowo nie odpowiada";
						} else if (error.equals("badreq")) {
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

				Log.e("blad", "getting data failed." + t);

			}
		});
	}

	public void getUsersOfActivity(int activity_id, String mode) {
		Call<List<Post>> call = activityDetailsApiService.getUsersOfActivityInterface(activity_id, mode);
		call.enqueue(new Callback<List<Post>>() {
			@Override
			public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
				if (response.isSuccessful()) {
					Log.i("asggasdgedddfg", "getting data success." + response.body().toString());
					UsersList = response.body();
					adapter = new RecyclerAdapterParticipantsOfActivity(UsersList);
					recyclerView.setAdapter(adapter);

				} else {
					System.out.println(response.errorBody().toString());
					try {
						JSONObject jObj = new JSONObject(response.errorBody().string());
						String error = jObj.getString("code");
						String errorText = "Ogólny błąd";
						if (error.equals("serverdown")) {
							errorText = "serwer chwilowo nie odpowiada";
						} else if (error.equals("badreq")) {
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

				Log.e("assrtsddsdsdfggaf", "getting data failed." + t);

			}
		});
	}

}