package com.ritesh.clique;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ritesh.clique.Constant.Appconstant;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 * Created by ritesh on 23/9/16.
 */


@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.top_holder)
    RelativeLayout cameraselect; // select from ic_camera_click
    @BindView(R.id.bottom_holder)
    RelativeLayout galleryselect;  // select from gallery
    @BindView(R.id.rl_bgcolor)
    RelativeLayout Rlselectimage;    // layout select from ic_camera_click and gallery

    @BindView(R.id.btn_logout)     // Logout Button visible only in for this activity
            Button logout;

    @BindView(R.id.iv_cameraclicklong)
    RingProgressBar IVlognclick;  // image view for Record video on button long pressed
    @BindView(R.id.iv_cameraclick)
    ImageView IVselectimage;    // image view for select image normal click default
    @BindView(R.id.iv_fb)
    ImageView IVfacebook;    // image view for facebook  default
    @BindView(R.id.iv_instagram)
    ImageView IVinstagram;    // image view for instagram  default
    @BindView(R.id.iv_snapchat)
    ImageView IVsnapchat;    // image view for snapchat  default
    @BindView(R.id.iv_twitter)
    ImageView IVtwitter;    // image view for twitter  default

  /*  @BindView(R.id.rl_fbclick)
    RelativeLayout rlfacebook;    // relative layout for facebook on middle
    @BindView(R.id.rl_instagramclick)
    RelativeLayout rlinstagram;   // relative layout for instagram on middle
    @BindView(R.id.rl_snapchatclick)
    RelativeLayout rlsnapchat;   // relative layout for snapchat on middle*/

    @BindView(R.id.ll_bottomsheet)
    LinearLayout llbottomfirst;   // bottom linear layout before taking image default
    @BindView(R.id.ll_bottomshet)
    LinearLayout llbottomsecond;  // bottom linear layout after taking image modified

    @BindView(R.id.rl_fb)
    RelativeLayout rlfb;    // relative layout for facebook before taking image only for dummy use
    @BindView(R.id.rl_instagram)
    RelativeLayout rlinstagrams;   // relative layout for instagram before taking image only for dummy use
    @BindView(R.id.rl_snapchat)
    RelativeLayout rlsnapchats;   // relative layout for snapchat before taking image only for dummy use
    @BindView(R.id.rl_twitter)
    RelativeLayout rltwitter;   // relative layout for twitter before taking image only for dummy use


    @BindView(R.id.iv_fbc)
    ImageView IVfb;    // image view for facebook after taking image used for sharing
    @BindView(R.id.iv_instagramc)
    ImageView IVinstagrams;    // image view for instagram after taking image used for sharing
    @BindView(R.id.iv_snapchatc)
    ImageView IVsnapchats;    // image view for snapchat after taking image used for sharing
    @BindView(R.id.iv_twitterc)
    ImageView IVtwitters;    // image view for snapchat after taking image used for sharing

    @BindView(R.id.setbgselectedimage)
    RelativeLayout setbgimage;    // MAIN relative layout for showing BIG IMAGE ON BACK GROUND after taking
   /* @BindView(R.id.rl_share_main)
    RelativeLayout sharelayoutmain;*/  // relative layout for showing after taking image
   /* @BindView(R.id.rl_imageselected)
    RelativeLayout imageselectedlayout; */ // relative layout for showing image on middle

    Uri mResumeUri, mImageCaptureUri, mImageCaptureUrii, fileUriCamera;
    Bitmap bm;
    File SharedImage;
    String capturedImagePath = "",
            picturePath = "",
            ImageFinalPath = "",
            currentTemplatePath = "",
            condition = "";

    boolean doubleBackToExitPressedOnce = false;

    Drawable d;

    private static final int CAMERA_PIC_REQUEST = 1337;
    private int progress = 0;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                if (progress < 100) {
                    progress++;
                    IVlognclick.setProgress(progress);

                    IVlognclick.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {
                            // Here after the completion of the processing
                        }
                    });
                }
            }
        }
    };

