package com.ramuqaqavendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramuqaqavendor.MyOrderActivity;
import com.ramuqaqavendor.R;
import com.ramuqaqavendor.model.OrderIdModel;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import java.util.ArrayList;

public class EariningOrderAdapter extends RecyclerView.Adapter<EariningOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderIdModel> orderIdModels;

    public EariningOrderAdapter(ArrayList<OrderIdModel> orderIdModels, Context context) {
        this.context = context;
        this.orderIdModels = orderIdModels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earing_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final EariningOrderAdapter.ViewHolder viewHolder, final int position) {

        final OrderIdModel data=orderIdModels.get(position);
        viewHolder.txt_orderid.setText("ORDERID: "+data.getUniqID());
        viewHolder.txt_cost.setText("Cost: "+"\u20B9"+" "+data.getPrice());

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

        TextView txt_orderid,txt_cost,txt_date,txt_view;

        public ViewHolder(View itemView){
            super(itemView);

            txt_orderid=itemView.findViewById(R.id.txt_orderid);
            txt_cost=itemView.findViewById(R.id.txt_cost);
            txt_date=itemView.findViewById(R.id.txt_date);

            txt_view=itemView.findViewById(R.id.txt_view);

        }
    }

}
