package com.example.ad.retrofittest.Activities;

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
 * Created by ad on 2018-03-07.
 */

 public class RecyclerAdapterMyActivities extends RecyclerView.Adapter<RecyclerAdapterMyActivities.MyViewHolder> {
    private List<Post> activityList;

    public RecyclerAdapterMyActivities(List<Post> contacts){
        this.activityList =contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  //parent - kontener z informacjami. Layoutinflater laczy kontener(parent)
    // z userslist
    { // view podstawowa klasa po ktorej dziedzicza kontrolki
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myactivitylist,parent,false);
        return new MyViewHolder(view);  //tworzy
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int activityId;
          activityId= activityList.get(position).getActivity_id();

        holder.typeTv.setText(activityList.get(position).getKind_of_activity());
        holder.whoTv.setText(activityList.get(position).getLogin());   //laczy
        holder.ActivityDetailBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context;
                final Intent intent;
                context = v.getContext();
                intent =  new Intent(context, ActivityDetails.class);
                intent.putExtra("idOfActivity",activityId);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView typeTv,whoTv;

        Button ActivityDetailBt;

        public MyViewHolder(View itemView) {
            super(itemView);

            typeTv = (TextView) itemView.findViewById(R.id.TypeMyActivityTvId);
            whoTv = (TextView) itemView.findViewById(R.id.WhoMyActivityTvId);
            ActivityDetailBt =(Button) itemView.findViewById(R.id.ButtonMyActivityId);
        }
    }
}