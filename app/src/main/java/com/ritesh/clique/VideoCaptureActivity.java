package com.ritesh.clique;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.IOException;

/**
 * Created by Umesh on 24/5/16.
 */


public class VideoCaptureActivity extends Activity {

    /*https://github.com/umesh-kushwaha/Android_Video_Recording_portrait*/
    /*https://github.com/umesh-kushwaha/Android_Video_Recording_portrait*/
    /*https://github.com/umesh-kushwaha/Android_Video_Recording_portrait*/
    private Camera mCamera;
    private VideoCapturePreview mPreview;
    private MediaRecorder mediaRecorder;
    private ImageView capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM = 3;
    private static final int RC_HANDLE_RECORD_AUDIO_PERM = 4;
    private String TAG = "test";
    private TextView mTimerTv;
    private String filePath = "", fileabsolutepath = "", condition = "";
    private Intent intent;
    private long mCountDownTimer = 16000;
    public static int cameraId = -1;
    File mediaFile;
    int file_size;
    Uri videouri;
    ImageView imagecamera;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        mTimerTv = (TextView) findViewById(R.id.tvTimer);
        imagecamera = (ImageView) findViewById(R.id.ChangeCameraa);
        imagecamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(VideoCaptureActivity.this, CameraActivity.class);
                intentImage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                YoYo.with(Techniques.RotateIn)
                        .duration(300)
                        .playOn(findViewById(R.id.ChangeCameraa));
                startActivity(intentImage);

            }
        });

        condition = "a";

/*        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/testVideo.mp4");
        filePath = Uri.fromFile(mediaFile).getPath();
        fileabsolutepath = mediaFile.getAbsolutePath();
        intent = getIntent();
        Log.e("filePath:", "" + filePath);
        Log.e("filePath:", "" + filePath);
        Log.e("filePath:", "" + filePath);

        Log.e("fileabsolutepath:", "" + fileabsolutepath);
        Log.e("fileabsolutepath:", "" + fileabsolutepath);
        Log.e("fileabsolutepath:", "" + fileabsolutepath);*/

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int sdCard = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (sdCard == PackageManager.PERMISSION_GRANTED) {
            int recordAudio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (recordAudio == PackageManager.PERMISSION_GRANTED) {
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    initialize();
                    initializeCamera();
                } else {
                    requestCameraPermission();
                }
            } else {
                requestRecordAudioPermission();
            }
        } else {
            requestSDCardPermission();
        }

    }

    private int findFrontFacingCamera() {
        cameraId = -1;
        // Search for the front facing ic_camera_click
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        cameraId = -1;
        // Search for the back facing ic_camera_click
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every ic_camera_click check
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a ic_camera_click!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED)
            initializeCamera();
    }

    private void initializeCamera() {
        if (mCamera == null) {
            // if the front facing ic_camera_click does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing ic_camera_click found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);

        mPreview = new VideoCapturePreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        capture = (ImageView) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (ImageView) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);
        capture.setSelected(true);
    }

    OnClickListener switchCameraListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                YoYo.with(Techniques.RotateIn)
                        .duration(300)
                        .playOn(findViewById(R.id.button_ChangeCamera));
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old ic_camera_click instance
                    // switch ic_camera_click, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {
                    Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one ic_camera_click!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };

    public void chooseCamera() {
        // if the ic_camera_click preview is the front
        if (cameraFront) {
            cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                mPreview.refreshCamera(mCamera);
            }
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release ic_camera_click in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has ic_camera_click
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    boolean recording = false;
    OnClickListener captrureListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {
                YoYo.with(Techniques.BounceInDown)
                        .duration(400)
                        .playOn(capture);
                // stop recording and release ic_camera_click
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                file_size = Integer.parseInt(String.valueOf(mediaFile.length() / 1024));

                Log.e(" mediaRecorder file_size :", "" + file_size);

                /*this toast are use for see the capture video detail like "path" and "size"*/

//                Toast.makeText(VideoCaptureActivity.this, "Video size!" + "" + "\n" + file_size, Toast.LENGTH_LONG).show();
//                Toast.makeText(VideoCaptureActivity.this, "Video captured!" + "" + "\n" + fileabsolutepath, Toast.LENGTH_LONG).show();

                /*this toast are use for see the capture video detail like "path" and "size"*/

                Intent intentVideoPath = new Intent(VideoCaptureActivity.this, FinalVideoActivity.class);
                intentVideoPath.putExtra("VIDEOPATH", fileabsolutepath);
//                intentImagePath.putExtra("IMAGEPATHs", picturePath);
                startActivity(intentVideoPath);

                recording = false;
                capture.setSelected(true);

            } else {
                if (!prepareMediaRecorder()) {
                    Toast.makeText(VideoCaptureActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table

                        try {
                            YoYo.with(Techniques.BounceIn)
                                    .duration(400)
                                    .playOn(capture);
                            capture.setSelected(false);
                            mediaRecorder.start();
                            startCountDownTimer();
                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });

                recording = true;
            }
        }
    };

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock ic_camera_click for later use
        }
    }

    private boolean prepareMediaRecorder() {

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOrientationHint(VideoCapturePreview.rotate);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/CliqueVideo.mp4");

        /*mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/testVideo.mp4");*/
        filePath = Uri.fromFile(mediaFile).getPath();
        fileabsolutepath = mediaFile.getAbsolutePath();
        intent = getIntent();
        Log.e("filePath:", "" + filePath);
        Log.e("fileabsolutepath:", "" + fileabsolutepath);

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setMaxDuration(15000); // Set max duration 15 sec.
        mediaRecorder.setMaxFileSize(1000000000); // Set max file size 100M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        // stop and release ic_camera_click
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * Handles the requesting of the ic_camera_click permission.
     */
    private void requestCameraPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_CAMERA_PERM);

    }

    private void requestSDCardPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "SDCard permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM);

    }

    private void requestRecordAudioPermission() {
        Log.w(VideoCaptureActivity.class.getName(), "Record audio permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(VideoCaptureActivity.this, permissions,
                RC_HANDLE_RECORD_AUDIO_PERM);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RC_HANDLE_CAMERA_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //cameraView.setVisibility(View.VISIBLE);
                    initialize();
                    initializeCamera();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("This application cannot record video because it does not have the ic_camera_click permission.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("This application cannot record video because it does not have the write external storage permission.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;
            case RC_HANDLE_RECORD_AUDIO_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage("This application cannot record video because it does not have the record audio permission.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                }
                break;

        }
    }


    private void startCountDownTimer() {
        new CountDownTimer(mCountDownTimer, 1000) {

            public void onTick(final long millisUntilFinished) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTv.setText("" + millisUntilFinished / 1000);
                    }
                });

            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mTimerTv.setText("done!");
                        mTimerTv.setText("15");
                        if (recording) {
                            // stop recording and release ic_camera_click
                            mediaRecorder.stop(); // stop the recording

                            releaseMediaRecorder(); // release the MediaRecorder object
                            Toast.makeText(VideoCaptureActivity.this, "Video captured!", Toast.LENGTH_LONG).show();
                            capture.setSelected(true);
                            recording = false;
                        }
                    }
                });

            }
        }.start();
    }

    @Override
    public void onBackPressed() {

        if (condition.equalsIgnoreCase("a")) {

            Log.e(" %%%%%%%%%%%%%%%%%% condition = a %%%%%%%%%%%%%%%%%% ", "TRUE");
            Intent intentback = new Intent(VideoCaptureActivity.this, CameraActivity.class);
            intentback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intentback);

        }
    }

}
