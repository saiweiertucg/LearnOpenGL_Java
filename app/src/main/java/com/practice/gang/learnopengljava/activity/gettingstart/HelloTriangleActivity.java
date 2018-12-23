package com.practice.gang.learnopengljava.activity.gettingstart;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.practice.gang.learnopengljava.renderer.HelloTriangleRenderer;

/**
 * Created by gang on 2018/12/22.
 */

public class HelloTriangleActivity extends AppCompatActivity {

    private final int CONTEXT_CLIENT_VERSION = 3;
    private static final String TAG = "HelloTriangleActivity";
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (detectOpenGLES30()) {
            mGLSurfaceView = getGLSurfaceView();
            mGLSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
            mGLSurfaceView.setRenderer(new HelloTriangleRenderer());
            setContentView(mGLSurfaceView);
            String title = getClass().getSimpleName();
            setTitle(title.substring(0, title.indexOf("Activity")));
        } else {
            Log.i(TAG, "CONTEXT_CLIENT_VERSION is lower");
            finish();
        }
    }

    private boolean detectOpenGLES30() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0X30000);
    }

    GLSurfaceView getGLSurfaceView() {
        return new GLSurfaceView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
    }
}
