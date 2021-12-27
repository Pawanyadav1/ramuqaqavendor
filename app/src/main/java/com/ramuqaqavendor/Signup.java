package com.ramuqaqavendor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ramuqaqavendor.other.API;
import com.ramuqaqavendor.other.ImageUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class Signup extends AppCompatActivity {
ImageView iv_cal,iv_front,iv_back,iv_image,iv_adhar,ivBack;
TextView txt_cal;
EditText editname,editemail,editmobile,edit_mobile1;
RadioGroup rd_group;
MaterialButton txt_signup;
String StrFinalStatus="";
    CheckBox checkbox;

    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2,GALLERY1 = 3, CAMERA1 = 4;
    File f,F1,F2,F3;
    String strImage="",strsignatureImage="",strPicImage="",strAdhar="";
    LinearLayout ll_loading;
    File front_gallery_file,front_back_file,picture_file,adhar_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        iv_cal=findViewById(R.id.iv_cal);
        txt_cal=findViewById(R.id.txt_cal);
        rd_group=findViewById(R.id.rd_group);
        editname=findViewById(R.id.editname);
        editemail=findViewById(R.id.editemail);
        editmobile=findViewById(R.id.editmobile);
        edit_mobile1=findViewById(R.id.edit_mobile1);
        iv_front=findViewById(R.id.iv_front);
        iv_back=findViewById(R.id.iv_back);
        iv_image=findViewById(R.id.iv_image);
        ll_loading=findViewById(R.id.ll_loading);
        txt_signup=findViewById(R.id.txt_signup);
        iv_adhar=findViewById(R.id.iv_adhar );
      //  checkbox=findViewById(R.id.checkbox );
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,AddMoreDetails.class));

            }
        });

       /* checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Terms="1";
                }else{
                    Terms="";
                }
            }
        });

        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ramuqaqa.com/term_driver_conditions";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);
            }
        });
*/

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editname.getText().toString().trim();
                String email=editemail.getText().toString().trim();
                String mobile=editmobile.getText().toString().trim();
                String emmobile=edit_mobile1.getText().toString().trim();
                String dob=txt_cal.getText().toString().trim();

                if (name.equals("")){
                    Toasty.error(Signup.this, "Please enter your name...", Toast.LENGTH_SHORT, true).show();

                }else  if (email.equals("")){
                    Toasty.error(Signup.this, "Please enter email...", Toast.LENGTH_SHORT, true).show();

                }else  if (mobile.equals("")){
                    Toasty.error(Signup.this, "Please enter mobile number...", Toast.LENGTH_SHORT, true).show();

                }else  if (emmobile.equals("")){
                    Toasty.error(Signup.this, "Please enter Emergency mobile number...", Toast.LENGTH_SHORT, true).show();

                }else  if (dob.equals("")){
                    Toasty.error(Signup.this, "Please select your date of birth ...", Toast.LENGTH_SHORT, true).show();

                }else  if (StrFinalStatus.equals("")){
                    Toasty.error(Signup.this, "Please choose gender", Toast.LENGTH_SHORT, true).show();

                }else {

                    startActivity(new Intent(Signup.this,AddMoreDetails.class));
                    //  Update(name,email,mobile,StrFinalStatus,dob);
                }
            }
        });

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxMediaPicker.builder(Signup.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(Signup.this, filepath));
                            picture_file = ImageUtils.bitmapToFile(bitmap, Signup.this);
                            Glide.with(Signup.this).load(picture_file).into(iv_image);
                            strPicImage = picture_file.toString();
                            F2 = new File(strPicImage);
                            Log.e("picImage", "picImage: " + strPicImage);
                        });
            }
        });


      /*  iv_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   showPictureDialog();

                RxMediaPicker.builder(Signup.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(Signup.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, Signup.this);
                            Glide.with(Signup.this).load(front_gallery_file).into(iv_front);
                            strImage = front_gallery_file.toString();
                            f = new File(strImage);
                            Log.e("govtFront", "govtFront: " + strImage);


                        });
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showPictureDialog1();
                RxMediaPicker.builder(Signup.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(Signup.this, filepath));
                            front_back_file = ImageUtils.bitmapToFile(bitmap, Signup.this);
                            Glide.with(Signup.this).load(front_back_file).into(iv_back);
                            strsignatureImage = front_back_file.toString();
                            F1 = new File(strsignatureImage);
                            Log.e("govtFront", "govtFront: " + strImage);
                        });
            }
        });

        iv_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPictureDialog1();
                RxMediaPicker.builder(Signup.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(Signup.this, filepath));
                            adhar_file = ImageUtils.bitmapToFile(bitmap, Signup.this);
                            Glide.with(Signup.this).load(adhar_file).into(iv_adhar);
                            strAdhar = adhar_file.toString();
                            F3= new File(strAdhar);
                            Log.e("govtFront", "govtFront: " + strAdhar);
                        });
            }
        });
*/

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rd_group.findViewById(checkedId);
                int index = rd_group.indexOfChild(radioButton);

                Log.e("fgdfghdfg", index + "");
                switch (index) {
                    case 0:
                        StrFinalStatus = "male";
                        break;
                    case 1:
                        StrFinalStatus = "Female";
                        break;

                    case 2:
                        StrFinalStatus = "Other";
                        break;
                }
            }
        });


        iv_cal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        //  txt_date.setText(selectedyear + "-" + ++selectedmonth  + "-" + selectedday);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(selectedyear, selectedmonth, selectedday);
                        //  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString = format.format(calendar.getTime());
                        txt_cal.setText(dateString);

                    }
                }, mYear, mMonth, mDay);
                Calendar c_min = Calendar.getInstance();
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
                mDatePicker.show();
            }
        });

    }


    public void showPictureDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Signup.this);
        builder.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        captureFromCamera();
                        break;
                }

            }
        });

        builder.show();
    }
    public void showPictureDialog1() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Signup.this);
        builder.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery1();
                        break;

                    case 1:
                        captureFromCamera1();
                        break;
                }

            }
        });

        builder.show();
    }

    public void choosePhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY);
    }

    public void choosePhotoFromGallery1() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY1);
    }



    public void captureFromCamera() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA);
    }

    public void captureFromCamera1() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    iv_front.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_front.setImageBitmap(thumbnail);
            saveImage(thumbnail);
        }else if (requestCode == GALLERY1) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    String path = saveImage1(bitmap);
                    iv_back.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == CAMERA1) {
            Bitmap thumbnail2 = (Bitmap) data.getExtras().get("data");
            iv_back.setImageBitmap(thumbnail2);
            saveImage1(thumbnail2);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(Signup.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("fvbcbv", "File Saved::---&gt;" + f.getAbsolutePath());
            strImage = String.valueOf(f);
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public String saveImage1(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            F1 = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            F1.createNewFile();
            FileOutputStream fo = new FileOutputStream(F1);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(Signup.this,
                    new String[]{F1.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("fvbcbv", "File Saved::---&gt;" + F1.getAbsolutePath());
            strsignatureImage = String.valueOf(F1);
            return F1.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


/*
    public void Update(String name, String email, String mobile, String strFinalStatus,String dob) {
        ll_loading.setVisibility(View.VISIBLE);
        AndroidNetworking.upload(API.add_driver)
                .addMultipartParameter("name", name)
                .addMultipartParameter("mobile",mobile )
                .addMultipartParameter("email", email)
                .addMultipartParameter("gender",strFinalStatus )
                .addMultipartParameter("dob",dob )
               .addMultipartFile("driving_liscsence_front", f)
                .addMultipartFile("driving_liscsence_back",F1 )
                .addMultipartFile("image",F2 )
                .addMultipartFile("adhar_image",F3 )
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ll_loading.setVisibility(View.GONE);
                Log.e("fbdghdgf", response.toString());
                try {
                    if (response.has("result")) {
                        if (response.getString("result").equals("successful")) {
                            Toasty.success(Signup.this, "Register Successful...Wait for admin approval....", Toast.LENGTH_SHORT, true).show();
                             finish();
                             startActivity(new Intent(Signup.this,MainActivity.class));

                        } else {
                            Toasty.error(Signup.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(Signup.this, ""+response.getString("result"), Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ll_loading.setVisibility(View.GONE);
                    Log.e("dfdfdsdfsfdsdfs", e.getMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("dfdfdsdfsfdsdfs", anError.getMessage());
                ll_loading.setVisibility(View.GONE);
            }
        });
    }
*/



}