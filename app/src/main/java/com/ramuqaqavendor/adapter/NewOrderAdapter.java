package com.ramuqaqavendor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramuqaqavendor.MyOrderActivity;
import com.ramuqaqavendor.R;
import com.ramuqaqavendor.model.OrderModel;

import java.util.ArrayList;

;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderModel> orderModels;

    public NewOrderAdapter(ArrayList<OrderModel> orderModels, Context context) {
        this.context = context;
        this.orderModels = orderModels;
    }
    @Override
    public NewOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        NewOrderAdapter.ViewHolder viewHolder = new NewOrderAdapter.ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final NewOrderAdapter.ViewHolder viewHolder, final int position) {

        final OrderModel data=orderModels.get(position);
        viewHolder.txt_cat.setText(data.getCatname());
        viewHolder.txt_name.setText(data.getName());
        viewHolder.txt_qty.setText("Quantity: "+data.getQty());
        viewHolder.txt_unit.setText("Unit: "+data.getUnit());
        viewHolder.txt_price.setText("\u20B9"+" "+data.getPrice());
       // viewHolder.notes.setText("Notes: "+" "+data.getNotes());
        Log.e("sdafadfaddfdfdf", data.getPickStatus() );
        viewHolder.checkbox.setVisibility(View.GONE);
        if (data.getPickStatus().equals("1")){
            viewHolder.checkbox.setChecked(true);
        }else {
            viewHolder.checkbox.setChecked(false);
        }
        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                               if (isChecked) {
                                                                   try {
                                                                       MyOrderActivity.Arr_productId.add(data.getID());
                                                                   } catch (Exception e) {
                                                                       e.printStackTrace();
                                                                   }

                                                               } else {
                                                                   try {
                                                                       MyOrderActivity.Arr_productId.remove(position);
                                                                   } catch (Exception e) {
                                                                       e.printStackTrace();
                                                                   }


                                                               }
                                                           }
                                                       }
        );



    }
    @Override
    public int getItemCount() {
        return orderModels.size();
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
        public ImageView  iv_cat;
        TextView txt_name,txt_cat,txt_qty,txt_unit,txt_price, notes;
        CheckBox checkbox;
        public ViewHolder(View itemView){
            super(itemView);

            iv_cat=itemView.findViewById(R.id.iv_cat);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_cat=itemView.findViewById(R.id.txt_cat);
            txt_qty=itemView.findViewById(R.id.txt_qty);
            txt_unit=itemView.findViewById(R.id.txt_unit);
            txt_price=itemView.findViewById(R.id.txt_price);
            checkbox=itemView.findViewById(R.id.checkbox);
            //notes=itemView.findViewById(R.id.txt_notes);

        }
    }

}
