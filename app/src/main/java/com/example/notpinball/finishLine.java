package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public class finishLine extends npbObject
{
	public finishLine(float Y)
	{
		super(0, Y, 0);
		thisType = type.nonColliding;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p = new Paint();
		p.setStrokeWidth(5);
		p.setARGB(255, 0, 0, 0);
		canvas.drawLine(0, y - 5 - NotPinball.cameraPos, NotPinball.screenWidth, y - 5 - NotPinball.cameraPos, p);
	}
}
