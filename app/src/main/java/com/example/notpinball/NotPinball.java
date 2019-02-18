package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotPinball extends AppCompatActivity {
    Random rand = new Random();
    MyGestureListener gestureListener;
    GestureDetectorCompat gestureDetector;
    SensorManager sensorMgr;
    Sensor accelerometer;
    AccelerometerListener listener;
    public static int radiusTarget, screenWidth, screenHeight, gameHeight, gameOffset;
    int radiusPlayer;

    List<npdObject> sprites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_pinball);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gestureListener = new MyGestureListener();
        gestureDetector = new GestureDetectorCompat(this, gestureListener);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new AccelerometerListener();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        gameHeight = (int) (screenHeight * 0.95);
        gameOffset = screenHeight - gameHeight;
        radiusPlayer = (int) (screenWidth / 17.5);
        radiusTarget = screenWidth / 15;

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, gameHeight);
        GraphicsView gv = new GraphicsView(this);
        gv.setY(gameOffset);
    //    gv.setBackgroundColor(0xFF00AA00);
        layout.addView(gv, params);
    }

    class AccelerometerListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
//            sprites.get(0).setaccdX(-sensorEvent.values[0]);
//            sprites.get(0).setaccdY(sensorEvent.values[1]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }


    public class GraphicsView extends View {

        public GraphicsView(Context c) {
            super(c);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }

    class MyGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        sensorMgr.unregisterListener(listener, accelerometer);
        Log.d("NPB", "OnPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        Log.d("NPB", "OnResume");
    }

}
