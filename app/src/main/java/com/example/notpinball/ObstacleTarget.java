package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ObstacleTarget extends npbObject
{
	public ObstacleTarget(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleTarget;
		image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.target_sprite),radius*2,radius*2,true);
	}
}
