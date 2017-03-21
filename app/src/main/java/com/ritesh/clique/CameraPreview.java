package com.ritesh.clique;

/**
 * @author Jose Davis Nidhin
 */

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ritesh.clique.CameraActivity;

import java.io.IOException;
import java.util.List;



@SuppressWarnings("deprecation")
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
//    private final String TAG = "Preview";
    private final String TAG = "Preview";

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    int heightmax;
    int widthmax;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;
    CameraActivity cameraActivity;

    CameraPreview(Context context, SurfaceView sv) {
        super(context);

        Log.e("CameraPreview called :", "YES");

        mSurfaceView = sv;
//        addView(mSurfaceView);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {

        Log.e("setCamera called :", "YES");

        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPictureSizes();
            requestLayout();

            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                mCamera.setParameters(params);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the ic_camera_click preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {

            mPreviewSize = maxSize();

        }
    }

    public Size maxSize() {

        Size sizeMax = mSupportedPreviewSizes.get(0);

        for (Size size : mSupportedPreviewSizes) {
            if (size.height * size.width > sizeMax.width * sizeMax.height) {
                sizeMax = size;

            }
        }

        return sizeMax;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the ic_camera_click and tell it where
        // to draw.

        Log.e("surfaceCreated called :", "YES");

        try {

            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                startFaceDetection(); // start face detection feature
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.

        Log.e("surfacedestroyed called :", "YES");

    }

    public void startFaceDetection() {
        // Try starting Face Detection
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0) {
//            Toast.makeText(CameraActivity.this, "Max Num Faces Allows: " + params.getMaxNumDetectedFaces(), Toast.LENGTH_LONG).show();

           Log.e("Max Num Faces Allows:", "" +params.getMaxNumDetectedFaces() );
            // ic_camera_click supports face detection, so can start it:
//            mCamera.startFaceDetection();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Log.e("surfaceChanged called :", "YES");

        if (mCamera != null) {


            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();
            mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
            startFaceDetection(); // start face detection feature

            mCamera.setParameters(parameters);
            mCamera.startPreview();

        }
    }

    class MyFaceDetectionListener implements Camera.FaceDetectionListener {
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length == 0) {
                Log.e("mTextViewFace3Coordinates.setText :", "Confidence Score: 0%\"");
            } else if (faces.length > 0) {
                int left0   = faces[0].rect.left;
                int top0    = faces[0].rect.top;
                int right0  = faces[0].rect.right;
                int bottom0 = faces[0].rect.bottom;
                Log.e("mTextViewFace0Coordinates.setText :",
                        "Face Rectangle:" +
                                left0 + "," + top0 + right0 + "," + bottom0 + ",");

                Log.e("mTextViewFace3Coordinates.setText :", "Confidence Score:" + faces[0].score + "%");


                try {
                    int leftEyeX = faces[0].leftEye.x;
                    int leftEyeY = faces[0].leftEye.y;
                    int rightEyeX = faces[0].rightEye.x;
                    int rightEyeY = faces[0].leftEye.y;
//                    mTextViewFace1Coordinates.setText("Left,Right Eye: (" + leftEyeX + "," + leftEyeY + "), (" + rightEyeX + "," + rightEyeY + ")");

                    Log.e("mTextViewFace1Coordinates.setText :", "\"Left,Right Eye:" + leftEyeX + ","
                    + leftEyeY + "" + rightEyeX + "," + rightEyeY + "");

                } catch (Exception e) {
//                    mTextViewFace1Coordinates.setText("Left,Right Eye: not supported");

                    Log.e( "mTextViewFace1Coordinates.setText :","Left,Right Eye: not supported" );
                }

                try {
                    int mouthX = faces[0].mouth.x;
                    int mouthY = faces[0].mouth.y;
//                    mTextViewFace2Coordinates.setText("Mouth: (" + mouthX + "," + mouthY + ")");

                    Log.e("mTextViewFace2Coordinates.setText :", "Mouth: " + mouthX + "," + mouthY + ",");
                } catch (Exception e) {
//                    mTextViewFace2Coordinates.setText("Mouth: not supported");
                }
            }
        }
    }

}
