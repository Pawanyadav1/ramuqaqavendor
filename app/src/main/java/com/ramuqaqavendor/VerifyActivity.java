package com.ramuqaqavendor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chaos.view.PinView;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;


import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class VerifyActivity extends AppCompatActivity {
LinearLayout ll_loading;
TextView txt_verify,txt_rsn_otp;
PinView otpview;
String NUMBER="",REGID="",USERID="",OTP="";
ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
         NUMBER= SharedHelper.getKey(VerifyActivity.this, APPCONSTANT.NUMBER);
        OTP=SharedHelper.getKey(VerifyActivity.this,APPCONSTANT.OTP);
        ll_loading=findViewById(R.id.ll_loading);
        txt_verify=findViewById(R.id.txt_verify);
        txt_rsn_otp=findViewById(R.id.txt_rsn_otp);
        otpview=findViewById(R.id.otpview);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strpview=otpview.getText().toString().trim();
                if (strpview.equals("")){

                    Toasty.error(VerifyActivity.this, "Please enter otp......", Toast.LENGTH_SHORT, true).show();

                }else if (!strpview.equals(OTP)){
                    Toasty.error(VerifyActivity.this, "OTP doesn't matched......", Toast.LENGTH_SHORT, true).show();

                }else {
                    Verify(strpview)  ;
                }
            }
        });

        txt_rsn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });



    }



    public void Verify(String otp) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.otp_verfication)
                .addBodyParameter("mobile", NUMBER)
                .addBodyParameter("otp",otp)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("Login Successful")) {
                            USERID= response.getString("id");
                            Toasty.success(VerifyActivity.this, "Successfully ......", Toast.LENGTH_SHORT, true).show();
                            SharedHelper.putKey(VerifyActivity.this, APPCONSTANT.USERID,USERID);
                            SharedHelper.putKey(VerifyActivity.this, APPCONSTANT.ONLNESTATUS,"1");
                            startActivity(new Intent(VerifyActivity.this, HomeActivity.class));
                            finish();

                        }else {
                            Toasty.error(VerifyActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

                        }
                    } else {
                        Toasty.error(VerifyActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

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


    public void login() {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.signup)
                .addBodyParameter("mobile", NUMBER)
                .addBodyParameter("type","0")
                .addBodyParameter("regid",REGID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successful")) {
                            String id = response.getString("id");
                            String mobile = response.getString("phone");
                            Toasty.success(VerifyActivity.this, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                            SharedHelper.putKey(VerifyActivity.this, APPCONSTANT.NUMBER,mobile);
                            SharedHelper.putKey(VerifyActivity.this, APPCONSTANT.REGID,REGID);

                        }
                    } else {
                        Toasty.error(VerifyActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
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



    public void Logout() {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_driver_status)
                .addBodyParameter("login_status", "0")
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            startActivity(new Intent(VerifyActivity.this, HomeActivity.class));
                            finish();
                        }
                    } else {
                        Toasty.error(VerifyActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

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