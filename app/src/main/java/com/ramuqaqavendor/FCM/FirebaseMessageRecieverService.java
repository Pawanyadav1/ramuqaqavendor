 package com.ramuqaqavendor.FCM;

 import android.annotation.SuppressLint;
 import android.app.Notification;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.graphics.Color;
 import android.media.AudioManager;
 import android.media.MediaPlayer;
 import android.net.Uri;
 import android.os.Build;
 import android.os.CountDownTimer;
 import android.os.Handler;
 import android.os.Looper;
 import android.util.Log;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.annotation.RequiresApi;
 import androidx.core.app.NotificationCompat;

 import com.androidnetworking.AndroidNetworking;
 import com.androidnetworking.error.ANError;
 import com.androidnetworking.interfaces.JSONObjectRequestListener;
 import com.google.firebase.messaging.FirebaseMessagingService;
 import com.google.firebase.messaging.RemoteMessage;

 import com.ramuqaqavendor.HomeActivity;
 import com.ramuqaqavendor.MainActivity;
 import com.ramuqaqavendor.NotificationActivity;
 import com.ramuqaqavendor.R;
 import com.ramuqaqavendor.SelectTypeActivity;
 import com.ramuqaqavendor.ShowOrderID;
 import com.ramuqaqavendor.other.API;
 import com.ramuqaqavendor.other.APPCONSTANT;
 import com.ramuqaqavendor.other.SharedHelper;

 import org.json.JSONException;
 import org.json.JSONObject;

 import es.dmoral.toasty.Toasty;

 import static android.app.Notification.PRIORITY_HIGH;

 public class FirebaseMessageRecieverService extends FirebaseMessagingService {
     CountDownTimer countDownTimer;
     MediaPlayer mediaPlayer;
     Uri path;
     Context context;
     String USERID="";
     private  void FirebaseMessageRecieverService(Context context){
         this.context=context;

     }
     @RequiresApi(api = Build.VERSION_CODES.Q)
     @Override
     public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
         super.onMessageReceived(remoteMessage);
         Log.e("check", "onMessageRecieved Called");
       //  USERID=SharedHelper.getKey(context,APPCONSTANT.USERID);
         if (remoteMessage.getNotification() != null) {

             String title = remoteMessage.getNotification().getTitle();

             Log.e("sjjcbs", "ii" + title);
         }

         if (remoteMessage.getData().size() > 0) {
             Log.e("check", "Data received");
             Log.e("check", remoteMessage.getData().toString());
             try {
                 String Data = remoteMessage.getData().toString();
                 String newData = "";
                 if (Data.contains("=")) {
                     newData = Data.replace("=", ":");
                 }
                 JSONObject jsonObject = new JSONObject(newData);
                 JSONObject data = jsonObject.getJSONObject("data");
                 String title = data.getString("title");
                 String message = data.getString("message");

                 Intent myIntent = new Intent("Check");
                 myIntent.putExtra("action", title);
                 myIntent.putExtra("message", message);
                 this.sendBroadcast(myIntent);
                 Log.e("gnggdgndgndng", "onMessageReceived: "+message );

                 PendingIntent contentAppActivityIntent = null;
                 Intent appActivityIntent=null;
                 if (message.equals("Your account is deactivated by admin")){
                     //
                      appActivityIntent = new Intent(this, MainActivity.class);

                      contentAppActivityIntent =
                             PendingIntent.getActivity(
                                     this,  // calling from Activity
                                     0,
                                     appActivityIntent,
                                     PendingIntent.FLAG_UPDATE_CURRENT);
                 }else if (message.equals("Your account is activated by admin")){

                 }else if (message.equals("You are removed from admin side")){
                     appActivityIntent = new Intent(this, MainActivity.class);

                     contentAppActivityIntent =
                             PendingIntent.getActivity(
                                     this,  // calling from Activity
                                     0,
                                     appActivityIntent,
                                     PendingIntent.FLAG_UPDATE_CURRENT);
                 }else {
                      appActivityIntent = new Intent(this, NotificationActivity.class);

                      contentAppActivityIntent =
                             PendingIntent.getActivity(
                                     this,  // calling from Activity
                                     0,
                                     appActivityIntent,
                                     PendingIntent.FLAG_UPDATE_CURRENT);
                 }


                 Notification notification = new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                         .setSmallIcon(R.drawable.logo)
                         .setContentTitle(title)
                         .setContentText(message)
                         .setPriority(PRIORITY_HIGH)
                         .setContentIntent(contentAppActivityIntent)
                         .setColor(Color.BLACK)
                         .setDefaults(Notification.DEFAULT_ALL)
                         .build();

                 NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                 manager.notify(1002, notification);

                 mediaPlayer=new MediaPlayer();
                 mediaPlayer = MediaPlayer.create(this, R.raw.notification);
                 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                 mediaPlayer.start();






             } catch (JSONException e) {
                 e.printStackTrace();
                 Log.e("jhjjhjhjhhb", e.getMessage());

             }
         }
     }

     @Override
     public void onDeletedMessages() {
         super.onDeletedMessages();
         Log.e("check", "onDeleteMessage Called");
     }

     @SuppressLint("ApplySharedPref")
     @Override
     public void onNewToken(@NonNull String s) {
         super.onNewToken(s);
         Log.e("zczzvzvzvv", "onNewToken Called" + s);

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
                             SharedHelper.putKey(context, APPCONSTANT.USERID,"");
                             Intent intent = new Intent(context, SelectTypeActivity.class);
                             if(Build.VERSION.SDK_INT >= 11) {
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             } else {
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             }
                             startActivity(intent);

                         }
                     } else {
                         Toasty.error(context, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

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

 }