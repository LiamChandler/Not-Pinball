package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
	TextView currentUser;
	UserManagement manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		manager = new UserManagement(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		currentUser = findViewById(R.id.textCurrentUser);
		if(manager.getLevel(0) != -1)
			currentUser.setText(("Current user: "+manager.getName(0)));
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	public void gamePlay(View v)
	{
		Log.d("NPB", "gamePlay");
		startActivity(new Intent(this, NotPinball.class));
	}
	
	public void gameUserManager(View v)
	{
		Log.d("NPB", "gameUserManager");
		startActivity(new Intent(this, UserManager.class));
	}
	
	public void gameViewHighscores(View v)
	{
		Log.d("NPB", "gameViewHighscores");
		startActivity(new Intent(this, Highscores.class));
	}
}
