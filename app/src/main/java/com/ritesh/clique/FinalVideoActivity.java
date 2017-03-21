package com.ritesh.clique;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 25/10/16.
 */

public class FinalVideoActivity extends Activity {


    @BindView(R.id.iv_fbc)
    ImageView ivfb;  // used for call facebook app for sharing image
    @BindView(R.id.iv_instagramc)
    ImageView ivinstagram;  // used for call instagram app for sharing image
    @BindView(R.id.iv_twitterc)
    ImageView ivtwitter;  // used for call twitter app for sharing image
    @BindView(R.id.iv_snapchatc)
    ImageView ivsnapchat;   // used for call snapchat app for sharing image

    @BindView(R.id.vv_video)
    VideoView finalvideoview;


    String BundleVideopath = "",
            currentTemplatePath = "",
            msg = "Android : ",
            condition = "";

    Uri video1;
    File SharedVideo;

    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_video);
        ButterKnife.bind(this);
        condition = "a";
        finalvideoview.setVisibility(View.VISIBLE);
        finalvideoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mGestureDetector = new GestureDetector(this, mGestureListener);

        Log.e(msg, "The onCreate() event");

        Bundle extra = getIntent().getExtras();
        BundleVideopath = extra.get("VIDEOPATH").toString();

        Log.e(" ********** final Video URL from bundle **********", "" + BundleVideopath);

        SharedVideo = new File(BundleVideopath);
        Log.e(" ********** final Video from file **********", "" + SharedVideo);


        video1 = Uri.parse(BundleVideopath);
        finalvideoview.setVideoURI(video1);
        finalvideoview.seekTo(800);  // 100 milliseconds (0.1 s) into the clip.
        finalvideoview.start();


        ivfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.facebook.katana")) {

                    currentTemplatePath = BundleVideopath;
                    Log.e(" Package Found", "com.facebook.katana");

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("video/*");
                    i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    i.setPackage("com.facebook.katana");
                    startActivity(i);

                } else {
                    Toast.makeText(FinalVideoActivity.this, "Please Download Facebook app", Toast.LENGTH_SHORT).show();
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

        ivinstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPackageExisted("com.instagram.android")) {

                    currentTemplatePath = BundleVideopath;
                    Log.e(" Package Found", "com.instagram.android");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);

                } else {
                    Toast.makeText(FinalVideoActivity.this, "Please Download Instagram app", Toast.LENGTH_SHORT).show();
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

        ivsnapchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.snapchat.android")) {

                    currentTemplatePath = BundleVideopath;
                    Log.e(" Package Found", "com.snapchat.android");

                    Intent snapivideo = new Intent(Intent.ACTION_SEND);
                    snapivideo.setType("video/*");
                    snapivideo.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    snapivideo.setPackage("com.snapchat.android");
                    startActivity(snapivideo);

                } else {
                    Log.e(" Package NOT  Found", "com.snapchat.android");
                    Toast.makeText(FinalVideoActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
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

        ivtwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.twitter.android")) {

                    currentTemplatePath = BundleVideopath;
                    Log.e(" Package Found", "com.twitter.android");
                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("video/*");
                    ii.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    ii.setPackage("com.twitter.android");
                    startActivity(ii);

                } else {
                    Log.e(" Package NOT  Found", "com.twitter.android");
                    Toast.makeText(FinalVideoActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
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

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (finalvideoview.isPlaying())
                finalvideoview.pause();
            else
                finalvideoview.start();
            return true;
        }
    };

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
    public void onBackPressed() {
        if (condition.equalsIgnoreCase("a")) {
            Log.e(" %%%%%%%%%%%%%%%%%% condition = a %%%%%%%%%%%%%%%%%% ", "TRUE");
            Intent intent = new Intent(FinalVideoActivity.this,VideoCaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

}
