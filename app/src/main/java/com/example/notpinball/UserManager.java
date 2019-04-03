package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserManager extends AppCompatActivity
{
	LinearLayout userSelect;
	UserManagement manager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manager);

		manager = new UserManagement(this);
		manager.load();
		userSelect = findViewById(R.id.userSelectSubBox);
		Button tmp;
		int num = manager.getSize();
		if(num > 0)
		{
			for (int i = 0; i < num; i++)
			{
				tmp = new Button(this);
				tmp.setText(manager.getName(i));
				userSelect.addView(tmp);
			}
		}
	}

	public void buttonAdd(View v)
	{

	}
}
