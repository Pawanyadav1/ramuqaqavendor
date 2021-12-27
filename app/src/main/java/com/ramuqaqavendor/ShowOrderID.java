package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ramuqaqavendor.adapter.OrderIDAdapter;
import com.ramuqaqavendor.model.OrderIdModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowOrderID extends AppCompatActivity {
    ImageView iv_back;
    RecyclerView rv_address;
    LinearLayout ll_loading;
    ArrayList<OrderIdModel>orderIdModels;
    String USERID="";
    RelativeLayout rl_rec;
    TextView txt_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_i_d);
        USERID= SharedHelper.getKey(ShowOrderID.this, APPCONSTANT.USERID);
        iv_back=findViewById(R.id.iv_back);
        rv_address=findViewById(R.id.rv_address);
        ll_loading=findViewById(R.id.ll_loading);
        rl_rec=findViewById(R.id.rl_rec);
        txt_no=findViewById(R.id.txt_no);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ShowOrderID.this,HomeActivity.class));
            }
        });
        showOrder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrder();
    }

    public void showOrder() {
        ll_loading.setVisibility(View.VISIBLE);
        rl_rec.setVisibility(View.GONE);
        AndroidNetworking.post(API.show_order)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                ll_loading.setVisibility(View.GONE);
                rl_rec.setVisibility(View.VISIBLE);
                orderIdModels=new ArrayList<>();
                Log.e("bdfdfdff", response.toString());
                try {
                    for (int i = 0; i <response.length() ; i++) {

                        JSONObject jsonObject=response.getJSONObject(i);
                        OrderIdModel orderIdModel=new OrderIdModel();
                        orderIdModel.setID(jsonObject.getString("id"));
                        orderIdModel.setUniqID(jsonObject.getString("uniqu_id"));
                        orderIdModel.setDate(jsonObject.getString("dates"));
                        orderIdModel.setPrice(jsonObject.getString("total_amount_price"));
                        orderIdModel.setDelievery(jsonObject.getString("delivery_charge"));
                        orderIdModel.setStatus(jsonObject.getString("status"));
                        orderIdModels.add(orderIdModel);

                    }
                    rv_address.setHasFixedSize(true);
                    rv_address.setLayoutManager(new LinearLayoutManager(ShowOrderID.this));
                    rv_address.setAdapter(new OrderIDAdapter(orderIdModels,ShowOrderID.this));


                    if (orderIdModels.size() > 0) {
                        txt_no.setVisibility(View.GONE);
                    } else {
                        txt_no.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("fgfggffgfg", e.getMessage());
                    rl_rec.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
                rl_rec.setVisibility(View.GONE);
            }
        });
    }
}