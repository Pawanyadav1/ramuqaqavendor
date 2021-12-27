package com.ramuqaqavendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ramuqaqavendor.R;
import com.ramuqaqavendor.model.ShowUserPojo;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    List<ShowUserPojo> showDocPojos;
    Context context;


    public RatingAdapter(List<ShowUserPojo> showDocPojos, Context context) {
        this.showDocPojos = showDocPojos;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.show_recom_userlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ShowUserPojo data = showDocPojos.get(position);

        holder.txt_username.setText(data.getName());
        holder.txt_date.setText(data.getDate());
        holder.star_rating.setText("("+data.getDate()+")");
        holder.comment.setText(data.getComment());
        holder.rating_avg.setIsIndicator(false);
        holder.rating_avg.setClickable(false);
        holder.rating_avg.setEnabled(false);
        holder.rating_avg.setRating(Float.parseFloat(String.valueOf(data.getRating())));
        holder.rating_avg.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        if (data.getImage().equals("")) {
            holder.iv_user.setImageDrawable(context.getResources().getDrawable(R.drawable.account));
        } else {
            Glide.with(context).load(data.getPath() + data.getImage()).into(holder.iv_user);
        }


    }

    @Override
    public int getItemCount() {
        return showDocPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_username, txt_date,comment,star_rating;
        ImageView iv_user;
        RatingBar rating_avg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_date = itemView.findViewById(R.id.txt_date);
            iv_user = itemView.findViewById(R.id.iv_user);
            comment = itemView.findViewById(R.id.comment);
            star_rating = itemView.findViewById(R.id.star_rating);
            rating_avg = itemView.findViewById(R.id.rating_avg);
        }
    }
}
