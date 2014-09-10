package net.ShortKey;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by hani on 9/9/14.
 */
public class VideoRecorder implements SurfaceHolder.Callback {
    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;
    private String videoFilePath;

    public void start() {

        if (!getVideoFilePath()) {
            Toast.makeText(ApplicationContextProvider.getContext(), "Warning, memory is not accessible", Toast.LENGTH_SHORT).show();
            return;
        }

        windowManager = (WindowManager) ApplicationContextProvider.getContext().getSystemService(Context.WINDOW_SERVICE);
        surfaceView = new SurfaceView(ApplicationContextProvider.getContext());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);
    }

    public void stop() {

        Toast.makeText(ApplicationContextProvider.getContext(), "The file is stored in: " + videoFilePath, Toast.LENGTH_LONG).show();

        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            camera.lock();
            camera.release();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            windowManager.removeView(surfaceView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        mediaRecorder = new MediaRecorder();
        camera.unlock();

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setOutputFile(
                videoFilePath
        );

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mediaRecorder.start();
            Toast.makeText(ApplicationContextProvider.getContext(), "Start to record video", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private boolean getVideoFilePath() {
        File file = new File(Environment.getExternalStorageDirectory() + "/ShortKey");
        if (!file.exists())
            file.mkdir();

        if (file.isDirectory() && file.exists() && file.canWrite()) {
            videoFilePath = file.getPath() + "/" +
                    DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) +
                    ".mp4";
            return true;
        }

        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
