package com.ritesh.clique;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 5/10/16.
 */

public class FinalImageActivity extends Activity {

    @BindView(R.id.iv_photo)
    ImageView ivphoto; // used for set final image in full screen for sharing

    @BindView(R.id.iv_fbc)
    ImageView ivfb;  // used for call facebook app for sharing image
    @BindView(R.id.iv_instagramc)
    ImageView ivinstagram;  // used for call instagram app for sharing image
    @BindView(R.id.iv_twitterc)
    ImageView ivtwitter;  // used for call twitter app for sharing image
    @BindView(R.id.iv_snapchatc)
    ImageView ivsnapchat;   // used for call snapchat app for sharing image


    String BundleImagepath = "",
            currentTemplatePath = "",
            msg = "Android : ",
            condition = "";

    Bitmap bmm;
    File SharedImage;
//    int xDim, yDim;        //stores ImageView dimensions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_image);
        ButterKnife.bind(this);
        condition = "a";

        Log.e(msg, "The onCreate() event for FinalImageActivity");

        Bundle extra = getIntent().getExtras();
        BundleImagepath = extra.get("IMAGEPATH").toString();

        Log.e(" ********** final image URL from bundle **********", "" + BundleImagepath);

        SharedImage = new File(BundleImagepath);
        Log.e(" ********** final image from file **********", "" + SharedImage);

        if (SharedImage.exists()) {

            long length = SharedImage.length();
            length = length / 1024;

            double bytes = SharedImage.length();
            double kilobytes = (bytes / 1024);
            double megabytes = (kilobytes / 1024);
            double gigabytes = (megabytes / 1024);

            Log.e("Image Size :", "" + bytes + "\n" + "" + kilobytes + "\n" + "" + megabytes);
            Log.e("Image Size :", "" + kilobytes + "\n" + "" + megabytes);

            if (SharedImage.length() >= 1048576) {
                Log.e("Image Size is greater than 1 mb :", "YES");

                /*http://stackoverflow.com/questions/5125779/how-to-compress-image-for-imageview-in-android*/
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inTempStorage = new byte[24 * 1024];
                options.inJustDecodeBounds = false;
                options.inSampleSize = 2;
                bmm = BitmapFactory.decodeFile(SharedImage.getAbsolutePath(), options);
                Bitmap b1 = ThumbnailUtils.extractThumbnail(bmm, 1024, 1024);
                ivphoto.setImageBitmap(b1);
                if (bmm != null) {
                    bmm.recycle();
                    Log.e("bmm recycle 1", "YES");
                }
                bmm = BitmapFactory.decodeFile(SharedImage.getAbsolutePath(), options);
                Bitmap b2 = ThumbnailUtils.extractThumbnail(bmm, 1024, 1024);  // 1024 , 1024 is image size after convert
                ivphoto.setImageBitmap(b2);

                int oldWidth = b2.getWidth();
                int oldHeight = b2.getHeight();
                Log.e("Image Height (b2):", "" + oldHeight + "\n" + "Image Width (b2) :" + "" + oldWidth);

                if (bmm != null) {
                    bmm.recycle();
                    Log.e("bmm recycle 2", "YES");
                }
                /*http://stackoverflow.com/questions/5125779/how-to-compress-image-for-imageview-in-android*/

            } else {

                bmm = BitmapFactory.decodeFile(SharedImage.getAbsolutePath());

                int oldWidth = bmm.getWidth();
                int oldHeight = bmm.getHeight();

                Log.e("Image Height (bmm) :", "" + oldHeight + "\n" + "Image Width (bmm) :" + "" + oldWidth);

                ivphoto.setImageBitmap(bmm);
                Log.e("Image Size is greater than 1 mb :", "NO");
            }

           /* bmm = BitmapFactory.decodeFile(SharedImage.getAbsolutePath());
            ivphoto.setImageBitmap(bmm);*/

        } else {
            Log.e("File does not exists!", "YES");
        }

        Log.e(" original bitmap bm : ", "" + bmm);

        ivfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageExisted("com.facebook.katana")) {

                    currentTemplatePath = BundleImagepath;
                    Log.e(" Package Found", "com.facebook.katana");

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    i.setPackage("com.facebook.katana");
                    startActivity(i);

                } else {
                    Toast.makeText(FinalImageActivity.this, "Please Download Facebook app", Toast.LENGTH_SHORT).show();
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

                    currentTemplatePath = BundleImagepath;
                    Log.e(" Package Found", "com.instagram.android");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);

                } else {
                    Toast.makeText(FinalImageActivity.this, "Please Download Instagram app", Toast.LENGTH_SHORT).show();
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

                    currentTemplatePath = BundleImagepath;
                    Log.e(" Package Found", "com.snapchat.android");

                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("image/*");
                    ii.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    ii.setPackage("com.snapchat.android");
                    startActivity(ii);

                } else {
                    Log.e(" Package NOT  Found", "com.snapchat.android");
                    Toast.makeText(FinalImageActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
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

                    currentTemplatePath = BundleImagepath;
                    Log.e(" Package Found", "com.twitter.android");
                    Intent ii = new Intent(Intent.ACTION_SEND);
                    ii.setType("image/*");
                    ii.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(currentTemplatePath)));
                    ii.setPackage("com.twitter.android");
                    startActivity(ii);

                } else {
                    Log.e(" Package NOT  Found", "com.twitter.android");
                    Toast.makeText(FinalImageActivity.this, "Please Download Snapchat app", Toast.LENGTH_SHORT).show();
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


    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(msg, "The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(msg, "The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(msg, "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(msg, "The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(msg, "The onDestroy() event");
    }

    @Override
    public void onBackPressed() {
        if (condition.equalsIgnoreCase("a")) {
            Log.e(" %%%%%%%%%%%%%%%%%% condition = a %%%%%%%%%%%%%%%%%% ", "TRUE");
            Intent intent = new Intent(getApplicationContext(),
                    CameraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
