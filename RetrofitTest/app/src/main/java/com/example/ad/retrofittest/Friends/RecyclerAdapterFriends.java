package com.example.ad.retrofittest.Friends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad.retrofittest.Profile.Profile;
import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.Post;

import java.util.List;

/**
 * Created by ad on 2018-03-10.
 */


public class RecyclerAdapterFriends extends RecyclerView.Adapter<com.example.ad.retrofittest.Friends.RecyclerAdapterFriends.MyViewHolder> {
    private List<Post> contactsUsers;

    public RecyclerAdapterFriends(List<Post> contacts){
        this.contactsUsers=contacts;
    }

    @Override
    public com.example.ad.retrofittest.Friends.RecyclerAdapterFriends.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //parent - kontener z informacjami. Layoutinflater laczy kontener(parent)
    // z userslist
    { // view podstawowa klasa po ktorej dziedzicza kontrolki
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usersfriends,parent,false);
        return new com.example.ad.retrofittest.Friends.RecyclerAdapterFriends.MyViewHolder(view);  //tworzy
    }

    @Override
    public void onBindViewHolder(final com.example.ad.retrofittest.Friends.RecyclerAdapterFriends.MyViewHolder holder, final int position) {
        final int reciverOfMessageId;

        holder.NameTv.setText(contactsUsers.get(position).getLogin());      //laczy
        reciverOfMessageId=contactsUsers.get(position).getUser_id();
        holder.profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Profile.class);
                intent.putExtra("idProfil",reciverOfMessageId);
                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return contactsUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView NameTv;

        private Button profileButton;


        public MyViewHolder(View itemView) {
            super(itemView);

            NameTv = (TextView) itemView.findViewById(R.id.NameTvIdFriendsListRc);
            profileButton =(Button) itemView.findViewById(R.id.FriendstBtRcId);
        }
    }
}
