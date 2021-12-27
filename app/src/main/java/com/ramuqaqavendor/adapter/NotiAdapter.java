package com.ramuqaqavendor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ramuqaqavendor.MainActivity;
import com.ramuqaqavendor.MyOrderActivity;
import com.ramuqaqavendor.NotificationActivity;
import com.ramuqaqavendor.R;
import com.ramuqaqavendor.ViewOrderActivity;
import com.ramuqaqavendor.model.NotiModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder> {
    Context context;
    ArrayList<NotiModel> notiModels;
    String USERID="";

    public NotiAdapter(ArrayList<NotiModel> notiModels, Context context) {
        this.context = context;
        this.notiModels = notiModels;
    }
    @Override
    public NotiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_list, parent, false);
        NotiAdapter.ViewHolder viewHolder = new NotiAdapter.ViewHolder(view);
        USERID= SharedHelper.getKey(context, APPCONSTANT.USERID);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final NotiAdapter.ViewHolder viewHolder, final int position) {

        final NotiModel data=notiModels.get(position);

        viewHolder.tittle.setText(data.getNotiTitle());
        viewHolder.message.setText(data.getNotiMsg());
        viewHolder.timedate.setText(data.getDate());

        if (data.getStatus().equals("0")){
            viewHolder.ll_bottom.setVisibility(View.VISIBLE);
            viewHolder.txt_view.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ll_bottom.setVisibility(View.GONE);
            viewHolder.txt_view.setVisibility(View.VISIBLE);
        }

        viewHolder.txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("1",data,data.getOrderId());
            }
        });

        viewHolder.txt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("2",data,data.getOrderId());
            }
        });


        viewHolder.txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(context, APPCONSTANT.ORDERID,data.getOrderId());
                ((Activity)context).finish();
                context.startActivity(new Intent(context, ViewOrderActivity.class));
            }
        });

    }
    @Override
    public int getItemCount() {
        return notiModels.size();
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

        TextView tittle,message,timedate,txt_view,txt_confirm,txt_reject;
        LinearLayout ll_bottom;

        public ViewHolder(View itemView){
            super(itemView);

            tittle=itemView.findViewById(R.id.tittle);
            message=itemView.findViewById(R.id.message);
            timedate=itemView.findViewById(R.id.timedate);
            ll_bottom=itemView.findViewById(R.id.ll_bottom);
            txt_view=itemView.findViewById(R.id.txt_view);
            txt_confirm=itemView.findViewById(R.id.txt_confirm);
            txt_reject=itemView.findViewById(R.id.txt_reject);

        }
    }


    public void update(String status, NotiModel data,String orderId) {
        NotificationActivity.ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_order_status)
                .addBodyParameter("driver_id", USERID)
                .addBodyParameter("order_id",orderId)
                .addBodyParameter("status",status)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                NotificationActivity. ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            Toasty.success(context, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                            SharedHelper.putKey(context, APPCONSTANT.ORDERID,data.getOrderId());
                            ((Activity)context).finish();
                            context.startActivity(new Intent(context, MyOrderActivity.class));
                        }else{
                            AlertDialog(""+response.getString("result"));
                        }
                    } else {
                        Toasty.error(context, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NotificationActivity. ll_loading.setVisibility(View.GONE);
                    Log.e("rdchbvbvsv", e.getMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("rdchbvbvsv", anError.getMessage());
                NotificationActivity. ll_loading.setVisibility(View.GONE);
                Toasty.success(context, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                SharedHelper.putKey(context, APPCONSTANT.ORDERID,data.getOrderId());
                ((Activity)context).finish();
                context.startActivity(new Intent(context, MyOrderActivity.class));
            }
        });
    }

    private  void AlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        ((Activity)context).finish();
                    }
                }
        );
        builder.show();
    }

}
