package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.ramuqaqavendor.other.API;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ContactUsActivity extends AppCompatActivity {
    ImageView iv_back;
    TextInputEditText edit_name, edit_email, edit_sub,edit_message,edit_phone;
    TextView txt_signup;
    LinearLayout ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        iv_back = findViewById(R.id.iv_back);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_sub = findViewById(R.id.edit_sub);
        edit_message = findViewById(R.id.edit_message);
        edit_phone = findViewById(R.id.edit_phone);
        txt_signup = findViewById(R.id.txt_signup);
        ll_loading = findViewById(R.id.ll_loading);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edit_name.getText().toString().trim();
                String strEmail = edit_email.getText().toString().trim();
                String strSub = edit_sub.getText().toString().trim();
                String strPhone = edit_phone.getText().toString().trim();
                String strMessage = edit_message.getText().toString().trim();

                if (strName.equals("")) {
                    Toasty.error(ContactUsActivity.this, "Please enter name ......", Toast.LENGTH_SHORT, true).show();
                } else if (strEmail.equals("")) {
                    Toasty.error(ContactUsActivity.this, "Please enter email-address......", Toast.LENGTH_SHORT, true).show();
                }else if (strPhone.equals("")) {
                    Toasty.error(ContactUsActivity.this, "Please enter phone number......", Toast.LENGTH_SHORT, true).show();
                } else if (strSub.equals("")) {
                    Toasty.error(ContactUsActivity.this, "Please enter subject ......", Toast.LENGTH_SHORT, true).show();
                } else if (strMessage.equals("")) {
                    Toasty.error(ContactUsActivity.this, "Please enter your message ......", Toast.LENGTH_SHORT, true).show();
                } else {
                    contactUs(strName, strEmail, strSub, strMessage,strPhone);
                }

            }
        });

    }


    public void contactUs(String strName, String strEmail, String strSub, String strMessage, String phone) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.contact_us)
                .addBodyParameter("name", strName)
                .addBodyParameter("email", strEmail)
                .addBodyParameter("subject", strSub)
                .addBodyParameter("messages", strMessage)
                .addBodyParameter("phone", phone)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("bdfdfdff", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successful")) {
                            Toasty.success(ContactUsActivity.this, "Send  Successfully ......", Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(ContactUsActivity.this, HomeActivity.class));
                            finish();

                        }
                    } else {
                        Toasty.error(ContactUsActivity.this, "" + response.getString("result"), Toast.LENGTH_SHORT, true).show();


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