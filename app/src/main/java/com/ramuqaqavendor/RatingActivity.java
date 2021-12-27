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
import com.ramuqaqavendor.adapter.OrderIDAdapter;
import com.ramuqaqavendor.adapter.RatingAdapter;
import com.ramuqaqavendor.model.OrderIdModel;
import com.ramuqaqavendor.model.ShowUserPojo;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
RecyclerView rv_rating;
ImageView iv_back;
ArrayList<ShowUserPojo>showUserPojos;
TextView txt_no;
LinearLayout ll_loading;
String USERID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        USERID= SharedHelper.getKey(RatingActivity.this, APPCONSTANT.USERID);

        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv_rating=findViewById(R.id.rv_rating);
        txt_no=findViewById(R.id.txt_no);
        ll_loading=findViewById(R.id.ll_loading);

        showrating();
    }


    public void showrating() {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.show_driver_rating)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                ll_loading.setVisibility(View.GONE);
                showUserPojos=new ArrayList<>();
                Log.e("bdfdfdff", response.toString());
                try {
                    for (int i = 0; i <response.length() ; i++) {

                        JSONObject jsonObject=response.getJSONObject(i);
                        ShowUserPojo userPojo=new ShowUserPojo();

                        userPojo.setRating(jsonObject.getString("rating"));
                        userPojo.setComment(jsonObject.getString("comment"));
                        userPojo.setDate(jsonObject.getString("dates"));
                        JSONArray jsonArray=new JSONArray(jsonObject.getString("user_details"));
                        for (int j = 0; j <jsonArray.length() ; j++) {
                            JSONObject jsonObject1=jsonArray.getJSONObject(j);
                            userPojo.setId(jsonObject1.getString("id"));
                            userPojo.setName(jsonObject1.getString("name"));
                           // userPojo.setPath(jsonObject1.getString("total_amount"));
                            userPojo.setImage(jsonObject1.getString("image"));

                        }
                        showUserPojos.add(userPojo);

                    }
                    rv_rating.setHasFixedSize(true);
                    rv_rating.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
                    rv_rating.setAdapter(new RatingAdapter(showUserPojos,RatingActivity.this));


                    if (showUserPojos.size() > 0) {
                        txt_no.setVisibility(View.GONE);
                    } else {
                        txt_no.setVisibility(View.VISIBLE);
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