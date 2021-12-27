package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ramuqaqavendor.other.API;

import org.json.JSONObject;

public class TermsActivity extends AppCompatActivity {
    TextView txt_terms;
    ImageView iv_back;
    LinearLayout ll_loading,ll_rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        txt_terms=findViewById(R.id.txt_terms);
        iv_back=findViewById(R.id.iv_back);
        ll_loading=findViewById(R.id.ll_loading);
        ll_rec=findViewById(R.id.ll_rec);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showTerms();


    }


    public void showTerms() {
        ll_loading.setVisibility(View.VISIBLE);
        ll_rec.setVisibility(View.GONE);
        AndroidNetworking.post(API.term_condition)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                ll_rec.setVisibility(View.VISIBLE);
                Log.e("bdfdfdff", response.toString());
                try {
                    txt_terms.setText(Html.fromHtml(response.getString("description")));

                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    ll_rec.setVisibility(View.GONE);
                    Log.e("fgfggffgfg", e.getMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
                ll_rec.setVisibility(View.GONE);
            }
        });
    }
}