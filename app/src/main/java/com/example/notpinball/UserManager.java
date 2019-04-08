package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserManager extends AppCompatActivity
{
	LinearLayout userSelect;
	UserManagement manager;
	boolean remove = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manager);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		manager = new UserManagement(this);
		
		startView();
	}
	
	private void startView()
	{
		userSelect = findViewById(R.id.userHighscoreShowView);
		userSelect.removeAllViews();
		Button tmp;
		int num = manager.getSize();
		if (num > 0)
		{
			for (int i = 0; i < num; i++)
			{
				final int finalTmp = i;
				tmp = new Button(this);
				tmp.setText((manager.getName(i) + '\n' + '\n' + "Level: " + manager.getLevel(i)));
				tmp.setAllCaps(false);
				tmp.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						int index = finalTmp;
						if(remove)
						{
							manager.removeUser(index);
							manager.save();
							remove = false;
							startView();
						}
						else
						{
							manager.setAsCurrentUser(index);
							manager.save();
							finish();
						}
					}
				});
				userSelect.addView(tmp);
				Log.d("NPB", manager.getName(i) + " Highscore: " + manager.getHighScore(i));
			}
		}
	}
	
	public void buttonAdd(View v)
	{
		setContentView(R.layout.activity_user_manager_details);
	}
	
	public void buttonAddUser(View v)
	{
		TextView t = findViewById(R.id.textViewGetName);
		manager.addUser(t.getText().toString());
		manager.save();
		setContentView(R.layout.activity_user_manager);
		startView();
	}
	
	public void buttonRemove(View v)
	{
		remove = true;
	}
	
}
