package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;

class ObstacleTarget extends npbObject
{
	ObstacleTarget(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleTarget;
		image = Bitmap.createScaledBitmap(NotPinball.spriteTarget,radius*2,radius*2,true);
	}
}
