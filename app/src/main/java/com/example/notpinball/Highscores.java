package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		TextView tmpName, tmpScore,tmpLevel;
		LinearLayout l;
		
		int num = manager.getSize(), width = displayMetrics.widthPixels-32;
		if (num > 0)
		{
			for (int i = -1; i < num; i++)
			{
				tmpName = new TextView(this);
				tmpName.setMinWidth((int) (width * 0.3));
				tmpLevel = new TextView(this);
				tmpLevel.setMinWidth((int) (width * 0.20));
				tmpScore = new TextView(this);
				
				if(i == -1)
				{
					tmpName.setMinHeight(120);
					tmpName.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
					tmpName.setText("Name");
					
					tmpLevel.setText("Level");
					tmpLevel.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
					
					tmpScore.setText("High Score");
					tmpScore.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
				}
				else
				{
					tmpName.setText((manager.getName(i)));
					tmpName.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					tmpLevel.setText((""+manager.getLevel(i)));
					tmpLevel.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
					
					tmpScore.setText(((""+manager.getHighscore(i))));
					tmpScore.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
				}
				
				l = new LinearLayout(this);
				l.setOrientation(LinearLayout.HORIZONTAL);
				l.addView(tmpName);
				l.addView(tmpLevel);
				l.addView(tmpScore);
				highscoreView.addView(l);
			}
		}
	}
	
	public void back(View v)
	{
		finish();
	}
}
