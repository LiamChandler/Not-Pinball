package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;

class ObstacleSpike extends npbObject
{
	ObstacleSpike(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleSpiked;
		image = Bitmap.createScaledBitmap(NotPinball.spriteSpike,radius*2,radius*2,true);
	}
}
