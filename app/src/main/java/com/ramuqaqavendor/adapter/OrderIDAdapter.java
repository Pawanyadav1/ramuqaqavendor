package com.ramuqaqavendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ramuqaqavendor.MyOrderActivity;
import com.ramuqaqavendor.R;
import com.ramuqaqavendor.model.OrderIdModel;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import java.util.ArrayList;

public class OrderIDAdapter extends RecyclerView.Adapter<OrderIDAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderIdModel> orderIdModels;

    public OrderIDAdapter(ArrayList<OrderIdModel> orderIdModels, Context context) {
        this.context = context;
        this.orderIdModels = orderIdModels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_listing, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final OrderIDAdapter.ViewHolder viewHolder, final int position) {

        final OrderIdModel data=orderIdModels.get(position);
        viewHolder.txt_orderid.setText("ORDERID: "+data.getUniqID());
        viewHolder.txt_date.setText(data.getDate());


        if (!data.getDelievery().equals("")){
            int amount=Integer.parseInt(data.getDelievery())+Integer.parseInt(data.getPrice());
            viewHolder.txt_cost.setText("Cost: "+"\u20B9"+" "+data.getPrice());
        }else {
            viewHolder.txt_cost.setText("Cost: "+"\u20B9"+" "+data.getPrice());
        }
        viewHolder.txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(context, APPCONSTANT.ORDERID,data.getUniqID());
                context.startActivity(new Intent(context, MyOrderActivity.class));
            }
        });

        if (data.getStatus().equals("1")){
            viewHolder.txt_status.setText("Order accepted");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }else  if (data.getStatus().equals("2")){
            viewHolder.txt_status.setText("Order rejected");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }else  if (data.getStatus().equals("6")){
            viewHolder.txt_status.setText("Order Cancelled");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }else  if (data.getStatus().equals("3")){
            viewHolder.txt_status.setText("Completed & delivered");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }else  if (data.getStatus().equals("4")){
            viewHolder.txt_status.setText("Order picked");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }else {
            viewHolder.txt_status.setText("Pending");
            viewHolder.txt_status.setTextColor(context.getResources().getColor(R.color.new_green));
        }

    }
    @Override
    public int getItemCount() {
        return orderIdModels.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_orderid,txt_cost,txt_date,txt_view,txt_status;

        public ViewHolder(View itemView){
            super(itemView);

            txt_orderid=itemView.findViewById(R.id.txt_orderid);
            txt_cost=itemView.findViewById(R.id.txt_cost);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_status=itemView.findViewById(R.id.txt_status);
            txt_view=itemView.findViewById(R.id.txt_view);

        }
    }

}
