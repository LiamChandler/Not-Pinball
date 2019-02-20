package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class NotPinball extends AppCompatActivity
{
	Random rand = new Random();
	MyGestureListener gestureListener;
	GestureDetectorCompat gestureDetector;
	SensorManager sensorMgr;
	Sensor accelerometer;
	AccelerometerListener listener;
	
	public static int screenWidth, screenHeight, gameLength, gameOffset, maxSpeed = 10, playerHealth;
	int radiusPlayer, radiusObstacle;
	public static float cameraPos = 0;
	
	List<npbObject> sprites;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		gestureListener = new MyGestureListener();
		gestureDetector = new GestureDetectorCompat(this, gestureListener);
		
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listener = new AccelerometerListener();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels + 100;
		gameLength = screenHeight * 4;
		screenWidth = displayMetrics.widthPixels;
		radiusPlayer = displayMetrics.densityDpi / 8;
		radiusObstacle = displayMetrics.densityDpi / 10;
		
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
		GraphicsView gv = new GraphicsView(this);
		gv.setY(gameOffset);
		gv.setBackgroundColor(Color.GRAY);
		layout.addView(gv, params);
		
		create();
		
		layout.setBackgroundColor(Color.BLACK);
		setContentView(layout);
	}
	
	private void create()
	{
		sprites = new ArrayList<>();
		
		cameraPos = 0;
		playerHealth = 50;
		
		sprites.add(new Player(screenWidth * 0.5f, 100, radiusPlayer));
		
		generateObstacles(30);
	}
	
	
	private void generateObstacles(int number)
	{
		float minDist = (radiusPlayer * 4) + (radiusObstacle * 2);
		npbObject[] tmpObjects = new npbObject[number];
		boolean noClashes = false, clash = false;
		
		for(int i = 0; i < number; i++)
		{
			float obX = rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle;
			float obY = rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle;
			while(!noClashes)
			{
				float distToPlayer = (float) Math.hypot(Math.abs(sprites.get(0).getX() - obX), Math.abs(sprites.get(0).getY() - obY));
				if(distToPlayer < (radiusPlayer * 4))
					clash = true;
				else
				{
					if(tmpObjects[0] == null)
						clash = false;
					else
						for(npbObject o : tmpObjects)
						{
							if(o != null)
							{
								float dist = (float) Math.hypot(Math.abs(o.getX() - obX), Math.abs(o.getY() - obY));
								if(dist < minDist)
								{
									clash = true;
									break;
								}
								else
									clash = false;
							}
						}
				}
				if(!clash)
					noClashes = true;
				else
				{
					obX = rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle;
					obY = rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle;
				}
			}
			noClashes = false;
			
			int randNum = rand.nextInt(3); // total 5
			
			if(randNum == 0)
				tmpObjects[i] = new ObstacleSolid(obX, obY, radiusObstacle);
			else if (randNum == 1)
			{
				npbObject tmp = new ObstacleMoving(obX, obY, radiusObstacle);
				tmp.setdX(((rand.nextFloat()*maxSpeed * 0.5f) - maxSpeed * 0.25f) + 2.5f);
				tmp.setdY(((rand.nextFloat()*maxSpeed) * 0.25f) - (maxSpeed * 0.125f));
				tmpObjects[i] = tmp;
			}
			else if(randNum == 2)
				tmpObjects[i] = new ObstacleSpike(obX, obY, radiusObstacle);
				
			
			sprites.add(tmpObjects[i]);
		}
	}
	
	class AccelerometerListener implements SensorEventListener
	{
		@Override
		public void onSensorChanged(SensorEvent sensorEvent)
		{
            sprites.get(0).moddX(-sensorEvent.values[0]/5);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int i)
		{
		}
	}
	
	
	public class GraphicsView extends View
	{
		
		public GraphicsView(Context c)
		{
			super(c);
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			for (int i = 0; i < sprites.size(); i++)
			{
//				if (true)//(sprites.get(i).getX() > cameraPos - (screenHeight * 0.1) && sprites.get(i).getX() < cameraPos + (screenHeight * 1.1f)) || i==0)		Optimization code for not rendering everything
//				{
					sprites.get(i).update(sprites);
					sprites.get(i).draw(canvas);
//				}
			}
			
			invalidate();
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			gestureDetector.onTouchEvent(event);
			return true;
		}
	}
	
	class MyGestureListener implements GestureDetector.OnGestureListener
	{
		@Override
		public boolean onDown(MotionEvent e)
		{
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e)
		{
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			return false;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e)
		{
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
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
		create();
		sensorMgr.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		Log.d("NPB", "OnResume");
	}
}
