package com.ramuqaqavendor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.ramuqaqavendor.other.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;

public class AddMoreDetails extends AppCompatActivity {

    ImageView ivBack,iv_License,iv_front,iv_back,iv_gst,iv_front1,iv_back1;
    EditText et_pancard,pancard,editacnumber,editifsc,editbranch;
    Spinner spin_category;
    MaterialButton btn_next;

    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2,GALLERY1 = 3, CAMERA1 = 4;
    File f,F1,F2,F3,F4,F5;
    String strImage="",strsignatureImage="",strPicImage="",strImage1="",strLicense="",strShopback="";
    LinearLayout ll_loading;
    File front_gallery_file,front_gallery_file1,front_back_file,license_file,picture_file,front_back_file1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_details);

        ivBack=findViewById(R.id.ivBack);
        iv_License=findViewById(R.id.iv_License);
        iv_front=findViewById(R.id.iv_front);
        iv_back=findViewById(R.id.iv_back);
        iv_gst=findViewById(R.id.iv_gst);
        iv_front1=findViewById(R.id.iv_front1);
        iv_back1=findViewById(R.id.iv_back1);
        pancard=findViewById(R.id.pancard);
        spin_category=findViewById(R.id.spin_category);
        editacnumber=findViewById(R.id.editacnumber);
        editifsc=findViewById(R.id.editifsc);
        editbranch=findViewById(R.id.editbranch);
        et_pancard=findViewById(R.id.et_pancard);
        btn_next=findViewById(R.id.btn_next);



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMoreDetails.this,HomeActivity.class));
            }
        });

        iv_License.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   showPictureDialog();

                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            license_file = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(license_file).into(iv_License);
                            strLicense = license_file.toString();
                            F4 = new File(strLicense);
                            Log.e("govtFront", "govtFront: " + strLicense);


                        });
            }
        });

        iv_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   showPictureDialog();

                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(front_gallery_file).into(iv_front);
                            strImage = front_gallery_file.toString();
                            F2 = new File(strImage);
                            Log.e("govtFront", "govtFront: " + strImage);


                        });
            }
        });


        iv_front1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   showPictureDialog();

                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            front_gallery_file1 = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(front_gallery_file1).into(iv_front1);
                            strImage1 = front_gallery_file1.toString();
                            f = new File(strImage1);
                            Log.e("govtFront", "govtFront: " + strImage1);


                        });
            }
        });


        iv_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPictureDialog1();
                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            picture_file = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(picture_file).into(iv_gst);
                            strPicImage = picture_file.toString();
                            F5 = new File(strPicImage);
                            Log.e("govtFront", "govtFront: " + strPicImage);
                        });
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPictureDialog1();
                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            front_back_file = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(front_back_file).into(iv_back);
                            strsignatureImage = front_back_file.toString();
                            F1 = new File(strsignatureImage);
                            Log.e("govtFront", "govtFront: " + strImage);
                        });
            }
        });



        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showPictureDialog1();
                RxMediaPicker.builder(AddMoreDetails.this)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddMoreDetails.this, filepath));
                            front_back_file1 = ImageUtils.bitmapToFile(bitmap, AddMoreDetails.this);
                            Glide.with(AddMoreDetails.this).load(front_back_file1).into(iv_back1);
                            strShopback = front_back_file1.toString();
                            F3= new File(strShopback);
                            Log.e("govtFront", "govtFront: " + strShopback);
                        });
            }
        });


    }

    public void showPictureDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddMoreDetails.this);
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

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddMoreDetails.this);
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
                    Toast.makeText(AddMoreDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddMoreDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == CAMERA1) {
            Bitmap thumbnail2 = (Bitmap) data.getExtras().get("data");
            iv_back.setImageBitmap(thumbnail2);
            saveImage1(thumbnail2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
            MediaScannerConnection.scanFile(AddMoreDetails.this,
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
            MediaScannerConnection.scanFile(AddMoreDetails.this,
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

}