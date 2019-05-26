package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;

class ObstacleSolid extends npbObject
{
	ObstacleSolid(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleSolid;
		image = Bitmap.createScaledBitmap(NotPinball.spriteSolid,radius*2,radius*2,true);
	}
}
