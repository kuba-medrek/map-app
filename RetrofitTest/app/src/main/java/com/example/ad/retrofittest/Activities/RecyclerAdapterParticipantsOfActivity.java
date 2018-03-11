package com.example.ad.retrofittest.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad.retrofittest.Chat.Chat;
import com.example.ad.retrofittest.Profile.Profile;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.Post;

import java.util.List;

/**
 * Created by ad on 2018-03-08.
 */



public class RecyclerAdapterParticipantsOfActivity extends RecyclerView.Adapter<RecyclerAdapterParticipantsOfActivity.MyViewHolder> {
    private List<Post> participantsList;

    public RecyclerAdapterParticipantsOfActivity(List<Post> contacts){
        this.participantsList =contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //parent - kontener z informacjami. Layoutinflater laczy kontener(parent)
    // z userslist
    { // view podstawowa klasa po ktorej dziedzicza kontrolki
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participans_of_activity,parent,false);
        return new MyViewHolder(view);  //tworzy
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int userId;
         userId= participantsList.get(position).getUser_id();
        holder.whoTv.setText(participantsList.get(position).getLogin());   //laczy



        holder.ProfileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Profile.class);
                intent.putExtra("idProfil",userId);
                context.startActivity(intent);
            }
        });

        holder.FriendsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                i++;

            }
        });

        holder.MessageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Chat.class);
                intent.putExtra("IdReciver",userId);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView whoTv;

      private  Button ProfileBt,FriendsBt,MessageBt;

        public MyViewHolder(View itemView) {
            super(itemView);


            whoTv = (TextView) itemView.findViewById(R.id.LoginParticipantsTvId);
            ProfileBt =(Button) itemView.findViewById(R.id.ProfilParticipantsBtId);
            FriendsBt =(Button) itemView.findViewById(R.id.FriendsProfilParticipantsBtId);
            MessageBt =(Button) itemView.findViewById(R.id.MessageParticipantsBtId);
        }
    }
}
