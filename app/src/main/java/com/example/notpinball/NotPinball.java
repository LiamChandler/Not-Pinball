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
import android.widget.TextView;

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
	TextView healthDisplay, scoreDisplay;
	
	public static int screenWidth, screenHeight, gameLength, gameOffset, maxSpeed, playerHealth, currScore, totalScore, textSize, Level;
	int radiusPlayer, radiusObstacle;
	public static float cameraPos;
	
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
		screenHeight = displayMetrics.heightPixels + 200;
		gameLength = screenHeight * 4;
		screenWidth = displayMetrics.widthPixels;
		radiusPlayer = displayMetrics.densityDpi / 8;
		radiusObstacle = displayMetrics.densityDpi / 10;
		textSize = (int)((float)(screenHeight)/(float)(screenWidth) * 10);
		maxSpeed = (int)((float)(screenHeight)/(float)(screenWidth) * 4);
		Log.d("NPB", "maxSpeed = " + maxSpeed);
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
		GraphicsView gv = new GraphicsView(this);
		gv.setY(gameOffset);
		gv.setBackgroundColor(Color.GRAY);
		layout.addView(gv, params);
		healthDisplay = new TextView(this);
		healthDisplay.setTextColor(Color.WHITE);
		healthDisplay.setX(screenWidth * 0.1f);
		healthDisplay.setY(5);
		healthDisplay.setTextSize(textSize);
		layout.addView(healthDisplay);
		scoreDisplay = new TextView(this);
		scoreDisplay.setTextColor(Color.WHITE);
		scoreDisplay.setX(screenWidth * 0.55f);
		scoreDisplay.setY(5);
		scoreDisplay.setTextSize(textSize);
		layout.addView(scoreDisplay);
		layout.setBackgroundColor(Color.BLACK);
		setContentView(layout);
		
		currScore = 0;
		totalScore = 0;
		playerHealth = 50;
		Level = 1;
		
		create();
	}
	
	private void create()
	{
		sprites = new ArrayList<>();
		
		currScore = 0;
		cameraPos = 0;
		
		sprites.add(new Player(screenWidth * 0.5f, 100, radiusPlayer));
		sprites.add(new finishLine(gameLength));
		sprites.add(new textShow(screenWidth / 2f, screenHeight / 2f, textSize * 20, Integer.toString(Level), Color.rgb(0, 0, 0), 70));
		generateObstacles(20 + (5 * Level));
	}
	
	private void showLivesScore()
	{
		String tmp ="Score: " + (currScore + totalScore);
		scoreDisplay.setText(tmp);
		tmp = "Health: " + playerHealth;
		healthDisplay.setText(tmp);
	}
	
	private void generateObstacles(int number)
	{
		int solid = (int)(number * 0.5),
			moving = (int)(number * 0.25),
			spike = (int)(number * 0.15),
			target = (int)(number * 0.10);
		boolean tmp = true;
		while(tmp)
		{
			int total = solid + moving + spike + target;
			if(total != number)
			{
				if(total<number)
					solid++;
				else
					solid--;
			}
			else
				tmp = false;
		}
		
		for(int i = 0; i < solid; i++) sprites.add(new ObstacleSolid(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle,rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle,radiusObstacle));
		
		for(int i = 0; i < moving; i++)
		{
			npbObject tmpO = new ObstacleMoving(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle, rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle, radiusObstacle);
			tmpO.setdX(((rand.nextFloat() * maxSpeed * 0.5f) - maxSpeed * 0.25f) + 2.5f);
			tmpO.setdY(((rand.nextFloat() * maxSpeed) * 0.25f) - (maxSpeed * 0.125f));
			sprites.add(tmpO);
		}
		for(int i = 0; i < spike; i++) sprites.add(new ObstacleSpike(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle,rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle,radiusObstacle));
		
		for(int i = 0; i < target; i++) sprites.add(new ObstacleTarget(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle,rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle,radiusObstacle));
		
		
	}
	
	public void winRound()
	{
		Log.d("NPB", "Win");
		totalScore += currScore;
		Level++;
		create();
	}
	
	public void loseRound()
	{
		Log.d("NPB", "Lose");
		create();
		playerHealth = 50;
		totalScore = 0;
	}
	
	class AccelerometerListener implements SensorEventListener
	{
		@Override
		public void onSensorChanged(SensorEvent sensorEvent)
		{
			sprites.get(0).moddX(-sensorEvent.values[0] / 5);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int i)
		{
		}
	}
	
	
	public class GraphicsView extends View
	{
		List<npbObject> tmp = new ArrayList<>();
		
		public GraphicsView(Context c)
		{
			super(c);
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			if(sprites.get(0).won)
				winRound();
			else if(sprites.get(0).lose)
				loseRound();
			else
			{
				for (int i = sprites.size()-1; i >= 0; i--)
				{
					if (sprites.get(i).dead)
						tmp.add(sprites.get(i));
					
					sprites.get(i).update(sprites);
					sprites.get(i).draw(canvas);
					showLivesScore();
				}
				
				for (npbObject n : tmp)
					sprites.remove(n);
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


/*      OLD CODE

	private void generateObstacles(int number)
	{
		float minDist = (radiusPlayer * 3) + (radiusObstacle * 2);
		npbObject[] tmpObjects = new npbObject[number];
		boolean noClashes = false, clash = false;
		
		for (int i = 0; i < number; i++)
		{
			float obX = rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle;
			float obY = rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle;
			while (!noClashes)
			{
				float distToPlayer = (float) Math.hypot(Math.abs(sprites.get(0).getX() - obX), Math.abs(sprites.get(0).getY() - obY));
				if (distToPlayer < (radiusPlayer * 4))
					clash = true;
				else
				{
					if (tmpObjects[0] == null)
						clash = false;
					else
						for (npbObject o : tmpObjects)
						{
							if (o != null)
							{
								float dist = (float) Math.hypot(Math.abs(o.getX() - obX), Math.abs(o.getY() - obY));
								if (dist < minDist)
								{
									clash = true;
									break;
								} else
									clash = false;
							}
						}
				}
				if (!clash)
					noClashes = true;
				else
				{
					obX = rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle;
					obY = rand.nextInt(gameLength - (radiusObstacle * 2)) + radiusObstacle;
				}
			}
			noClashes = false;
			
			int randNum = rand.nextInt(100);
			int solidChance = 50, movingChance = 65, spikeChance = 90;
			
			if (randNum <= solidChance)
				tmpObjects[i] = new ObstacleSolid(obX, obY, radiusObstacle);
			else if (randNum <= movingChance)
			{
				npbObject tmp = new ObstacleMoving(obX, obY, radiusObstacle);
				tmp.setdX(((rand.nextFloat() * maxSpeed * 0.5f) - maxSpeed * 0.25f) + 2.5f);
				tmp.setdY(((rand.nextFloat() * maxSpeed) * 0.25f) - (maxSpeed * 0.125f));
				tmpObjects[i] = tmp;
			} else if (randNum <= spikeChance)
				tmpObjects[i] = new ObstacleSpike(obX, obY, radiusObstacle);
			else
				tmpObjects[i] = new ObstacleTarget(obX, obY, radiusObstacle);
			
			sprites.add(tmpObjects[i]);
		}
	}
 */
