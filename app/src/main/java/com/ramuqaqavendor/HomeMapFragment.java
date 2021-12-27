package com.ramuqaqavendor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.GPSTracker;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class HomeMapFragment extends Fragment implements OnMapReadyCallback{
ImageView logout,iv_menu,iv_noti;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    double lat, lng;
    LatLng latLng;
    RelativeLayout goOnline,goOffline;
    public static CircleImageView onlinebtn;
    String getUserId="",USERID="";
    Timer carousalTimer;
    TextView txt_mode,txt_location;
    LinearLayout ll_loading;

    /////////////////////////////////////////////

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean isLocationPermission = false;
    Location lastLocation;
   String OnlineStatus="";
    CameraPosition cameraPosition;

    //////////////////////////////////////////////

BroadcastReceiver mRegistrationBroadcastReceiver =new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("chchchchchchch", "onReceive: " );


        String result = intent.getStringExtra("title");


    }
};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);
        USERID=SharedHelper.getKey(getActivity(),APPCONSTANT.USERID);
        goOnline = view.findViewById(R.id.goOnline);
        goOffline = view.findViewById(R.id.goOffline);
        onlinebtn = view.findViewById(R.id.onlinebtn);
        iv_noti = view.findViewById(R.id.iv_noti);
        txt_mode = view.findViewById(R.id.txt_mode);
        txt_location = view.findViewById(R.id.txt_location);
        ll_loading = view.findViewById(R.id.ll_loading);

        getUserId = SharedHelper.getKey(getActivity(), APPCONSTANT.USERID);

        iv_noti=view.findViewById(R.id.iv_noti);
        iv_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NotificationActivity.class));
            }
        });


        OnlineStatus = SharedHelper.getKey(getActivity(), APPCONSTANT.ONLNESTATUS);
        Log.e("HomeMapFragment", "OnlineStatus: " +OnlineStatus);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        iv_menu=view.findViewById(R.id.iv_menu);

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                HomeActivity.drawer.openDrawer(Gravity.START);
            }
        });

        logout=view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle(getActivity().getResources().getString(R.string.app_name))
                        .setMessage("Are you sure you want to logout in the app")
                        .setPositiveButton(Html.fromHtml("<font color='#FF8C00'>Ok</font>"), new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(final DialogInterface dialog, int which) {

                                if (OnlineStatus.equals("0")){
                                    Toasty.error(getActivity(), "You are Online , if you want to exit in the app please offline your mode ", Toast.LENGTH_SHORT, true).show();

                                }else {
                                    Logout();
                                }




                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#FF8C00'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.logout)
                        .show();
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode== KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_UP){

                    Exit();

                    return  true;
                }
                return false;
            }
        });

        Log.e("sdfddadfadfa", OnlineStatus+"ddfdfdf" );
        if (getUserId.equals("")) {
            goOnline.setVisibility(View.GONE);
            goOffline.setVisibility(View.VISIBLE);
        }
        else {
            if (OnlineStatus.equals("0")){
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
                onlinebtn.setVisibility(View.VISIBLE);
                txt_mode.setText("you are online and accepting orders");
                goOnline.setBackgroundColor(getResources().getColor(R.color.red));
            }else   if (OnlineStatus.equals("1")){
                onlinebtn.setVisibility(View.GONE);
                goOnline.setVisibility(View.GONE);
                goOffline.setVisibility(View.VISIBLE);
                txt_mode.setText("you are offline and  not accepting orders");
                goOffline.setBackgroundColor(getResources().getColor(R.color.new_green));

            }else {
                OnlineStatus="0";
                goOnline.setVisibility(View.VISIBLE);
                goOffline.setVisibility(View.GONE);
                txt_mode.setText("you are online and accepting orders");
                goOnline.setBackgroundColor(getResources().getColor(R.color.red));
            }



        }

        goOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
                    Toasty.error(getActivity(), "Please on your location...", Toast.LENGTH_SHORT, true).show();
                else
                    onlineStatus(getUserId, "0");
               

            }
        });

        goOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MapFragment", "online: " +getUserId);
                // Toast.makeText(getActivity(), "checkOnline", Toast.LENGTH_SHORT).show();
               // getUserId = SharedHelper.getKey(getActivity(), Appconstant.UserID);
                onlineStatus(getUserId, "1");
            }
        });




        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById( R.id.frag_map );
        mapFragment.getMapAsync( this );

        gpsTracker = new GPSTracker(getActivity());
        lat = gpsTracker.getLatitude();
        lng = gpsTracker.getLongitude();
        Log.e("sdfdsv", lat + "");
        Log.e("sdfdsv", lng + "");


        return view;
    }



    public void Logout() {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_driver_status)
                .addBodyParameter("login_status", "1")
                .addBodyParameter("user_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            SharedHelper.putKey(getActivity(), APPCONSTANT.USERID,"");
                            Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                            if(Build.VERSION.SDK_INT >= 11) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            startActivity(intent);

                        }
                    } else {
                        Toasty.error(getActivity(), ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

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


    public void Exit(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to exit?");
        builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {

                if (OnlineStatus.equals("0")){
                    Toasty.error(getActivity(), "You are Online , if you want to exit in the app please offline your mode ", Toast.LENGTH_SHORT, true).show();

                }else {
                    getActivity().finish();
                }

            }
        }).setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        ////set direction circle marker bootom right on map
        mMap.setPadding(10, 650, 10, 10);


        //Place current location marker
        latLng = new LatLng(lat, lng);

        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();

        ////SHOW LOCATION OF MARKER AND COLOUR

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("Check"));
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
    }*/

    private void onlineStatus(String getUserId, String status){
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_driver_status)
                .addBodyParameter("user_id",getUserId)
                .addBodyParameter("login_status",status)
                .setTag("Accept Request")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ll_loading.setVisibility(View.GONE);
                        Log.e("dsfvgfd", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("successfully")){

                                SharedHelper.putKey(getActivity(), APPCONSTANT.ONLNESTATUS, response.getString("login_status"));

                                Log.e("hdfhd", response.getString("login_status"));

                                    OnlineStatus=status;
                                if (response.getString("login_status").equals("1")) {

                                    Toasty.success(getActivity(), "Offline.......", Toast.LENGTH_SHORT, true).show();

                                    goOffline.setVisibility(View.VISIBLE);
                                    goOnline.setVisibility(View.GONE);
                                    onlinebtn.setVisibility(View.GONE);
                                    txt_mode.setText("you are offline and  not accepting orders");
                                    goOffline.setBackgroundColor(getResources().getColor(R.color.new_green));

                                } else if(response.getString("login_status").equals("0")) {
                                    Toasty.success(getActivity(), "Online.......", Toast.LENGTH_SHORT, true).show();

                                    goOnline.setVisibility(View.VISIBLE);
                                    goOffline.setVisibility(View.GONE);
                                    onlinebtn.setVisibility(View.VISIBLE);
                                    txt_mode.setText("you are online and accepting orders");
                                    goOnline.setBackgroundColor(getResources().getColor(R.color.red));

                                }

                            }
                        } catch (JSONException e) {
                            Log.e("fdvfdv", "onResponse: " +e);
                            ll_loading.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdvfdv", "anError: " +anError);
                        ll_loading.setVisibility(View.GONE);
                    }
                });

    }





    private void updateLatLong(String latitude, String longitude){

        AndroidNetworking.post(API.update_profile)
                .addBodyParameter("user_id",getUserId)
                .addBodyParameter("latitude",latitude)
                .addBodyParameter("longitude",longitude)
                .addBodyParameter("address",longitude)
                .setTag("driver_online_offline")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tyuiyijuy", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("successfully")){
                              //  Toast.makeText(getActivity(), response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("hfgtjhfg", "onResponse: " +e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("hfgtjhfg", "anError: " +anError);
                    }
                });

    }

    public void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isLocationPermission = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermission = true;
            }
        }

        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (isLocationPermission) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void getDeviceLocation() {

        if (isLocationPermission) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();

                        if (lastLocation != null) {
                            lastLocation.getLatitude();
                            lastLocation.getLongitude();


                            carousalTimer = new Timer(); // At this line a new Thread will be created
                            carousalTimer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {


                                    Log.e("sdavsdvds", "check: " + lastLocation.getLatitude());
                                    Log.e("sdavsdvds", "check: " + lastLocation.getLongitude());



                                    updateLatLong(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));
                                 //   updateLocation(new LatLng(lastLocation.getLatitude(),(lastLocation.getLongitude())));
                                }
                            }, 0, 5 * 1000); // delay


                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 12));


                        } else {
                            LatLng latLng = new LatLng(33.8688, 151.2093);
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                }
            });


        }
    }



    private void updateLocation(final LatLng centerLatLng) {
        if (centerLatLng != null) {
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
               String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String knownName = addresses.get(0).getFeatureName();

                txt_location.setText(address);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}