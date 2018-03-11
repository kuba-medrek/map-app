package com.example.ad.retrofittest.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad.retrofittest.Account.Login;
import com.example.ad.retrofittest.Activities.MyActivityList;
import com.example.ad.retrofittest.Chat.ChatList;
import com.example.ad.retrofittest.Models.Activity;
import com.example.ad.retrofittest.Models.ActivityDeserializer;
import com.example.ad.retrofittest.Profile.Profile;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ad.retrofittest.Account.Login.currentUserId;

public class StartActivity extends AppCompatActivity implements OnMapReadyCallback {
	//need this to get permission from user
	public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
	public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
	private static final int PERMISSION_REQUEST_CODE = 1234;
	private GoogleMap mMap;
	private APIService apiServiceStart;
	private LocationManager locationManager;
	public EditText rodzajEt, opisEt;
	private Marker marker;
	private static LatLng latLngUserActivity;
	public List<Activity> activitiesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		apiServiceStart = ApiUtils.getAPIService();
		rodzajEt = findViewById(R.id.rodzajEtId);
		opisEt = findViewById(R.id.opisEtId);

		Toolbar toolbar = findViewById(R.id.toolbarid);
		setSupportActionBar(toolbar);

		PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
		placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {


			@Override
			public void onPlaceSelected(Place place) {
				latLngUserActivity = place.getLatLng();

			}

			@Override
			public void onError(Status status) {
				Toast.makeText(StartActivity.this, "blad" + status.toString(), Toast.LENGTH_LONG).show();

			}
		});
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
        LatLng myGPSPosition;
		mMap = googleMap;

		if (ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions();
		} else {
			mMap.setMyLocationEnabled(true);
		}
		try {
			Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			//here what you need:
			double latitude = locationGPS.getLatitude();
			double longitude = locationGPS.getLongitude();
			//create marker
			myGPSPosition = new LatLng(latitude, longitude);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myGPSPosition, 13));

		} catch (NullPointerException | IllegalArgumentException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to get GPS data", Toast.LENGTH_SHORT).show();
		}


		getAllMarkers();


		mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				DialogFragmentJoin dialog = new DialogFragmentJoin();
				dialog.show(getSupportFragmentManager(), "DialogFragmentJoin");

				//PACK DATA IN A BUNDLE
				int i = (int) marker.getTag();
				Bundle bundle = new Bundle();
				bundle.putString("Login", marker.getTitle());
				bundle.putString("Description", marker.getSnippet());
				bundle.putInt("ActivityId", i);

				//PASS OVER THE BUNDLE TO OUR FRAGMENT
				dialog.setArguments(bundle);
				return true;
			}
		});

	}


	public void addActivity(View v) {
        LatLng coordinates = StartActivity.latLngUserActivity;

		double lat = 0;
		double lng = 0;

		try {
			lat = coordinates.latitude;
			lng = coordinates.longitude;
		} catch (NullPointerException e) {
			Toast.makeText(this, "Nie wybrano pozycji na mapie", Toast.LENGTH_LONG).show();
		}

		String type = rodzajEt.getText().toString().trim();
		String description = opisEt.getText().toString().trim();

		if ((!type.equals("")) && (!description.equals(""))) {
			sendMarkerCall(currentUserId, lat, lng, type, description, "nazwa aktywności");
		} else {
			Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_LONG).show();
		}
	}


	public void sendMarkerCall(int user_id, double lat, double lng, String kind_of_activity, String description_of_activity, String nameOfActivity) {
		Call<List<Post>> call = apiServiceStart.markerSendInterface(user_id, lat, lng, kind_of_activity, description_of_activity, nameOfActivity);
		call.enqueue(new Callback<List<Post>>() {
			@Override
			public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
				if (response.isSuccessful()) {

                    Gson activityGson = new GsonBuilder()
                            .registerTypeAdapter(Activity.class, new ActivityDeserializer())
                            .create();

                    Activity newActivity = activityGson.fromJson(response.toString(), Activity.class);

					int activityId = newActivity.getActivityId();
					String kindOfActivity = newActivity.getKindOfActivity();
					String descriptionOfActivity = newActivity.getDescription();
					String nameActivity = newActivity.getName();
					Double lat = newActivity.getLat();
					Double lng = newActivity.getLng();

					LatLng latLng = new LatLng(lat, lng);
					marker = mMap.addMarker(new MarkerOptions().position(latLng).title(nameActivity)
							.snippet(kindOfActivity + System.lineSeparator() + descriptionOfActivity));
					marker.setTag(activityId);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

					Log.i("start_activity_sent", "post submitted to API." + response.body());

				} else {
					try {
						JSONObject jObj = new JSONObject(response.errorBody().toString());
						String error = jObj.getString("code");
						String errorText = "Ogólny błąd";
						if (error.equals("badformat")) {
							errorText = "Zły format";
						} else if (error.equals("serverdown")) {
							errorText = "serwer chwilowo nie odpowiada";
						} else if (error.equals("badreq")) {
							errorText = "uzupelnij wszystkie pola";
						}

						Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						Toast.makeText(getBaseContext(), "Fatal server error. Not a JSON on a server.", Toast.LENGTH_LONG).show();
					}
				}

			}

			@Override
			public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {

				Log.e("start_activity_failed", "getting data failed." + t);

			}
		});
	}


	public void getAllMarkers() {
		Call<List<Post>> call = apiServiceStart.getAllActivitiesInterface("all");
		call.enqueue(new Callback<List<Post>>() {
			@Override
			public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {

                Gson activitiesGson = new GsonBuilder()
                        .registerTypeAdapter(Activity.class, new ActivityDeserializer())
                        .create();

				if (response.isSuccessful()) {
                    activitiesList = activitiesGson.fromJson(response.toString(), List.class);

					for (Activity activity : activitiesList) {

						int activityId = activity.getActivityId();
						String kindOfActivity = activity.getKindOfActivity();
						String descriptionOfActivity = activity.getDescription();
						Double lat = activity.getLat();
						Double lng = activity.getLng();
						String name = activity.getName();

						LatLng latLng = new LatLng(lat, lng);
						marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
								.snippet(kindOfActivity + System.lineSeparator() + descriptionOfActivity));
						marker.setTag(activityId);
					}
					//  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

					Log.i("start_activity_sent2", "post submitted to API." + response.body());


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

				Log.e("start_activity_failed", "getting data failed." + t);

			}
		});
	}


	public void requestPermissions() {
		ActivityCompat.requestPermissions(this,
			new String[]{
				ACCESS_COARSE_LOCATION,
				ACCESS_FINE_LOCATION
			},
			PERMISSION_REQUEST_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menulist, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
			case R.id.profileIdMenu:
				i = new Intent(StartActivity.this, Profile.class);
				startActivity(i);
				break;

			case R.id.photosIdMenu:
				i = new Intent(StartActivity.this, Profile.class);
				startActivity(i);
				break;

			case R.id.friendsIdMenu:
				i = new Intent(StartActivity.this, Profile.class);
				startActivity(i);
				break;

			case R.id.messagesIdMenu:
				i = new Intent(StartActivity.this, ChatList.class);
				startActivity(i);
				break;

			case R.id.activitiesIdMenu:
				i = new Intent(StartActivity.this, MyActivityList.class);
				startActivity(i);
				break;

			case R.id.LogoutIdMenu:
				i = new Intent(StartActivity.this, Login.class);
				startActivity(i);
				break;

		}
		return super.onOptionsItemSelected(item);
	}


}
