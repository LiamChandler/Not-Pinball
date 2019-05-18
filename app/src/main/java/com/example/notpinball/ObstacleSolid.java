package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ObstacleSolid extends npbObject
{
	public ObstacleSolid(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleSolid;
		image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.solid_sprite),radius*2,radius*2,true);
	}
}
