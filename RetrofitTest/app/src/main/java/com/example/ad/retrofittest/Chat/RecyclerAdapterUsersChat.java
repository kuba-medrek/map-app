package com.example.ad.retrofittest.Chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad.retrofittest.R;
import com.example.ad.retrofittest.Retrofit.Post;

import java.util.List;

/**
 * Created by ad on 2018-02-21.
 */

public class RecyclerAdapterUsersChat extends RecyclerView.Adapter<RecyclerAdapterUsersChat.MyViewHolder> {
    private List<Post> contactsUsers;

    public RecyclerAdapterUsersChat(List<Post> contacts){
        this.contactsUsers=contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //parent - kontener z informacjami. Layoutinflater laczy kontener(parent)
    // z userslist
    { // view podstawowa klasa po ktorej dziedzicza kontrolki
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist,parent,false);
        return new MyViewHolder(view);  //tworzy
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int reciverOfMessageId;

        holder.NameTv.setText(contactsUsers.get(position).getLogin());      //laczy
        reciverOfMessageId=contactsUsers.get(position).getUser_id();
        holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, Chat.class);
                intent.putExtra("IdReciver",reciverOfMessageId);
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

       private Button sendButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            NameTv = (TextView) itemView.findViewById(R.id.NameTvIdUserListRc);
            sendButton=(Button) itemView.findViewById(R.id.ChatBtRcId);
        }
    }
}