package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ramuqaqavendor.adapter.NotiAdapter;
import com.ramuqaqavendor.model.NotiModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
RecyclerView rv_noti;
ImageView iv_back;
ArrayList<NotiModel>notiModels;
public  static  LinearLayout ll_loading;
String USERID="";
TextView txt_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
         USERID= SharedHelper.getKey(NotificationActivity.this, APPCONSTANT.USERID);
        Log.e("sdfddfdfdfd", USERID);
        rv_noti=findViewById(R.id.rv_noti);
        ll_loading=findViewById(R.id.ll_loading);
        iv_back=findViewById(R.id.iv_back);
        txt_no=findViewById(R.id.txt_no);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ShowNoti();
    }

    public void ShowNoti() {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.show_notification)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                notiModels=new ArrayList<>();
                Log.e("htdhjf", response.toString());
                try {
                    JSONArray jsonArray=new JSONArray(response.getString("data"));
                    for (int i = 0; i <jsonArray.length() ; i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        NotiModel notiModel=new NotiModel();
                        notiModel.setNotiId(jsonObject.getString("id"));
                        notiModel.setNotiTitle(jsonObject.getString("title"));
                        notiModel.setNotiMsg(jsonObject.getString("description"));
                        notiModel.setDate(jsonObject.getString("date"));

                        JSONArray jsonArray1=new JSONArray(jsonObject.getString("order_details"));
                        for (int j = 0; j <jsonArray1.length() ; j++) {
                            JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                            notiModel.setOrderId(jsonObject1.getString("uniqu_id"));
                            notiModel.setStatus(jsonObject1.getString("status"));

                        }
                       notiModels.add(notiModel);

                    }
                    rv_noti.setHasFixedSize(true);
                    rv_noti.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                    rv_noti.setAdapter(new NotiAdapter(notiModels,NotificationActivity.this));


                    if (notiModels.size() > 0) {
                        txt_no.setVisibility(View.GONE);
                    } else {
                        txt_no.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("rdchbvbvsv", e.getMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("rdchbvbvsv", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
            }
        });
    }
}