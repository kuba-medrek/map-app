package com.example.ad.retrofittest.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

	@POST("backend/account/login.php")
	@FormUrlEncoded
	Call<Post> loginInterface(
		@Field("login") String login,
		@Field("password") String password);

	@POST("backend/account/register.php")
	@FormUrlEncoded
	Call<Post> registerInterface(
		@Field("login") String login,
		@Field("password") String password,
		@Field("email") String email);

	@POST("backend/activity/addActivity.php")
	@FormUrlEncoded
	Call<List<Post>> markerSendInterface(
		@Field("creatorId") int user_id,
		@Field("lat") double lat,
		@Field("lng") double lng,
		@Field("kindOfActivity") String kind_of_activity,
		@Field("activityDescription") String description_of_activity,
		@Field("activityName") String nameOfActivity);

	@POST("backend/activity/listActivities.php")
	@FormUrlEncoded
	Call<List<Post>> getActivitiesInterface(
		@Field("userId") int user_id,
		@Field("mode") String mode);

	@POST("backend/activity/listActivities.php")
	@FormUrlEncoded
	Call<List<Post>> getAllActivitiesInterface(
			@Field("mode") String mode);

	@POST("backend/activity/activity.php")
	@FormUrlEncoded
	Call<List<Post>> getUsersOfActivityInterface(
		@Field("activityId") int activity_id,
		@Field("mode") String mode);


	@POST("backend/activity/joinActivity.php")
	@FormUrlEncoded
	Call<List<Post>> joinActivityInterface(
		@Field("userId") int user_id,
		@Field("activityId") int activity_id);


/*********************************/

	@POST("retrofittest/chat/gettAllMessages.php")
	@FormUrlEncoded
	Call<List<Post>> getAllMessagesInterface(
		@Field("sender_id") int sender_id,
		@Field("reciver_id") int reciver_id);

	@POST("retrofittest/chat/getUserId.php")
	@FormUrlEncoded
	Call<Post> getUserIdInterface(@Field("login") String login);

	@POST("retrofittest/profile/getProfile.php")
	@FormUrlEncoded
	Call<List<Post>> getProfileInterface(@Field("user_id") int user_id);

	@POST("retrofittest/chat/GetUsersList.php")
	Call<List<Post>> GetUsersListInterface();

	@POST("retrofittest/chat/getUsersContacted.php")
	@FormUrlEncoded
	Call<List<Post>> getUsersContactedInterface(@Field("user_id") int user_id);

	@POST("retrofittest/chat/PutMessage.php")
	@FormUrlEncoded
	Call<List<Post>> putMessageInterface(@Field("content") String content,
										 @Field("sender_id") int sender_id,
										 @Field("reciver_id") int reciver_id);
}

