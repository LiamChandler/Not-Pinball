package com.example.notpinball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void gamePlay(View v)
    {
        Log.d("NPB", "gamePlay");
    }

    public void gameCreateUser(View v)
    {
        Log.d("NPB", "gameCreateUser");

    }

    public void gameViewHighscores(View v)
    {
        Log.d("NPB", "gameViewHighscores");

    }
}
