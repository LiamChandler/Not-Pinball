package com.example.notpinball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class finishLine extends npbObject
{
	private float height;
	
	public finishLine(float Y, float Height, Context c)
	{
		super(0, Y - (Height/2f), 0,c);
		thisType = type.nonColliding;
		height = Height;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p = new Paint();
		p.setARGB(255, 255, 255, 255);
		int numChecks = 20;
		int spacing = NotPinball.screenWidth / numChecks;
		
		canvas.drawRect(0, y - NotPinball.cameraPos, NotPinball.screenWidth, y - NotPinball.cameraPos + height, p);
		
		p.setARGB(255, 0, 0, 0);
		for (int i = 0; i < 20; i++)
		{
			float tmpY = y - NotPinball.cameraPos;
			if (i % 2 == 0)
				tmpY += height / 2f;
			
			canvas.drawRect(spacing * i, tmpY, spacing * (i + 1), tmpY + height / 2f, p);
		}
	}
}
