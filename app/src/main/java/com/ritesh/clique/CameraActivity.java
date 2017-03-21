package com.ritesh.clique;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rm.rmswitch.RMTristateSwitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class CameraActivity extends Activity {

	/*https://github.com/devcelebi/Kut-Camera*/
    /*https://github.com/devcelebi/Kut-Camera*/
    /*https://github.com/devcelebi/Kut-Camera*/

    private static final int MY_INTENT_CLICK = 302;
    @BindView(R.id.iv_capture)
    ImageView CameraButton;   // imageview for capture image from ic_camera_click
    @BindView(R.id.iv_gallery)
    ImageView galleryselect;   // imageview for select image from gallery
    @BindView(R.id.iv_rotate_camera)
    ImageView Rotatecamera;   // imageviwe for rotate ic_camera_click
    @BindView(R.id.iv_photo)
    ImageView finalimage;  // imageview for showing final image select from ic_camera_click or gallery
    @BindView(R.id.setbgselectedimage)
    RelativeLayout setbgimage;   // relative layout for set final image
    @BindView(R.id.rl_flash)
    RelativeLayout flash;    // relative layout for set flash mode on, off, auto
    private boolean flashmode = false;
    @BindView(R.id.rm_triswitch)
    RMTristateSwitch mRMTristateSwitch;     // three state switch button for set flash mode on, off, auto
    @BindView(R.id.txt_rm_triswitch_state)
    TextView mTxtRMTristateSwitchState;  // textview used for set text for flash mode is ON/OFF/AUTO


    @BindView(R.id.ll_bottomshet)
    LinearLayout llbottomsecond;   // linear layout visible and showing social icons after taking image
    @BindView(R.id.rl_rlbottomfirst)
    RelativeLayout rlbottomfirst;  // relative layout showing ic_camera_click click button and gallery button on botton sheet
    @BindView(R.id.rl_rlbottomsecond)
    RelativeLayout rlbottomsecond;  // relative layout showing big button
    @BindView(R.id.ChangeCamera)
    ImageView videocamera;

    Drawable d;

    Activity context;
    CameraPreview preview;
    Camera camera;

    //	String path = "/sdcard/KutCamera/cache/images/";
    ExifInterface exif = null;

    Uri fileUriCamera;
    Bitmap bm, copybm;
    File rotatedimagefilepathpictureFile;
    String picturePath = "",
            ImageFinalPath = "",
            ImagePath = "",
            currentTemplatePath = "",
            condition = "",
            getfilepath = "",
            rotatedimagefilepath = "",
            mimeType = "";
    Camera.Parameters param;

    private int cameraId;
    private int rotation;

    @BindView(R.id.iv_fbc)
    ImageView IVfb;    // image view for facebook after taking image used for sharing
    @BindView(R.id.iv_instagramc)
    ImageView IVinstagrams;    // image view for instagram after taking image used for sharing
    @BindView(R.id.iv_snapchatc)
    ImageView IVsnapchats;    // image view for snapchat after taking image used for sharing
    @BindView(R.id.iv_twitterc)
    ImageView IVtwitters;    // image view for snapchat after taking image used for sharing

    @BindView(R.id.preview)
    FrameLayout frame;
    @BindView(R.id.KutCameraFragment)
    SurfaceView surfaceView;
    /*@BindView(R.id.camera_zoon_control)
    ZoomControls zoomControls;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        rlbottomfirst.setVisibility(View.VISIBLE);

        /*three state switch button for flash mode (Start)*/
        int state = mRMTristateSwitch.getState();


        surfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    try {
                        param = camera.getParameters();
                        param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_OFF
                                : Camera.Parameters.FLASH_MODE_OFF);
                        param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        camera.setParameters(param);
                        flashmode = !flashmode;
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e(" ******** Exception 2******** :", "" + e);
                    }

                }
            }
        });

        videocamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(CameraActivity.this, VideoCaptureActivity.class);
                intentImage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentImage);
            }
        });


        Log.e("Camera Flash Mode state :", "" + state);

        mTxtRMTristateSwitchState.setText(state == RMTristateSwitch.STATE_LEFT ?
                "FLASH OFF" : state == RMTristateSwitch.STATE_MIDDLE ? "FLASH AUTO" : "FLASH ON");
        mRMTristateSwitch.addSwitchObserver(new RMTristateSwitch.RMTristateSwitchObserver() {
            @Override
            public void onCheckStateChange(@RMTristateSwitch.State int state) {
                Log.e("Camera Flash Mode SwitchObserver :", "" + state);

                mTxtRMTristateSwitchState.setText(state == RMTristateSwitch.STATE_LEFT ?
                        "FLASH OFF" :
                        state == RMTristateSwitch.STATE_MIDDLE ?
                                "FLASH AUTO" :
                                "FLASH ON");

                ////////////////////////// FLASH MODE (START)////////////////////////////////////
                if (state == RMTristateSwitch.STATE_LEFT) {

                    Log.e("Camera Flash Mode state :", "" + state + "FLASH OFF");

                    if (camera != null) {
                        try {
                            param = camera.getParameters();
                            param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_OFF
                                    : Camera.Parameters.FLASH_MODE_OFF);
                            param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            camera.setParameters(param);
                            flashmode = !flashmode;
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(" ******** Exception 2******** :", "" + e);
                        }

                    }
                } else if (state == RMTristateSwitch.STATE_MIDDLE) {

                    Log.e("Camera Flash Mode state :", "" + state + "FLASH AUTO");

                    if (camera != null) {
                        try {
                            Camera.Parameters param = camera.getParameters();
                            param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_AUTO
                                    : Camera.Parameters.FLASH_MODE_AUTO);
                            camera.setParameters(param);
                            flashmode = !flashmode;
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(" ******** Exception 2******** :", "" + e);
                        }

                    }
                } else if (state == RMTristateSwitch.STATE_RIGHT) {

                    Log.e("Camera Flash Mode state :", "" + state + "FLASH ON");

                    if (camera != null) {
                        try {
                            Camera.Parameters param = camera.getParameters();
                            param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_ON
                                    : Camera.Parameters.FLASH_MODE_ON);
                            camera.setParameters(param);
                            flashmode = !flashmode;
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(" ******** Exception 2******** :", "" + e);
                        }

                    }
                }
                ////////////////////////// FLASH MODE (END)////////////////////////////////////

            }
        });
        /*three state switch button for flash mode (End)*/

        condition = "f";   // this is used for exit the application

        Log.e("onCreate", " Yes");

        context = this;

        preview = new CameraPreview(this, surfaceView);
        frame.addView(preview);
        preview.setKeepScreenOn(true);



        /*default ic_camera_click condition when ic_camera_click is display (Start)*/
        if (camera != null) {
            try {
                param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_OFF
                        : Camera.Parameters.FLASH_MODE_OFF);
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(" ******** Exception 2******** :", "" + e);
            }

        }
        /*default ic_camera_click condition when ic_camera_click is display (End)*/


        /*ic_camera_click button click for capture images condition (Start)*/

        CameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                CameraButton.setBackgroundResource(R.drawable.ic_camera_click_two);
