package com.ramuqaqavendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.chaos.view.PinView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ramuqaqavendor.adapter.OrderAdapter;
import com.ramuqaqavendor.model.OrderModel;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MyOrderActivity extends AppCompatActivity {
    //global declaration
    private TextView timeUpdate;
    Calendar calendar;


    ImageView iv_back;
    RecyclerView rv_address;
    TextView text_date,txt_notes,txt_subtotal,txt_deliv,txt_total,txt_name,txt_email,txt_num,txt_address,txt_pickadd,txt_picked,txt_store_name,txt_confirm,txt_done,txt_dis,txt_earn,txt_rcv,star_rating,txt_riderdeliv, txt_reverttotal;
    LinearLayout ll_loading,ll_pickupadd,ll_user_summary;
    String USERID="",ORDERID="",StrProId="",shopLat="",shopLng="",userLat="",userLng="",addressLat="",addressLng="";
    ArrayList<OrderModel>orderIdModels;
    CheckBox checkbox_all;
    BottomSheetBehavior sheetBehavior;
    PinView pinview;
    CoordinatorLayout rl_rec;
    public  static  ArrayList<String>Arr_productId=new ArrayList<>();
    RatingBar rating_avg;
    RelativeLayout rl_picked,notes_rl;
    TextView txt_location,txt_picklocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        USERID= SharedHelper.getKey(MyOrderActivity.this, APPCONSTANT.USERID);
        ORDERID= SharedHelper.getKey(MyOrderActivity.this, APPCONSTANT.ORDERID);
        Log.e("dafadadfddf", USERID);
        Log.e("dafadadfddf", ORDERID);
        txt_subtotal=findViewById(R.id.txt_subtotal);
        txt_deliv=findViewById(R.id.txt_deliv);
        txt_total=findViewById(R.id.txt_total);
        text_date=findViewById(R.id.text_date);
       // iv_back=findViewById(R.id.iv_back);
        rv_address=findViewById(R.id.rv_address);
        txt_notes=findViewById(R.id.txt_notes);
        notes_rl=findViewById(R.id.notes_rl);
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
        txt_done=findViewById(R.id.txt_done);
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
        txt_riderdeliv=findViewById(R.id.txt_riderdeliv);
        txt_reverttotal=findViewById(R.id.txt_reverttotal);

        RelativeLayout layoutBottomSheet =findViewById(R.id.bottomsheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

       /* iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        */
        showOrder();



        //in onStart()
        calendar = Calendar.getInstance();
//date format is:  "Date-Month-Year Hour:Minutes am/pm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a"); //Date and time
        String currentDate = sdf.format(calendar.getTime());

//Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);
        text_date.setText(" " + currentDate + "");





}


    @Override
    protected void onResume() {
        super.onResume();
        showOrder();

    }



    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            // btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //btnBottomSheet.setText("Expand sheet");
        }


    }


    public void completeupdate(String status,String code) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_order_status)
                .addBodyParameter("driver_id", USERID)
                .addBodyParameter("order_id",ORDERID)
                .addBodyParameter("status",status)
                .addBodyParameter("verify_code",code)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            Toasty.success(MyOrderActivity.this, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                            deliever("3");


                        }else {
                            Toasty.error(MyOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

                        }
                    } else {
                        Toasty.error(MyOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
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
    public void deliever(String status) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.update_order_status)
                .addBodyParameter("driver_id", USERID)
                .addBodyParameter("order_id",ORDERID)
                .addBodyParameter("status",status)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            Toasty.success(MyOrderActivity.this, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                            finish();
                            startActivity(new Intent(MyOrderActivity.this,MyOrderActivity.class));


                        }else {
                            Toasty.error(MyOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(MyOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
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
               // Toasty.success(MyOrderActivity.this, "Order Completed Successfully ......", Toast.LENGTH_SHORT, true).show();
                finish();
                startActivity(new Intent(MyOrderActivity.this,MyOrderActivity.class));
            }
        });
    }






    public void updateOrderPick(String status) {
        ll_loading.setVisibility(View.VISIBLE);
        Log.e("ssdsdsdsdsd", StrProId);
        Log.e("ssdsdsdsdsd", ORDERID);
        Log.e("ssdsdsdsdsd", status);
        AndroidNetworking.post(API.order_picked)
                .addBodyParameter("order_id",ORDERID)
                .addBodyParameter("status","4")
                .addBodyParameter("product_id",StrProId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("htdhjf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successfully")) {
                            Toasty.success(MyOrderActivity.this, " Successfully ......", Toast.LENGTH_SHORT, true).show();
                            showOrder();
                        }
                    } else {
                        Toasty.error(MyOrderActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
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
                        orderIdModel.setUnit(jsonObject.getString("unit_name"));
                        orderIdModel.setPrice(jsonObject.getString("price"));
                        orderIdModel.setStatus(jsonObject.getString("status"));
                        orderIdModel.setPickStatus(jsonObject.getString("picked_status"));
                        orderIdModels.add(orderIdModel);
                        if(!jsonObject.getString("notes").equals("")){
                            notes_rl.setVisibility(View.VISIBLE);
                            txt_notes.setText(jsonObject.getString("notes"));
                        }
                        txt_subtotal.setText("\u20B9"+jsonObject.getString("subtotal"));
                        txt_deliv.setText("\u20B9"+jsonObject.getString("delivery_charge"));
                        txt_riderdeliv.setText("\u20B9"+jsonObject.getString("rider_delivery_charge"));

                        if(jsonObject.getString("received_amount").equals("")){
                           txt_reverttotal.setText("\u20B9"+"0");

                            }else{
                                txt_reverttotal.setText("\u20B9"+jsonObject.getString("received_amount"));
                            }

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

                        if (jsonObject.getString("status").equals("1")){
                            rl_picked.setVisibility(View.GONE);
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }

                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Picked items");
                            txt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int j = 0; j < Arr_productId.size(); j++) {
                                        if (StrProId.equals("")) {
                                            StrProId = Arr_productId.get(j);
                                        } else {
                                            StrProId = StrProId + "," +Arr_productId.get(j);
                                            Log.e("gddjkcd", StrProId);

                                        }
                                    }
                                    if (StrProId.equals("")){
                                        Toasty.error(MyOrderActivity.this, "Please select picked items....", Toast.LENGTH_SHORT, true).show();

                                    }else {
                                        updateOrderPick("4");

                                    }
                                }
                            });


                        }else if (jsonObject.getString("status").equals("5")){
                            rl_picked.setVisibility(View.VISIBLE);
                            String strAmount=jsonObject.getString("delivery_charge");
                            Log.e("daddddfdfdfdffd", strAmount+"hdgfd" );
                            Log.e("daddddfdfdfdffd", jsonObject.getString("picked_amount")+"hdgfd" );
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            if (strAmount.equals("")){
                                txt_picked.setText("\u20B9"+jsonObject.getString("picked_amount"));

                            }else {
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("picked_amount"));
                                txt_picked.setText("\u20B9"+pickedamount);

                            }

                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Click here to complete Order");
                            txt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    deliever("3");
                                }
                            });
                        }else if (jsonObject.getString("status").equals("4")){
                            rl_picked.setVisibility(View.VISIBLE);
                            String strAmount=jsonObject.getString("delivery_charge");
                            Log.e("daddddfdfdfdffd", strAmount+"hdgfd" );
                            Log.e("daddddfdfdfdffd", jsonObject.getString("picked_amount")+"hdgfd" );
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            if (strAmount.equals("")){
                                txt_picked.setText("\u20B9"+jsonObject.getString("picked_amount"));

                            }else {
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("picked_amount"));
                                txt_picked.setText("\u20B9"+pickedamount);

                            }

                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Click here to complete dilevery");
                            txt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    txt_confirm.setVisibility(View.GONE);
                                    toggleBottomSheet();

                                }
                            });
                            txt_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String code=pinview.getText().toString().trim();
                                    if (code.equals("")){
                                        Toasty.error(MyOrderActivity.this, "Please enter your code....", Toast.LENGTH_SHORT, true).show();

                                    }else {
                                        completeupdate("5",code);
                                    }

                                }
                            });
                        }else if (jsonObject.getString("status").equals("6")){
                            rl_picked.setVisibility(View.GONE);
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Order cancelled");
                        }else if (jsonObject.getString("status").equals("2")){
                            rl_picked.setVisibility(View.GONE);
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Rejected");
                        }else if (jsonObject.getString("status").equals("3")){
                            rl_picked.setVisibility(View.GONE);
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            ll_user_summary.setVisibility(View.GONE);
                            txt_confirm.setText("Completed & delivered");
                        }else {
                            ll_user_summary.setVisibility(View.VISIBLE);
                            txt_confirm.setText("Order Pick");
                            if (!jsonObject.getString("delivery_charge").equals("")){
                                int pickedamount=Integer.parseInt(jsonObject.getString("delivery_charge"))+Integer.parseInt(jsonObject.getString("subtotal"));
                                txt_total.setText("\u20B9"+pickedamount);
                            }else {
                                txt_total.setText("\u20B9"+jsonObject.getString("subtotal"));
                            }
                            txt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateOrderPick("4");
                                }
                            });




                        }





                    }
                    rv_address.setHasFixedSize(true);
                    rv_address.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
                    rv_address.setAdapter(new OrderAdapter(orderIdModels,MyOrderActivity.this));

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



    private void updateLatLong(String addresssLat, String addresssLng, String shoppLat, String shoppLng){
        Log.e("sddsadsdsdsd", addresssLat+"addressLat" );
        Log.e("sddsadsdsdsd", addresssLng+"addressLng" );
        Log.e("sddsadsdsdsd", shoppLat +"shopLat");
        Log.e("sddsadsdsdsd", shoppLng+"shopLng" );
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
                                            intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse("http://maps.google.com/maps?saddr="+response.getString("latitude")+","+response.getString("longitude")+"&"+"daddr="+ addresssLat +","+ addresssLng));
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
                                                    Uri.parse("http://maps.google.com/maps?saddr=" + response.getString("latitude") + "," + response.getString("longitude") + "&" + "daddr=" +shoppLat + "," + shoppLng));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(MyOrderActivity.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
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