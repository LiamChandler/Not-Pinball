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
import java.util.Timer;
import java.util.TimerTask;

public class NotPinball extends AppCompatActivity
{
	private Random rand = new Random();
	private GestureDetectorCompat gestureDetector;
	private SensorManager sensorMgr;
	private Sensor accelerometer;
	private AccelerometerListener listener;
	private TextView healthDisplay, scoreDisplay;
	private Timer winTimer = new Timer(), loseTimer = new Timer();
	private Boolean winTimerBool = false, loseTimerBool = false, run;
	UserManagement manager;
	User currentUser;
	
	public static int screenWidth, screenHeight, gameLength, maxSpeed, playerHealth, currScore, totalScore, lastTargetScore, textSize, Level;
	int radiusPlayer, radiusObstacle;
	public static float cameraPos;
	public static final int playerMaxHealth = 5;
	
	List<npbObject> sprites;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		MyGestureListener gestureListener;
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		gestureListener = new MyGestureListener();
		gestureDetector = new GestureDetectorCompat(this, gestureListener);
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listener = new AccelerometerListener();
		manager = new UserManagement(this);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels + 200;
		gameLength = screenHeight * 4;
		screenWidth = displayMetrics.widthPixels;
		textSize = (int) ((float) (screenHeight) / (float) (screenWidth) * 10);
		maxSpeed = screenHeight / 220;
		
		Log.d("NPB", "maxSpeed = " + maxSpeed);
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
		GraphicsView gv = new GraphicsView(this);
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
		lastTargetScore = 0;
		playerHealth = playerMaxHealth;
		Level = manager.getLevel(0);
		
		create();
	}
	
	private void create()
	{
		currScore = 0;
		cameraPos = 0;
		run = false;
		
		radiusPlayer = (int)(screenWidth / 17.5f);
		radiusObstacle = screenWidth / 20;
		for(int i = 1; i < Level; i+=10)
		{
			radiusPlayer --;
			radiusObstacle --;
		}
		
		sprites = new ArrayList<>();
		sprites.add(new Player(screenWidth * 0.5f, 100, radiusPlayer));
		
		generateObstacles(15 + (Level/3));
		Log.d("NPB","NumObstacles " + (15 + (Level/3)));
		
		sprites.add(new finishLine(gameLength, screenHeight / 20f));
		
		sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.32f, textSize * 3, "Level", Color.rgb(0, 0, 0), 70));
		sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.55f, textSize * 20, Integer.toString(Level), Color.rgb(0, 0, 0), 70));
		if(totalScore == 0)
			sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.2f, textSize * 4, "Tap to Start", Color.rgb(0, 0, 0)));
		
		for (int i = sprites.size() - 1; i >= 0; i--)
			sprites.get(i).update(sprites);
		for (int i = sprites.size() - 1; i >= 0; i--)
			sprites.get(i).update(sprites);
	}
	
	private void showLivesScore()
	{
		String tmp = "Score: " + (currScore + totalScore);
		scoreDisplay.setText(tmp);
		tmp = "Health: " + playerHealth;
		healthDisplay.setText(tmp);
	}
	
	private void generateObstacles(int number)
	{
		int solid = (int) (number * 0.5),
				moving = (int) (number * 0.25),
				spike = (int) (number * 0.10),
				target = (int) (number * 0.15);
		boolean tmp = true;
		while (tmp)
		{
			int total = solid + moving + spike + target;
			if (total != number)
			{
				if (total < number)
					solid++;
				else
					solid--;
			} else
				tmp = false;
		}
		
		int percent = (int) (gameLength - screenHeight * 0.3f) / solid;
		for (int i = 0; i < solid; i++)
			sprites.add(new ObstacleSolid(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle));
		
		percent = (int) (gameLength - screenHeight * 0.3f) / moving;
		for (int i = 0; i < moving; i++)
		{
			npbObject tmpO = new ObstacleMoving(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle);
			tmpO.setdX(((rand.nextFloat() * maxSpeed / 2f) - maxSpeed /4f) + 2.5f);
			tmpO.setdY(((rand.nextFloat() * maxSpeed) / 8f) - (maxSpeed / 16f));
			sprites.add(tmpO);
		}
		
		percent = (int) (gameLength - screenHeight * 0.3f) / spike;
		for (int i = 0; i < spike; i++)
			sprites.add(new ObstacleSpike(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle));
		
		percent = (int) (gameLength - screenHeight * 0.3f) / target;
		for (int i = 0; i < target; i++)
			sprites.add(new ObstacleTarget(rand.nextInt(screenWidth - (radiusObstacle * 2)) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle));
	}
	
	public void winRound()
	{
		totalScore += currScore;
		manager.updateHighScore(0,totalScore);
		Level++;
		manager.updateLevel(0,Level);
		create();
	}
	
	public void loseRound()
	{
		playerHealth = playerMaxHealth;
		totalScore += currScore;
		manager.updateHighScore(0,totalScore);
		totalScore = 0;
		lastTargetScore = 0;
		create();
	}
	
	class AccelerometerListener implements SensorEventListener
	{
		@Override
		public void onSensorChanged(SensorEvent sensorEvent)
		{
			if(!sprites.isEmpty() && run)
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
			if (!sprites.isEmpty())
			{
				if (!winTimerBool && sprites.get(0).won)
				{
					winTimer.schedule(new winTimerTask(), (long) 1250);
					winTimerBool = true;
				} else if (!loseTimerBool && sprites.get(0).lose)
				{
					loseTimer.schedule(new loseTimerTask(), (long) 1500);
					sprites.get(0).dying = true;
					loseTimerBool = true;
				}
				else
				{
					for (int i = sprites.size() - 1; i >= 0; i--)
					{
						if (!sprites.isEmpty())
						{
							if (sprites.get(i).dead)
								tmp.add(sprites.get(i));
							if (run)
								sprites.get(i).update(sprites);
							sprites.get(i).draw(canvas);
							showLivesScore();
						}
						else
							break;
					}
					for (npbObject n : tmp)
						sprites.remove(n);
				}
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
			if(!run)
				run = true;
				
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
		manager.save();
		sensorMgr.unregisterListener(listener, accelerometer);
		Log.d("NPB", "OnPause");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		manager.load();
		create();
		sensorMgr.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		Log.d("NPB", "OnResume");
	}
	
	class winTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			winRound();
			winTimerBool = false;
		}
	}
	
	class loseTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			loseRound();
			loseTimerBool = false;
		}
	}
}