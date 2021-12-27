package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chaos.view.PinView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ramuqaqavendor.adapter.NewOrderAdapter;
import com.ramuqaqavendor.adapter.OrderAdapter;
import com.ramuqaqavendor.model.OrderModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ViewOrderActivity extends AppCompatActivity {
    ImageView iv_back;
    RecyclerView rv_address;
    TextView txt_notes,txt_subtotal,txt_deliv,txt_total,txt_name,txt_email,txt_num,txt_address,txt_pickadd,txt_picked,txt_store_name,txt_confirm,txt_done,txt_dis,txt_earn,txt_rcv,star_rating,txt_reject,txt_riderdeliv;
    LinearLayout ll_loading,ll_pickupadd,ll_user_summary;
    String USERID="",ORDERID="",StrProId="",shopLat="",shopLng="",userLat="",userLng="",addressLat="",addressLng="";
    ArrayList<OrderModel>orderIdModels;
    CheckBox checkbox_all;
    BottomSheetBehavior sheetBehavior;
    PinView pinview;
    CoordinatorLayout rl_rec;
    public  static ArrayList<String> Arr_productId=new ArrayList<>();
    RatingBar rating_avg;
    RelativeLayout rl_picked,notes_rl;
    TextView txt_location,txt_picklocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);


        USERID= SharedHelper.getKey(ViewOrderActivity.this, APPCONSTANT.USERID);
        ORDERID= SharedHelper.getKey(ViewOrderActivity.this, APPCONSTANT.ORDERID);
        Log.e("dafadadfddf", USERID);
        Log.e("dafadadfddf", ORDERID);
        txt_subtotal=findViewById(R.id.txt_subtotal);
        txt_deliv=findViewById(R.id.txt_deliv);
        txt_total=findViewById(R.id.txt_total);
        iv_back=findViewById(R.id.iv_back);
        txt_notes=findViewById(R.id.txt_notes);
        notes_rl=findViewById(R.id.notes_rl);
        rv_address=findViewById(R.id.rv_address);
        ll_loading=findViewById(R.id.ll_loading);
        txt_name=findViewById(R.id.txt_name);
        txt_email=findViewById(R.id.txt_email);
        txt_num=findViewById(R.id.txt_num);
        txt_address=findViewById(R.id.txt_address);
        txt_pickadd=findViewById(R.id.txt_pickadd);
        txt_store_name=findViewById(R.id.txt_store_name);
        txt_confirm=findViewById(R.id.txt_confirm);
        checkbox_all=findViewById(R.id.checkbox_all);
        pinview=findViewById(R.id.pinview);
        rl_rec=findViewById(R.id.rl_rec);
        ll_pickupadd=findViewById(R.id.ll_pickupadd);
        ll_user_summary=findViewById(R.id.ll_user_summary);
        txt_dis=findViewById(R.id.txt_dis);
        txt_earn=findViewById(R.id.txt_earn);
        txt_rcv=findViewById(R.id.txt_rcv);
        rating_avg=findViewById(R.id.rating_avg);
        star_rating=findViewById(R.id.star_rating);
        txt_picked=findViewById(R.id.txt_picked);
        rl_picked=findViewById(R.id.rl_picked);
        txt_location=findViewById(R.id.txt_location);
        txt_picklocation=findViewById(R.id.txt_picklocation);
        txt_reject=findViewById(R.id.txt_reject);
        txt_riderdeliv=findViewById(R.id.txt_riderdeliv);



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showOrder();
    }



    public void updateStatus(String status) {
        ll_loading.setVisibility(View.VISIBLE);
        Log.e("ssdsdsdsdsd", StrProId);
        Log.e("ssdsdsdsdsd", ORDERID);
        Log.e("ssdsdsdsdsd", status);
        AndroidNetworking.post(API.update_order_status)
                .addBodyParameter("order_id",ORDERID)
                .addBodyParameter("status",status)
                .addBodyParameter("driver_id",USERID)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            Toasty.success(ViewOrderActivity.this, "successfully...", Toast.LENGTH_SHORT, true).show();
                            finish();
                            startActivity(new Intent(ViewOrderActivity.this,HomeActivity.class));
                        }else{
                            AlertDialog(""+response.getString("result"));
                        }
                    } else {

                        Toasty.error(ViewOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
                       // finish();
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
                Toasty.success(ViewOrderActivity.this, "successfully...", Toast.LENGTH_SHORT, true).show();
                finish();
                startActivity(new Intent(ViewOrderActivity.this,HomeActivity.class));
            }
        });
    }

    private  void AlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrderActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }
        );
        builder.show();
    }

    public void showOrder() {
        ll_loading.setVisibility(View.VISIBLE);
        rl_rec.setVisibility(View.GONE);
        txt_confirm.setVisibility(View.GONE);
        Log.e("sdasacssacsacsa", ORDERID);
        Log.e("sdasacssacsacsa", USERID);
        AndroidNetworking.post(API.show_order_details)
                .addBodyParameter("user_id",USERID)
                .addBodyParameter("order_id",ORDERID)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                ll_loading.setVisibility(View.GONE);
                rl_rec.setVisibility(View.VISIBLE);
                txt_confirm.setVisibility(View.VISIBLE);
                orderIdModels =new ArrayList<>();
                Log.e("bdfdfdff", response.toString());
                try {
                    for (int i = 0; i <response.length() ; i++) {

                        JSONObject jsonObject=response.getJSONObject(i);
                        shopLat=jsonObject.getString("shop_lat");
                        shopLng=jsonObject.getString("shop_long");
                        if (jsonObject.getString("delivery_type").equals("1")){
                            ll_pickupadd.setVisibility(View.VISIBLE);
                            txt_picklocation.setVisibility(View.VISIBLE);
                        }else {
                            ll_pickupadd.setVisibility(View.GONE);
                            txt_picklocation.setVisibility(View.GONE);
                        }

                        txt_dis.setText("Distance: "+jsonObject.getString("distance"));
                        txt_earn.setText("\u20B9"+jsonObject.getString("earning_amount"));
                        if (!jsonObject.getString("received_amount").equals("")){
                            txt_rcv.setText("\u20B9"+jsonObject.getString("received_amount"));
                        }else {
                            txt_rcv.setText("No amount recieved yet");
                        }
                        if (jsonObject.getString("rating_status").equals("0")){
                            rating_avg.setVisibility(View.GONE);
                            // txt_store_name.setVisibility(View.GONE);
                        }else if (jsonObject.getString("rating_status").equals("1")){
                            rating_avg.setVisibility(View.VISIBLE);
                            // txt_store_name.setVisibility(View.VISIBLE);

                            rating_avg.setIsIndicator(false);
                            rating_avg.setClickable(false);
                            rating_avg.setEnabled(false);
                            rating_avg.setOnTouchListener(new View.OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                            JSONArray jsonArray=new JSONArray(jsonObject.getString("rating_details"));
                            for (int j = 0; j <jsonArray.length() ; j++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(j);
                                String count=jsonObject1.getString("rating");
                                rating_avg.setRating(Float.parseFloat(String.valueOf(count)));
                                star_rating.setText("Your rating: "+"("+count+")");
                            }


                        }

                        OrderModel orderIdModel=new OrderModel();
                        orderIdModel.setID(jsonObject.getString("id"));
                        //  orderIdModel.setCatname(jsonObject.getString("uniqu_id"));
                        orderIdModel.setName(jsonObject.getString("name"));
                        orderIdModel.setQty(jsonObject.getString("quantity"));
                        orderIdModel.setUnit(jsonObject.getString("unit"));
                        orderIdModel.setPrice(jsonObject.getString("price"));
                        orderIdModel.setPickStatus(jsonObject.getString("picked_status"));
                        orderIdModel.setNotes(jsonObject.getString("notes"));
                        orderIdModels.add(orderIdModel);

                        if(!jsonObject.getString("notes").equals("")){
                            notes_rl.setVisibility(View.VISIBLE);
                            txt_notes.setText(jsonObject.getString("notes"));
                        }
                        txt_subtotal.setText("\u20B9"+jsonObject.getString("subtotal"));
                        txt_deliv.setText("\u20B9"+jsonObject.getString("delivery_charge"));
                        txt_riderdeliv.setText("\u20B9"+jsonObject.getString("rider_delivery_charge"));



                        txt_store_name.setText(jsonObject.getString("shop_name"));
                        txt_pickadd.setText(jsonObject.getString("shop_address"));

                        JSONArray jsonArray=new JSONArray(jsonObject.getString("user_details"));
                        for (int j = 0; j <jsonArray.length() ; j++) {

                            JSONObject jsonObject1=jsonArray.getJSONObject(j);
                            txt_name.setText(jsonObject1.getString("name"));
                            txt_email.setText(jsonObject1.getString("email"));
                            txt_num.setText(jsonObject1.getString("phone"));

                            userLat=jsonObject1.getString("latitude");
                            userLng=jsonObject1.getString("longitude");
                            txt_num.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+(jsonObject1.getString("phone"))));
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            txt_email.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        String[] recipients = new String[0];
                                        recipients = new String[]{jsonObject1.getString("email")};
                                        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...");
                                        intent.putExtra(Intent.EXTRA_TEXT, "Your Message here...");
                                        intent.putExtra(Intent.EXTRA_CC, jsonObject1.getString("email"));
                                        intent.setType("text/html");
                                        intent.setPackage("com.google.android.gm");
                                        startActivity(Intent.createChooser(intent, "Send mail"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        }

                        JSONArray jsonArray1=new JSONArray(jsonObject.getString("adress_details"));
                        for (int j = 0; j <jsonArray1.length() ; j++) {
                            JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                            txt_address.setText(jsonObject1.getString("address"));
                            addressLat=jsonObject1.getString("latitude");
                            addressLng=jsonObject1.getString("longitude");


                            updateLatLong(addressLat,addressLng,shopLat,shopLng);

                        }

                        if (jsonObject.getString("status").equals("0")){
                            rl_picked.setVisibility(View.GONE);
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Accept");
                            txt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateStatus("1");
                                }
                            });

                            txt_reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateStatus("2");
                                }
                            });
