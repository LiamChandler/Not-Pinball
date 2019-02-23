package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ObstacleSolid extends npbObject
{
	public ObstacleSolid(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.ObstacleSolid;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		p1.setColor(Color.rgb(0, 0, 128));
		canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
	}
}
