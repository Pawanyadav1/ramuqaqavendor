package com.ramuqaqavendor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.ramuqaqavendor.adapter.OrderAdapter;
import com.ramuqaqavendor.model.OrderModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountFrag extends Fragment {
ImageView iv_image,iv_front,iv_back,iv_adhar,ivback;
TextView txt_name,txt_email,txt_num,txt_gender,txt_dob, txt_riderId;
LinearLayout ll_loading;
String USERID="";
ScrollView scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        USERID= SharedHelper.getKey(getActivity(), APPCONSTANT.USERID);
        iv_image=view.findViewById(R.id.iv_image);
        iv_front=view.findViewById(R.id.iv_front);
        iv_back=view.findViewById(R.id.iv_back);
        iv_adhar=view.findViewById(R.id.iv_adhar);
        txt_name=view.findViewById(R.id.txt_name);
        txt_riderId=view.findViewById(R.id.txt_rider_id);
        txt_email=view.findViewById(R.id.txt_email);
        txt_num=view.findViewById(R.id.txt_num);
        txt_gender=view.findViewById(R.id.txt_gender);
        txt_dob=view.findViewById(R.id.txt_dob);
        ll_loading=view.findViewById(R.id.ll_loading);
        scroll=view.findViewById(R.id.scroll);
        ivback=view.findViewById(R.id.ivback);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getActivity(),HomeActivity.class));
            }
        });

        updateProfile();
        return view;
    }

    public void updateProfile() {
        ll_loading.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.GONE);
        AndroidNetworking.post(API.update_profile)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
                Log.e("bdfdfdff", response.toString());
                try {
                    Glide.with(getActivity()).load(response.getString("path")+response.getString("image")).into(iv_image);
                    Glide.with(getActivity()).load(response.getString("path")+response.getString("driving_liscsence_front")).into(iv_front);
                    Glide.with(getActivity()).load(response.getString("path")+response.getString("driving_liscsence_back")).into(iv_back);
                    Glide.with(getActivity()).load(response.getString("path")+response.getString("adhar_image")).into(iv_adhar);
                    txt_name.setText(response.getString("name"));
                    txt_riderId.setText("Rider ID: "+response.getString("specific_id"));
                    txt_email.setText(response.getString("email"));
                    txt_num.setText(response.getString("phone"));
                    txt_gender.setText(response.getString("gender"));
                    txt_dob.setText(response.getString("dob"));


                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("fgfggffgfg", e.getMessage());
                    scroll.setVisibility(View.GONE);


                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);

            }
        });
    }
}