//                Toast.makeText(getApplicationContext(), "Perform Image Capture", Toast.LENGTH_SHORT).show();
                YoYo.with(Techniques.BounceIn)
                        .duration(400)
                        .playOn(CameraButton);

                try {
                    takeFocusedPicture();
                } catch (Exception e) {
                    Log.e(" ******** Exception 1******** :", "" + e);
                }
                CameraButton.setClickable(false);

            }
        });
        /*ic_camera_click button click for capture images condition (End)*/

        /*condition for check mobile has flash feature or not (Start)*/
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            Log.e(" %%%%%%%%%% You Don't have flash %%%%%%%%%% :", "No");
            mRMTristateSwitch.setVisibility(View.GONE);
            mTxtRMTristateSwitchState.setVisibility(View.GONE);
            flash.setVisibility(View.GONE);
        } else {
            mRMTristateSwitch.setVisibility(View.VISIBLE);
            mTxtRMTristateSwitchState.setVisibility(View.VISIBLE);
            flash.setVisibility(View.VISIBLE);
            Log.e(" %%%%%%%%%% You have flash %%%%%%%%%% :", "Yes");
        }
        /*condition for check mobile has flash feature or not (End)*/

        /*condition for check mobile has more than one ic_camera_click or not (Start)*/
        if (Camera.getNumberOfCameras() > 1) {
            Rotatecamera.setVisibility(View.VISIBLE);
            Log.e(" %%%%%%%%%% You have Multiple Camera %%%%%%%%%% :", "Yes");
        } else {
            Rotatecamera.setVisibility(View.GONE);
            Log.e(" %%%%%%%%%% You Don't have Multiple Camera %%%%%%%%%% :", "No");
        }
        /*condition for check mobile has more than one ic_camera_click or not (End)*/
