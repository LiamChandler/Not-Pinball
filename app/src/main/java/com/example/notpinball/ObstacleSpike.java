package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ObstacleSpike extends npbObject
{
	public ObstacleSpike(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.ObstacleSpiked;
		image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.spike_sprite),radius*2,radius*2,true);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		canvas.drawBitmap(image,(x-radius),(y - NotPinball.cameraPos-radius),p1);
	}
}
