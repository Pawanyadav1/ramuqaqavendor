package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ramuqaqavendor.adapter.EariningOrderAdapter;
import com.ramuqaqavendor.adapter.OrderIDAdapter;
import com.ramuqaqavendor.model.OrderIdModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EarningActivity extends AppCompatActivity {
TextView txt_earings,txt_no;
ImageView iv_back_new;
String USERID="";
RecyclerView rv_earns;
    ArrayList<OrderIdModel> orderIdModels;
    LinearLayout ll_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        USERID= SharedHelper.getKey(EarningActivity.this, APPCONSTANT.USERID);
        txt_earings=findViewById(R.id.txt_earings);
        rv_earns=findViewById(R.id.rv_earns);
        ll_loading=findViewById(R.id.ll_loading);
        txt_no=findViewById(R.id.txt_no);
        iv_back_new=findViewById(R.id.iv_back_new);

        updateProfile();
        showOrder();

        iv_back_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public void updateProfile() {
        AndroidNetworking.post(API.update_profile)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getString("total_earning").equals("")){
                        txt_earings.setVisibility(View.VISIBLE);
                    }else {
                        txt_earings.setVisibility(View.GONE);
                    }
                    txt_earings.setText("Earnings: "+"\u20B9"+response.getString("total_earning"));
                } catch (Exception e) {
                    e.printStackTrace();



                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
            }
        });
    }



    public void showOrder() {
        ll_loading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(API.show_order)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                ll_loading.setVisibility(View.GONE);
                orderIdModels=new ArrayList<>();
                Log.e("bdfdfdff", response.toString());
                try {
                    for (int i = 0; i <response.length() ; i++) {

                        JSONObject jsonObject=response.getJSONObject(i);

                        if (jsonObject.getString("status").equals("3") ){
                            OrderIdModel orderIdModel=new OrderIdModel();
                            orderIdModel.setID(jsonObject.getString("id"));
                            orderIdModel.setUniqID(jsonObject.getString("uniqu_id"));
                            orderIdModel.setDate(jsonObject.getString("dates"));
                            orderIdModel.setPrice(jsonObject.getString("earning_amount"));
                            orderIdModels.add(orderIdModel);

                        }else {

                        }

                    }
                    rv_earns.setHasFixedSize(true);
                    rv_earns.setLayoutManager(new LinearLayoutManager(EarningActivity.this));
                    rv_earns.setAdapter(new EariningOrderAdapter(orderIdModels,EarningActivity.this));


                    if (orderIdModels.size() > 0) {
                        txt_no.setVisibility(View.GONE);
                       // txt_earings.setVisibility(View.VISIBLE);
                    } else {
                        txt_no.setVisibility(View.VISIBLE);
                      ///  txt_earings.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("fgfggffgfg", e.getMessage());

                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
                ll_loading.setVisibility(View.GONE);

            }
        });
    }
}