//        surfaceHolder = surfaceView.getHolder();
        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        Rotatecamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Rotate ic_camera_click Clicked :", "Yes");

                YoYo.with(Techniques.RotateIn)
                        .duration(300)
                        .playOn(findViewById(R.id.iv_rotate_camera));

                int id = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT
                        : Camera.CameraInfo.CAMERA_FACING_BACK);
                if (!openCamera(id)) {
//                    alertCameraDialog();
                    Log.e("ic_camera_click Error :", "YEs");
                }

                Log.e("ic_camera_click id :", "" + cameraId);

            }
        });

        galleryselect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                condition = "d"; // used for back pressed
//                ic_camera_click.release();

                Log.e(" ********** START SELECTION FROM Gallery FOR IMAGE **********", "YES");

                // SDK < API11
                if (Build.VERSION.SDK_INT > 19) {

                    Log.e("SDK version is above 19 :", " " + Build.VERSION.SDK_INT);


                    // Create the Intent for Image Gallery.
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/* video/*");
                    condition = "d";
                    // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                    startActivityForResult(i, 1);

                } else if (Build.VERSION.SDK_INT <= 19 && Build.VERSION.SDK_INT > 15) {
                    Log.e("SDK version is below 20 :", " " + Build.VERSION.SDK_INT);

                    Intent intent = new Intent();
                    intent.setType("image/* video/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), MY_INTENT_CLICK);

//                    selectImageVideo();
                } else {
                    Toast.makeText(getApplicationContext(), "oops...Your Phone is Too Old", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT > 19) {

                Log.e("onActivityResult for SDK Above 19 :", "  Your SDK is " + "" + Build.VERSION.SDK_INT);

                ContentResolver cr;
                fileUriCamera = data.getData();
                cr = this.getContentResolver();
                mimeType = cr.getType(fileUriCamera);
                Log.e(" --------------- mimeType --------------- :", "" + mimeType);

                    /*///////////////////////////////////////////////////////////////////////*/
                fileUriCamera = data.getData();
                Log.e("Gallery  URI :", "" + fileUriCamera);

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(fileUriCamera, projection, null, null, null);
                cursor.moveToFirst();
                picturePath = cursor.getString(cursor.getColumnIndex(projection[0]));

                cursor.close();
                Log.e("xxxxxxxxxxxxGallery  picturePath :", "" + picturePath);


                if ((picturePath.contains(".mp4")) || (picturePath.contains(".mpeg")) || (picturePath.contains(".avi")) || (picturePath.contains(".3gp"))) {

                    ImageFinalPath = fileUriCamera.getPath();
                    Log.e("Gallery Video Final Path :", "" + ImageFinalPath);

                    Intent intentVideoPaths = new Intent(CameraActivity.this, FinalVideoActivity.class);
                    intentVideoPaths.putExtra("VIDEOPATH", picturePath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                    intentVideoPaths.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);


                    startActivity(intentVideoPaths);

                } else if ((picturePath.contains(".jpg")) || (picturePath.contains(".png")) || (picturePath.contains(".etc"))) {

                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUriCamera);
                        Log.e("Gallery  Path", fileUriCamera.getPath());

                        ImageFinalPath = fileUriCamera.getPath();
                        Log.e("Gallery Final Path :", "" + ImageFinalPath);

                        Intent intentImagePaths = new Intent(CameraActivity.this, FinalImageActivity.class);
                        intentImagePaths.putExtra("IMAGEPATH", picturePath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                        intentImagePaths.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);


                        startActivity(intentImagePaths);

                    } catch (IOException e) {
                        Log.e(" ******** Exception 16******** :", "" + e);
                        e.printStackTrace();
                    }
                }
            }
            if (Build.VERSION.SDK_INT <= 19) {

                Log.e("onActivityResult for SDK below 20 :", "  Your SDK is " + "" + Build.VERSION.SDK_INT);

                    /*///////////////////////////////////////////////////////////////////////*/
                fileUriCamera = data.getData();
                Log.e("Gallery  URI :", "" + fileUriCamera);

                ImageFinalPath = fileUriCamera.getPath();
                Log.e("Gallery Final Path :", "" + ImageFinalPath);

                if ((ImageFinalPath.contains(".mp4")) || (ImageFinalPath.contains(".MP4"))
                        ||
                        (ImageFinalPath.contains(".mpeg")) || (ImageFinalPath.contains(".MPEG"))
                        ||
                        (ImageFinalPath.contains(".avi")) || (ImageFinalPath.contains(".AVI"))
                        ||
                        (picturePath.contains(".3gp")) || (picturePath.contains(".3GP"))
                        ||
                        (picturePath.contains(".3gpp")) || (picturePath.contains(".3GPP"))
                        ||
                        (picturePath.contains(".aac")) || (picturePath.contains(".AAC"))
                        ||
                        (picturePath.contains(".mkv")) || (picturePath.contains(".MKV"))
                        ||
                        (picturePath.contains(".webm")) || (picturePath.contains(".WEBM"))
                        ) {

                    Log.e("Gallery Final Path Contains Video File :", "YES" + "" + ImageFinalPath);

                    Intent intentVideoPaths = new Intent(CameraActivity.this, FinalVideoActivity.class);
                    intentVideoPaths.putExtra("VIDEOPATH", ImageFinalPath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                    intentVideoPaths.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intentVideoPaths);

                } else if ((ImageFinalPath.contains(".jpg")) || (ImageFinalPath.contains(".JPG"))
                        ||
                        (ImageFinalPath.contains(".jpeg")) || (ImageFinalPath.contains(".JPEG"))
                        ||
                        (ImageFinalPath.contains(".webp")) || (ImageFinalPath.contains(".WEBP"))
                        ||
                        (ImageFinalPath.contains(".bmp")) || (ImageFinalPath.contains(".BMP"))
                        ||
                        (ImageFinalPath.contains(".png")) || (ImageFinalPath.contains(".PNG"))
                        ||
                        (ImageFinalPath.contains(".gif")) || (ImageFinalPath.contains(".GIF"))) {

                    Log.e("Gallery Final Path Contains Image File :", "YES" + "" + ImageFinalPath);

                    Intent intentImagePaths = new Intent(CameraActivity.this, FinalImageActivity.class);
                    intentImagePaths.putExtra("IMAGEPATH", ImageFinalPath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                    intentImagePaths.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);


                    startActivity(intentImagePaths);

                }
            }
        }
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(" ******** Exception 3******** :", "" + e);
        }
        if (camera != null) {
            try {
                setUpCamera(camera);

                preview = new CameraPreview(this, surfaceView);
                frame.addView(preview);
                preview.setKeepScreenOn(true);

                camera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(preview.mHolder);
                camera.startPreview();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(" ******** Exception 4******** :", "" + e);
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
//            surfaceView.setVisibility(View.VISIBLE);
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
//            surfaceView.setVisibility(View.GONE);
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Camera.Parameters params = c.getParameters();

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // release the ic_camera_click immediately on pause event
            //releaseCamera();


            camera.release();
            preview.mHolder.removeCallback(preview);
            camera = null;
            camera.stopPreview();
            camera.setPreviewCallback(null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(" ******** Exception 5******** :", "" + e);
        }

//        releaseCamera();

        Log.e("onPause", "YES");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("onStop", "YES");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (camera == null) {
            camera = Camera.open();
            camera.startPreview();
            camera.setErrorCallback(new Camera.ErrorCallback() {
                public void onError(int error, Camera mcamera) {

                    camera.release();
                    camera = null;
                    camera = Camera.open();
                    Log.e("Camera died", "error ic_camera_click");

                }
            });
        }
        Log.e("onRestart", "YES");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("onStart", "YES");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("onResume Called : ", "YES");

        if (currentTemplatePath.equalsIgnoreCase("")) {

            if (camera == null) {
                camera = Camera.open();
                camera.startPreview();
                camera.setErrorCallback(new Camera.ErrorCallback() {
                    public void onError(int error, Camera mcamera) {

                        camera.release();
                        camera = null;
                        camera = Camera.open();
                        Log.e("Camera died", "error ic_camera_click");

                    }
                });
            }
            if (camera != null) {
                if (Build.VERSION.SDK_INT >= 14)
                    setCameraDisplayOrientation(context, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
                preview.setCamera(camera);
            }
        } else {

            Log.e("currentTemplatePath Found :", "" + currentTemplatePath);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("onDestroy", "YES");
    }

    private void releaseCamera() {
        // stop and release ic_camera_click
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        camera.setPreviewCallback(null);
    }


    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            try {
                camera.takePicture(mShutterCallback, null, jpegCallback);
            } catch (Exception e) {
                Log.e(" ******** Exception 6******** :", "" + e);
            }

        }
    };

    Camera.ShutterCallback mShutterCallback = new ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    public void takeFocusedPicture() {
        camera.autoFocus(mAutoFocusCallback);

    }

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //////////////////////////////////////////////////
            //make a new picture file
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                //write the file
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

                Log.e(" ******** Exception 7******** :", "" + e);
            } catch (IOException e) {
                Log.e(" ******** Exception 8******** :", "" + e);
            }

            picturePath = pictureFile.getAbsolutePath();

            Log.e("absulate sky>>>>>>>>>>>", "" + picturePath);

            //////////////////

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;

            options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

            options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


            bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            Log.e(" BitmapFactory.decodeByteArray : ", "" + bm + "" + data.toString() + "" + data.length + "" + options);
            Log.e(" BitmapFactory.decodeByteArray : ", "" + bm + "" + data + "" + data.length + "" + options);
            Log.e(" BitmapFactory.decodeByteArray : ", "" + bm);

            try {
                exif = new ExifInterface(picturePath);
                Log.e(" ExifInterface PicturePath : ", "" + exif);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e(" ******** Exception 9******** :", "" + e);
                e.printStackTrace();
            }


            /*condition for rotating image for front ic_camera_click and back ic_camera_click (Start)*/
            if (cameraId == 1) {//*********** front ic_camera_click ***********
                Log.e(" **************** ic_camera_click ****************  id is 1 for EXIF:", "Yes");
                try {
                    Log.e("EXIF value", "" + exif.getAttribute(ExifInterface.TAG_ORIENTATION));

                    if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("1")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 1 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 270);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 8 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 270);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 3 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 270);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 0 >>>>>>>>>>>>>>>:", "" + exif);
                        bm = rotate(bm, 270);
                    }


                } catch (Exception e) {

                    Log.e(" ******** Exception 10******** :", "" + e);
                }
            } else if (cameraId == 0) {    //*********** back ic_camera_click ***********
                Log.e(" **************** ic_camera_click ****************  id is 0 for EXIF:", "Yes");
                try {
                    Log.e("EXIF value", "" + exif.getAttribute(ExifInterface.TAG_ORIENTATION));

                    if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("1")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 1 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 90);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 8 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 90);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 3 >>>>>>>>>>>>>>>:", "" + exif);

                        bm = rotate(bm, 90);
                    } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {

                        Log.e("<<<<<<<<<<<<<<< EXIF value 0 >>>>>>>>>>>>>>>:", "" + exif);
                        bm = rotate(bm, 90);
                    }


                } catch (Exception e) {
                    Log.e(" ******** Exception 11******** :", "" + e);
                }
            }
            /*condition for rotating image for front ic_camera_click and back ic_camera_click (End)*/

            if (exif == null) {
                setbgimage.setVisibility(View.GONE);
            } else {

/* /////////////////////////////////// this method is used for creating copy of new bitmap with correct orientation ////////////////////////////////////////////////////////////*/
/* /////////////////////////////////// this method is used for creating copy of new bitmap with correct orientation ////////////////////////////////////////////////////////////*/


                /*copy bitmap to new bitmap for saving this image in correct orientation (Start)*/
                copybm = bm.copy(bm.getConfig(), true);
                Log.e(" copy bitmap copybm : ", "" + copybm);

                rotatedimagefilepathpictureFile = getOutputMediaFilee();
                if (rotatedimagefilepathpictureFile == null) {
                    return;
                }
                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(rotatedimagefilepathpictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {

                    Log.e(" ******** Exception 12******** :", "" + e);
                } catch (IOException e) {
                    Log.e(" ******** Exception 13******** :", "" + e);
                }

                rotatedimagefilepath = rotatedimagefilepathpictureFile.getAbsolutePath();
                Log.e("<<<<<<<<<<<<<<<  rotatedimagefilepath  >>>>>>>>>>>>>> ", "" + rotatedimagefilepath);

                FileOutputStream out = null;
                try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    out = new FileOutputStream(rotatedimagefilepathpictureFile);
                    copybm.compress(Bitmap.CompressFormat.JPEG, 100, out); // create default image size by using quality of image is 100 %

            /*this code is used to show the image size in logcat (Start)*/
                    int bitmapByteCount = BitmapCompat.getAllocationByteCount(copybm);
                    long bitmapByteCountKB = bitmapByteCount / 1024;  // count in kb
                    float bitmapByteCountMB = bitmapByteCountKB / 1024;  // count in mb
                    String calString = Float.toString(bitmapByteCountMB);
                    float bitmapByteCountGB = bitmapByteCountMB / 1024;  // count in gb

                    Log.e("bitmap image size in Byte :", "" + bitmapByteCount);

                    Log.e("bitmap image size in kb :", "" + bitmapByteCountKB);

                    Log.e("bitmap image size in MB :", "" + bitmapByteCountMB);

                    Log.e("bitmap image size in GB :", "" + bitmapByteCountGB);

                    long length = rotatedimagefilepathpictureFile.length();
                    length = length / 1024;
                    getfilepath = rotatedimagefilepathpictureFile.getPath();
            /*this code is used to show the image size in logcat (End)*/

                    Log.e(" file information :", "File Path :" + getfilepath + "File Size :" + length + "KB");

                    Log.e("rotatedimagefilepath absulate sky>>>>>>>>>>>", "" + rotatedimagefilepath);

                } catch (Exception e) {
                    Log.e(" ******** Exception 14******** :", "" + e);
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();  // Not really required
                            out.close();  // do not forget to close the stream
                        }
                    } catch (IOException e) {
                        Log.e(" ******** Exception 15******** :", "" + e);
                        e.printStackTrace();
                    }
                }
        /*copy bitmap to new bitmap for saving this image in correct orientation (End)*/


                deleteImage();
                Log.e("deleteImage() call :", "Yes");

                ImagePath = rotatedimagefilepath;
                Log.e("<<<<<<<<<<<<<<<<new rotated ImagePath call>>>>>>>>>>>>>>>>>>>> :", "" + ImagePath);

                Intent intentImagePath = new Intent(CameraActivity.this, FinalImageActivity.class);
                intentImagePath.putExtra("IMAGEPATH", ImagePath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                startActivity(intentImagePath);

                llbottomsecond.setVisibility(View.GONE);  // social icon visible in botton sheet
                setbgimage.setVisibility(View.GONE);  // image relative layout visibility gone
                finalimage.setVisibility(View.GONE); // image view visibility visible

//                finalimage.setImageBitmap(bm);  // set captured image to the image view for sharing

                CameraButton.setClickable(false);  // disable again capture button right now
                galleryselect.setClickable(false);  // disable gallery select button right now

//                ic_camera_click.release();  // release ic_camera_click for other use right now
            }


        }
    };

    public void deleteImage() {
        File fdelete = new File(picturePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + picturePath);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + picturePath);
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }

    //////////////// this is used for saving image for temprory purpose only //////////////////
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCliqueApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCliqueApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
    //////////////// this is used for saving image for temprory purpose only //////////////////

    //////////////// this is used for saving image for share purpose //////////////////
    private static File getOutputMediaFilee() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCliqueApp/CliqueImage");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("MyCliqueApp/CliqueImage", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "Clique" + timeStamp + ".jpg");

        return mediaFile;
    }
    //////////////// this is used for saving image for share purpose //////////////////

    @Override
    public void onBackPressed() {

        if (condition.equalsIgnoreCase("f")) {
            finish();
            Log.e(" %%%%%%%%%%%%%%%%%% condition = f %%%%%%%%%%%%%%%%%% ", "TRUE");
        } else if (condition.equalsIgnoreCase("d")) {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = d %%%%%%%%%%%%%%%%%% ", "TRUE");

            Intent intent = new Intent(getApplicationContext(),
                    CameraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
    }
}