/*
                            tx.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateStatus("1");
                                }
                            });
                            */
                        }




                    }
                    rv_address.setHasFixedSize(true);
                    rv_address.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
                    rv_address.setAdapter(new NewOrderAdapter(orderIdModels,ViewOrderActivity.this));

                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("fgfggffgfg", e.getMessage());
                    rl_rec.setVisibility(View.GONE);
                    txt_confirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("fdgdffggffgg", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
                rl_rec.setVisibility(View.GONE);
                txt_confirm.setVisibility(View.GONE);
            }
        });
    }



    private void updateLatLong(String addressLat, String addressLng, String shopLat, String shopLng){
        Log.e("dssddsdddd", addressLat+"addressLat" );
        Log.e("dssddsdddd", addressLng+"addressLng" );
        Log.e("dssddsdddd", shopLat+"shopLat" );
        Log.e("dssddsdddd", shopLng+"shopLng" );
        AndroidNetworking.post(API.update_profile)
                .addBodyParameter("user_id",USERID)
                .setTag("driver_online_offline")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tyuiyijuy", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("successfully")){
                                txt_location.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = null;
                                        try {
                                            Log.e("dssddsdddd", response.getString("latitude")+"addressLat" );
                                            Log.e("dssddsdddd", response.getString("longitude")+"addressLat" );
                                            Log.e("dssddsdddd", addressLat+"addressLat2" );
                                            Log.e("dssddsdddd", addressLng+"addressLng2" );
                                            intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://maps.google.com/maps?saddr="+response.getString("latitude")+","+response.getString("longitude")+"&"+"daddr="+addressLat +","+addressLng));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                txt_picklocation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.e("dssddsdddd", response.getString("latitude")+"addressLat" );
                                            Log.e("dssddsdddd", response.getString("longitude")+"addressLat" );
                                            Log.e("dssddsdddd", shopLat+"shopLat" );
                                            Log.e("dssddsdddd", shopLng+"shopLng" );
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse("http://maps.google.com/maps?saddr=" + response.getString("latitude") + "," + response.getString("longitude") + "&" + "daddr=" + shopLat+ "," +shopLng));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(ViewOrderActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
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
}