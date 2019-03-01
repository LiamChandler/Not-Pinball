package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class ObstacleTarget extends npbObject
{
	private float spikeHeight = 8;
	
	public ObstacleTarget(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.ObstacleTarget;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.rgb(255, 0, 0));
		
		canvas.drawCircle(x,y - NotPinball.cameraPos,radius,p1);
		p1.setColor(Color.rgb(255, 255, 255));
		canvas.drawCircle(x,y- NotPinball.cameraPos,radius*0.666f,p1);
		p1.setColor(Color.rgb(255, 0, 0));
		canvas.drawCircle(x,y- NotPinball.cameraPos,radius * 0.333f,p1);
	}
}
