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
import android.widget.Toast;

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
	
	public static int screenWidth, screenHeight, gameLength, maxSpeed, playerHealth, currScore, totalScore, lastTargetScore, textSize, Level,playerStartY;
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
		playerStartY = 100;
		screenWidth = displayMetrics.widthPixels;
		textSize = (int) ((float) (screenHeight) / (float) (screenWidth) * 11);
		maxSpeed = screenHeight / 220;
		Log.d("NPB", "maxSpeed = " + maxSpeed);
		
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
		GraphicsView gv = new GraphicsView(this);
		gv.setBackgroundColor(Color.GRAY);
		layout.addView(gv, params);
		healthDisplay = new TextView(this);
		healthDisplay.setTextColor(Color.WHITE);
		healthDisplay.setX(screenWidth * 0.05f);
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
		
		Level = manager.getLevel(0);
		if(Level == -1)
		{
			Toast.makeText(this,"Please select a user",Toast.LENGTH_LONG).show();
			finish();
		}
		else
		{
			currScore = 0;
			lastTargetScore = 0;
			playerHealth = manager.getCurrentHealth(0);
			totalScore = manager.getCurrentScore(0);
			
			create();
		}
	}
	
	private void create()
	{
		currScore = 0;
		cameraPos = 0;
		run = false;
		
		radiusPlayer = screenWidth / 15;
		radiusObstacle = screenWidth / 18;
		
		int num = (int)(1000/(0.05*Level+20))-50;
		radiusObstacle += num;
		radiusPlayer +=num;
		
		sprites = new ArrayList<>();
		sprites.add(new Player(screenWidth * 0.5f, playerStartY, radiusPlayer,this));
		
		sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.1f, textSize*2, ("Highscore: " + manager.getHighScore(0)), Color.rgb(255, 255, 255),this));
		sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.45f, textSize * 3, "Level", Color.rgb(0, 0, 0), 70,this));
		sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.68f, textSize * 13, Integer.toString(Level), Color.rgb(0, 0, 0), 70,this));
		if(totalScore == 0)
			sprites.add(new textShow(screenWidth / 2f, screenHeight * 0.25f, textSize * 4, "Tap to Start", Color.rgb(0, 0, 0),this));
		
		generateObstacles((int)(15*Math.pow(Level+4,.25)));
		
		sprites.add(new finishLine(gameLength, screenHeight / 20f,this));
		
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
		int 	moving = (int) (number * 0.25),
				spike = (int) (number * 0.10),
				target = (int) (number * 0.15);
		int     solid = number-(moving+spike+target);
		
		int percent = (int) (gameLength - screenHeight * 0.4f) / solid;
		for (int i = 0; i < solid; i++)
			sprites.add(new ObstacleSolid(rand.nextInt(screenWidth), rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle,this));
		
		percent = (int) (gameLength - screenHeight * 0.4f) / moving;
		for (int i = 0; i < moving; i++)
		{
			npbObject tmpO = new ObstacleMoving(rand.nextInt(screenWidth) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle,this);
			tmpO.setdX(((rand.nextFloat() * maxSpeed / 2f) - maxSpeed /4f) + 2.5f);
			tmpO.setdY(((rand.nextFloat() * maxSpeed) / 8f) - (maxSpeed / 16f));
			sprites.add(tmpO);
		}
		
		percent = (int) (gameLength - screenHeight * 0.4f) / spike;
		for (int i = 0; i < spike; i++)
			sprites.add(new ObstacleSpike(rand.nextInt(screenWidth) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle,this));
		
		percent = (int) (gameLength - screenHeight * 0.4f) / target;
		for (int i = 0; i < target; i++)
			sprites.add(new ObstacleTarget(rand.nextInt(screenWidth) + radiusObstacle, rand.nextInt(percent) + (percent * i) + screenHeight * 0.3f, radiusObstacle,this));
	}
	
	public void winRound()
	{
		totalScore += currScore;
		manager.setCurrentScore(0,totalScore,playerHealth);
		manager.save();
		Level++;
		manager.updateLevel(0,Level);
		create();
	}
	
	public void loseRound()
	{
		playerHealth = playerMaxHealth;
		totalScore += currScore;
		manager.addScore(0,totalScore);
		manager.updateLevel(0,Level);
		totalScore = 0;
		lastTargetScore = 0;
		create();
	}
	
	class AccelerometerListener implements SensorEventListener
	{
		float tmp;
		@Override
		public void onSensorChanged(SensorEvent sensorEvent)
		{
			if (!sprites.isEmpty() && run)
				sprites.get(0).moddX(-sensorEvent.values[0] / 5);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int i){}
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
			try
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
					} else
					{
						for (int i = sprites.size() - 1; i >= 0; i--)
						{
							if(sprites.get(i).onScreen())
							{
								if (sprites.get(i).dead)
									tmp.add(sprites.get(i));
								if (run)
									sprites.get(i).update(sprites);
								sprites.get(i).draw(canvas);
								showLivesScore();
							}
						}
						for (npbObject n : tmp)
							sprites.remove(n);
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
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
		sensorMgr.unregisterListener(listener, accelerometer);
		Log.d("NPB", "NotPinball-Pause");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if(run)
		{
			totalScore += currScore;
			manager.addScore(0, totalScore);
		}
		manager.updateLevel(0, Level);
		manager.save();
		Log.d("NPB", "NotPinball-Stop");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		create();
		sensorMgr.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		Log.d("NPB", "NotPinball-Resume");
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