package com.ramuqaqavendor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;


import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
TextView txt_signup,txt_login;
    ImageView iv_back;
    LinearLayout ll_loading;
    TextInputEditText editemail,edit_pass;
    String REGID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        REGID=SharedHelper.getKey(LoginActivity.this,APPCONSTANT.REGID);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    REGID= task.getResult();
                    Log.e("saaddasadsdas", REGID+"dhfd" );
                    SharedHelper.putKey(LoginActivity.this, APPCONSTANT.REGID,REGID);
                }

            }
        });

        iv_back=findViewById(R.id.iv_back);
        txt_signup=findViewById(R.id.txt_signup);
        txt_login=findViewById(R.id.txt_login);
        ll_loading=findViewById(R.id.ll_loading);
        editemail=findViewById(R.id.editemail);
        edit_pass=findViewById(R.id.edit_pass);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= editemail.getText().toString().trim();
              //  String pass= edit_pass.getText().toString().trim();
                if (email.equals("")){
                    Toasty.error(LoginActivity.this, "Please enter mobile number......", Toast.LENGTH_SHORT, true).show();
                }else {
                    if (email.length()==10){
                        login(email);
                    }else {
                        Toasty.error(LoginActivity.this, "Please enter atleast 10 digit mobile number..", Toast.LENGTH_SHORT, true).show();

                    }

                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }

    public void login(String email) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.signup)
                .addBodyParameter("mobile", email)
                .addBodyParameter("type","1")
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
                            String login_status = response.getString("login_status");
                            Toasty.success(LoginActivity.this, "Successfully ......", Toast.LENGTH_SHORT, true).show();
                            SharedHelper.putKey(LoginActivity.this, APPCONSTANT.NUMBER,mobile);
                            SharedHelper.putKey(LoginActivity.this, APPCONSTANT.REGID,REGID);
                            SharedHelper.putKey(LoginActivity.this, APPCONSTANT.ONLNESTATUS,login_status);
                            SharedHelper.putKey(LoginActivity.this, APPCONSTANT.OTP, response.getString("otp"));
                            startActivity(new Intent(LoginActivity.this, VerifyActivity.class));
                            finish();

                        }else  if (response.getString("result").equals("Your account is not approved by admin")) {
                            Toasty.error(LoginActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();


                        }else  if (response.getString("result").equals("user not registered")) {
                            Toasty.error(LoginActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();


                        }else {
                            Toasty.error(LoginActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

                        }
                    } else {
                        Toasty.error(LoginActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
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