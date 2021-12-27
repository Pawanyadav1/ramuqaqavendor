package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ramuqaqavendor.internetConnection.IinternetConnectionInterface;
import com.ramuqaqavendor.internetConnection.InternetConnectivity;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
String USERID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNetconnection();
        USERID= SharedHelper.getKey(MainActivity.this, APPCONSTANT.USERID);
        Log.e("dfdsdsffaafd", USERID);


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                )

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        new Handler(Looper.myLooper()).postDelayed(() -> {
                            if (USERID.equals("")){
                                startActivity(new Intent(MainActivity.this, SelectTypeActivity.class));
                                finish();
                            }else {
                                showProfile();

                            }
                        }, 2000);


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();


    }


    public void showProfile() {
        AndroidNetworking.post(API.check_user)
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("User not found")){
                        Logout();
                    }else {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }

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



    public void Logout() {
        AndroidNetworking.post(API.update_driver_status)
                .addBodyParameter("login_status", "1")
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            SharedHelper.putKey(MainActivity.this, APPCONSTANT.USERID,"");
                            Intent intent = new Intent(MainActivity.this, SelectTypeActivity.class);
                            if(Build.VERSION.SDK_INT >= 11) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            startActivity(intent);

                        }
                    } else {
                        Toasty.error(MainActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("rdchbvbvsv", anError.getMessage());

            }
        });
    }
    public void checkNetconnection(){
        IinternetConnectionInterface connectivity = new InternetConnectivity();
        if (!connectivity.isConnected(getApplicationContext())) {
            AlertDialog();
        }
    }

    private  void AlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Please check your internet connectivity");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();

                    }
                }
        );
        builder.show();
    }
}