package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class finishLine extends npbObject
{
	private int height;
	
	public finishLine(int Y, int Height, Context c)
	{
		super(0, Y - (Height/2f), 0,c);
		thisType = type.nonColliding;
		height = Height;
		image = Bitmap.createScaledBitmap(NotPinball.spriteFinish,NotPinball.screenWidth,height,true);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		canvas.drawBitmap(image, x, (y - NotPinball.cameraPos), p1);
	}
}