/*
    private ExplosionField mExplosionField;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
/*
        mExplosionField = ExplosionField.attach2Window(this);
        addListener(findViewById(R.id.ll_bottomshet));*/


        Rlselectimage.setVisibility(View.GONE);  // on start layout is not visible
        logout.setVisibility(View.VISIBLE);

        IVselectimage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    IVselectimage.setVisibility(View.GONE);
                    IVlognclick.setVisibility(View.VISIBLE);
                    Log.e("oooooooooooooooo Button Pressed call ", " YES");

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            for (int i = 0; i < 100; i++) {
                                try {
                                    Thread.sleep(100);

                                    mHandler.sendEmptyMessage(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    IVselectimage.setVisibility(View.VISIBLE);
                    IVlognclick.setVisibility(View.GONE);
                    Log.e("oooooooooooooooo Button Released call ", " YES");

                   /* Intent intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                }
                return MainActivity.super.onTouchEvent(event);
            }
        });

        IVselectimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });




        /* **********  ********* **********  ********* Main screen Logout Button data (Start) **********  ********* **********  ********* */
        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_LONG).show();
                Appconstant.editor.clear();
                Appconstant.editor.commit();

                Intent sendToLoginandRegistration = new Intent(getApplicationContext(),
                        LoginSignupSelectAcitivity.class);

                startActivity(sendToLoginandRegistration);
            }
        });
        /* **********  ********* **********  ********* Main screen Logout Button  data (Start) **********  ********* **********  ********* */


        IVselectimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Rlselectimage.setVisibility(View.VISIBLE);
                condition = "b";  // used for back pressed

                IVselectimage.setClickable(false);
                IVfacebook.setClickable(false);
                IVinstagram.setClickable(false);
                IVsnapchat.setClickable(false);
                IVtwitter.setClickable(false);
                IVselectimage.setLongClickable(false);
                IVlognclick.setVisibility(View.GONE);
                IVselectimage.setVisibility(View.GONE);
                logout.setClickable(false);
            }
        });

        IVfacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "OOhooo..\nPlease Select Your Image First", TastyToast.LENGTH_LONG,
                        TastyToast.INFO);
            }
        });

        IVinstagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "OOhooo..\nPlease Select Your Image First", TastyToast.LENGTH_LONG,
                        TastyToast.INFO);
            }
        });

        IVsnapchat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "OOhooo..\nPlease Select Your Image First", TastyToast.LENGTH_LONG,
                        TastyToast.INFO);
            }
        });

        IVtwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "OOhooo..\nPlease Select Your Image First", TastyToast.LENGTH_LONG,
                        TastyToast.INFO);
            }
        });


        cameraselect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                condition = "c"; // used for back pressed

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.e(" ********** START CAMERA FOR CAPTURING IMAGE **********", "YES");
                fileUriCamera = getOutputMediaFileUri(0);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriCamera);
                startActivityForResult(cameraIntent, 0);
                Rlselectimage.setVisibility(View.GONE);
            }
        });

        galleryselect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(" ********** START SELECTION FROM Gallery FOR IMAGE **********", "YES");

                // Create the Intent for Image Gallery.
                Intent i = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);

                // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                startActivityForResult(i, 1);
                condition = "d"; // used for back pressed
                Rlselectimage.setVisibility(View.GONE);


            }
        });

        IVfb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPackageExisted("com.facebook.katana")) {

                    currentTemplatePath = picturePath;
                    Log.e(" Package Found", "com.facebook.katana");

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    i.setPackage("com.facebook.katana");
                    startActivity(i);

                } else {
                    Toast.makeText(MainActivity.this, "Please Download Facebook app", Toast.LENGTH_SHORT).show();
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    Intent intents;
                    intents = new Intent(Intent.ACTION_VIEW);
                    intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intents.setData(Uri.parse("market://details?id="
                            + "com.facebook.katana"));
                    startActivity(intents);

                }


            }
        });

        IVinstagrams.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPackageExisted("com.instagram.android")) {

                    currentTemplatePath = picturePath;
                    Log.e(" Package Found", "com.instagram.android");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);

                } else {
                    Toast.makeText(MainActivity.this, "Please Download Instagram app", Toast.LENGTH_SHORT).show();
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    Intent intent;
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id="
                            + "com.instagram.android"));
                    startActivity(intent);

                }
            }
        });

        IVsnapchats.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.snapchat.android")) {

                    currentTemplatePath = picturePath;
                    Log.e(" Package Found", "com.snapchat.android");

                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("image/*");
                    ii.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    ii.setPackage("com.snapchat.android");
                    startActivity(ii);

                } else {
                    Log.e(" Package NOT  Found", "com.snapchat.android");
                    Toast.makeText(MainActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    Intent intentss;
                    intentss = new Intent(Intent.ACTION_VIEW);
                    intentss.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentss.setData(Uri.parse("market://details?id="
                            + "com.snapchat.android"));
                    startActivity(intentss);
                }


            }
        });

        IVtwitters.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.twitter.android")) {

                    currentTemplatePath = picturePath;
                    Log.e(" Package Found", "com.twitter.android");
                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("image/*");
                    ii.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    ii.setPackage("com.twitter.android");
                    startActivity(ii);

                } else {
                    Log.e(" Package NOT  Found", "com.twitter.android");
                    Toast.makeText(MainActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    Intent intentss;
                    intentss = new Intent(Intent.ACTION_VIEW);
                    intentss.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentss.setData(Uri.parse("market://details?id="
                            + "com.twitter.android"));
                    startActivity(intentss);
                }


            }
        });


    }

    public boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {

        switch (requestCode) {

            case 1:
                condition = "e"; // used for back pressed

                if (resultCode == RESULT_OK) {

                    fileUriCamera = data.getData();
                    Log.e("Gallery  URI :", "" + fileUriCamera);

                    String[] projection = {Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(fileUriCamera, projection, null, null, null);
                    cursor.moveToFirst();
                    picturePath = cursor.getString(cursor.getColumnIndex(projection[0]));

                    cursor.close();
                    Log.e("xxxxxxxxxxxxGallery  picturePath :", "" + picturePath);

                    try {
                        bm = Images.Media.getBitmap(getContentResolver(), fileUriCamera);
                        Log.e("Gallery  Path", fileUriCamera.getPath());

                        ImageFinalPath = fileUriCamera.getPath();
                        Log.e("Gallery Final Path :", "" + ImageFinalPath);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            d = new BitmapDrawable(getResources(), bm);
                            setbgimage.setBackgroundDrawable(d);
                            Log.e(" *********** GAllery IMAGE ***********: ", "" + SharedImage);
                        } else {
                            d = new BitmapDrawable(getResources(), bm);
                            setbgimage.setBackground(d);
                        }

                        llbottomfirst.setVisibility(View.GONE);
                        llbottomsecond.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case 0:
                condition = "e"; // used for back pressed

                Log.e("Camera  Path", fileUriCamera.getPath());

                ImageFinalPath = fileUriCamera.getPath();
                picturePath = ImageFinalPath;
                Log.e("Camera Final Path For Share :", "" + picturePath);

                Log.e("Camera Final Path :", "" + ImageFinalPath);

                Log.e(" *********** CAPTURED IMAGE ***********: ", "" + ImageFinalPath);

                SharedImage = new File(fileUriCamera.getPath());

                try {
                    bm = Images.Media.getBitmap(this.getContentResolver(), fileUriCamera);

                    if (bm == null) {

                    } else {
//                            capturedImagePath.add(fileUri.getPath());
//                            setbgimage.add(bm);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            d = new BitmapDrawable(getResources(), bm);
                            setbgimage.setBackgroundDrawable(d);
                        } else {
                            d = new BitmapDrawable(getResources(), bm);
                            setbgimage.setBackground(d);
                        }
                    }

                    llbottomfirst.setVisibility(View.GONE);
                    llbottomsecond.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("bmcamera", "" + bm);


                break;


//            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/Clique");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUriCamera);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUriCamera = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    public void onBackPressed() {
//        Rlselectimage.setVisibility(View.GONE);
//        IVselectimage.setClickable(true);
//        IVfacebook.setClickable(true);
//        IVinstagram.setClickable(true);
//        IVsnapchat.setClickable(true);

        if (condition == "a") {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = a %%%%%%%%%%%%%%%%%% ", "TRUE");

            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } else if (condition == "b") {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = b %%%%%%%%%%%%%%%%%% ", "TRUE");

            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (condition == "c") {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = c %%%%%%%%%%%%%%%%%% ", "TRUE");

            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (condition == "d") {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = d %%%%%%%%%%%%%%%%%% ", "TRUE");
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (condition == "e") {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = e %%%%%%%%%%%%%%%%%% ", "TRUE");

            /*reset ExplosionField field (Start)*/
/*            View bottomshet = findViewById(R.id.ll_bottomshet);
            reset(bottomshet);
            addListener(bottomshet);
            mExplosionField.clear();*/
             /*reset ExplosionField field (End)*/


            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        if (doubleBackToExitPressedOnce) {
            Log.e(" %%%%%%%%%%%%%%%%%% Double tap %%%%%%%%%%%%%%%%%% ", "TRUE");
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//         Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


}
