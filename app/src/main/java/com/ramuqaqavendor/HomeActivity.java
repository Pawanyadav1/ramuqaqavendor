package com.ramuqaqavendor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ramuqaqavendor.internetConnection.IinternetConnectionInterface;
import com.ramuqaqavendor.internetConnection.InternetConnectivity;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.APPCONSTANT;
import com.ramuqaqavendor.other.SharedHelper;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
public  static DrawerLayout drawer;
LinearLayout ll_home,ll_order,ll_contact,ll_about,ll_terms,ll_privacy,ll_rating,ll_earn;
BottomNavigationView bottomNav;
ImageView iv_pro;
String USERID="",OnlineStatus="";
TextView txt_earns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkNetconnection();
        String REGID=SharedHelper.getKey(HomeActivity.this,APPCONSTANT.REGID);
        USERID=SharedHelper.getKey(HomeActivity.this,APPCONSTANT.USERID);

        OnlineStatus = SharedHelper.getKey(HomeActivity.this, APPCONSTANT.ONLNESTATUS);

        ll_home=findViewById(R.id.ll_home);
        ll_order=findViewById(R.id.ll_order);
        ll_contact=findViewById(R.id.ll_contact);
        ll_about=findViewById(R.id.ll_about);
        ll_terms=findViewById(R.id.ll_terms);
        bottomNav=findViewById(R.id.bottomNav);
        iv_pro=findViewById(R.id.iv_pro);
        ll_privacy=findViewById(R.id.ll_privacy);
        ll_rating=findViewById(R.id.ll_rating);
        txt_earns=findViewById(R.id.txt_earns);
        ll_earn=findViewById(R.id.ll_earn);
        ll_contact.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        ll_terms.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        iv_pro.setOnClickListener(this);
        ll_privacy.setOnClickListener(this);
        ll_rating.setOnClickListener(this);
        ll_earn.setOnClickListener(this);
        bottomNav.setOnNavigationItemSelectedListener(this);

        drawer=findViewById(R.id.drawer);
        if (savedInstanceState == null) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeMapFragment())
                   .addToBackStack(null)   .commit();
        }

        showProfile();


        //bottomNav.getMenu().performIdentifierAction(R.id.nav_home, 0);
        //onNavigationItemSelected(bottomNav.getMenu().findItem(R.id.nav_home));



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeMapFragment())
                        .addToBackStack(null)  .commit();
                drawer.closeDrawer(Gravity.LEFT);
                bottomNav.setSelectedItemId(R.id.nav_home);
                break;

            case R.id.ll_order:
                startActivity(new Intent(HomeActivity.this,ShowOrderID.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.ll_contact:
                startActivity(new Intent(HomeActivity.this,ContactUsActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.ll_rating:
                startActivity(new Intent(HomeActivity.this,RatingActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.ll_earn:
                startActivity(new Intent(HomeActivity.this,EarningActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.ll_about:
              /* startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
               finish();*/

                String url = "https://ramuqaqa.com/about_us.php";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.ll_terms:
             /*  startActivity(new Intent(HomeActivity.this,TermsActivity.class));
               finish();*/

                String url2 = "https://ramuqaqa.com/term_and_conditions.php";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url2));
                startActivity(intent2);
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.ll_privacy:
                String url3 = "https://ramuqaqa.com/privacy_policy.php";
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse(url3));
                startActivity(intent3);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.iv_pro:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFrag())
                        .addToBackStack(null) .commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;



        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iv_order:
                startActivity(new Intent(HomeActivity.this,ShowOrderID.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeMapFragment())
                        .addToBackStack(null)  .commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFrag())
                        .addToBackStack(null)   .commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_home);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (OnlineStatus.equals("0")){
            Toasty.error(HomeActivity.this, "You are Online , if you want to exit in the app please offline your mode ", Toast.LENGTH_SHORT, true).show();

        }/*else {
            super.onDestroy();
        }*/
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
                            SharedHelper.putKey(HomeActivity.this, APPCONSTANT.USERID,"");
                            Intent intent = new Intent(HomeActivity.this, SelectTypeActivity.class);
                            if(Build.VERSION.SDK_INT >= 11) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            startActivity(intent);

                        }
                    } else {
                        Toasty.error(HomeActivity.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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