package com.example.ad.retrofittest.Maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad.retrofittest.Activities.ActivityDetails;
import com.example.ad.retrofittest.Chat.Chat;
import com.example.ad.retrofittest.Profile.Profile;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.APIService;
import com.example.ad.retrofittest.Retrofit.ApiUtils;
import com.example.ad.retrofittest.Retrofit.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ad on 2018-03-03.
 */

public class DialogFragmentJoin extends DialogFragment {
    private static final String TAG = "DialogFragmentJoin";
    private Button CancelBtDialog,sendBtDialog,profilBtDialog,joinBtDialog;
    private APIService apiServiceStart;
    private int  reciverid;
    private int activityId;
    @SuppressLint("CutPasteId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogjoin, container, false);
        apiServiceStart = ApiUtils.getAPIService();
        TextView nickDialog = (TextView) view.findViewById(R.id.dialogJoinNickId);
        TextView descriptionDialog = (TextView) view.findViewById(R.id.dialogJoinDescriptionId);
        CancelBtDialog = (Button) view.findViewById(R.id.CancelBtDialogId);
        sendBtDialog = (Button) view.findViewById(R.id.sendBtDialogId);
        profilBtDialog = (Button) view.findViewById(R.id.profilBtDialogId);
        joinBtDialog = (Button) view.findViewById(R.id.joinBtDialogId);

         activityId = this.getArguments().getInt("ActivityId",0);
        String login = this.getArguments().getString("Login");
        String description = this.getArguments().getString("Description");


        nickDialog.setText(login);
        descriptionDialog.setText(description);
        Log.i("checking_test2", "getting data success. " + activityId);



        CancelBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });


        getUserIdCall(login);

        sendBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("liczbaid3", "post submitted to API." + reciverid);
                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Chat.class);
                intent.putExtra("IdReciver",reciverid);
                context.startActivity(intent);
            }
        });

        profilBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Profile.class);
                intent.putExtra("idProfil",reciverid);
                context.startActivity(intent);


            }
        });

        joinBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "userid: "+Login.currentUserId+" activityId: "+activityId , Toast.LENGTH_LONG).show();
               final  Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, ActivityDetails.class);
                intent.putExtra("idOfActivity",activityId);
                context.startActivity(intent);
                Log.i("checking_test3", "getting data success. " + activityId);

            }
        });


        return view;

    }





    public void getUserIdCall(String login) {
        Call<Post> call = apiServiceStart.getUserIdInterface(login);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {

               reciverid =response.body().getUser_id();
                Log.i("qwertyy", "post submitted to API." + reciverid);

            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {

                Log.e("blad", "getting data failed." + t);

            }
        });


    }




}
