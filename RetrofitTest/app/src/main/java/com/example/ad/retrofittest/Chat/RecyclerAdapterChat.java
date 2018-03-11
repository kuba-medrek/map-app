package com.example.ad.retrofittest.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ad.retrofittest.Retrofit.Post;
import com.example.ad.retrofittest.R;

import java.util.List;

/**
 * Created by ad on 2018-02-14.
 */

public class RecyclerAdapterChat extends RecyclerView.Adapter<RecyclerAdapterChat.MyViewHolder> {
    private List<Post> contacts;

    public RecyclerAdapterChat(List<Post> contacts){
        this.contacts=contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //parent - kontener z informacjami. Layoutinflater laczy kontener(parent) z singlemessagelayout
    { // view podstawowa klasa po ktorej dziedzicza kontrolki
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlemessagelayout,parent,false);
        return new MyViewHolder(view);  //tworzy
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       holder.LoginTv.setText(contacts.get(position).getLogin());
        holder.MessagesTv.setText(contacts.get(position).getContent());    //laczy
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView LoginTv, MessagesTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            LoginTv = (TextView) itemView.findViewById(R.id.usernametext);
            MessagesTv = (TextView) itemView.findViewById(R.id.messageText);
        }
    }
}