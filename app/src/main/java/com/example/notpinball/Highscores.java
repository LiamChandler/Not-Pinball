package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class Highscores extends AppCompatActivity
{
	LinearLayout highscoreView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_highscores);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		UserManagement manager = new UserManagement(this);
		highscoreView = findViewById(R.id.userHighscoreShowView);
		TextView tmpName, tmpHighscore, tmpLevel, tmpAveScore;
		LinearLayout l;
		
		List<User> users = manager.getUsers();
		Collections.sort(users);
		
		int num = users.size(), width = displayMetrics.widthPixels - 32;
		if (num > 0)
		{
			for (int i = -1; i < num; i++)
			{
				tmpName = new TextView(this);
				tmpName.setMinWidth((int) (width * 0.25));
				tmpLevel = new TextView(this);
				tmpLevel.setMinWidth((int) (width * 0.15));
				tmpHighscore = new TextView(this);
				tmpHighscore.setMinWidth((int) (width * 0.25));
				tmpAveScore = new TextView(this);
				
				if (i == -1)
				{
					tmpName.setMinHeight(70);
					tmpName.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
					tmpName.setText("Name");
					
					tmpLevel.setText("Level");
					tmpLevel.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
					
					tmpHighscore.setText("High Score");
					tmpHighscore.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
					
					tmpAveScore.setText("Average Score");
					tmpAveScore.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
					
					l = new LinearLayout(this);
					l.setOrientation(LinearLayout.HORIZONTAL);
					l.addView(tmpName);
					l.addView(tmpLevel);
					l.addView(tmpHighscore);
					l.addView(tmpAveScore);
					highscoreView.addView(l);
				} else
				{
					tmpName.setText(users.get(i).name);
					tmpName.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					tmpLevel.setText(String.valueOf(users.get(i).level));
					tmpLevel.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					tmpHighscore.setText(String.valueOf(users.get(i).getHighscore()));
					tmpHighscore.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					tmpAveScore.setText(String.valueOf(users.get(i).getAverageScore()));
					tmpAveScore.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					l = new LinearLayout(this);
					l.setOrientation(LinearLayout.HORIZONTAL);
					l.addView(tmpName);
					l.addView(tmpLevel);
					l.addView(tmpHighscore);
					l.addView(tmpAveScore);
					highscoreView.addView(l);
				}
			}
		}
	}
	
	public void back(View v)
	{
		finish();
	}
}
