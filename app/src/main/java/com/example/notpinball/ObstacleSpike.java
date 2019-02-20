package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class ObstacleSpike extends npbObject
{
	public ObstacleSpike(float X, float Y, int Radius)
	{
		super(X,Y,Radius);
		thisType = type.ObstacleSpiked;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.BLACK);
		p1.setStrokeWidth(2);
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x,y - NotPinball.cameraPos);
		
		for(float i = 0; i < 2* Math.PI; i += Math.PI*0.1)
		{
			path.lineTo((float) (Math.sin(i)*radius+2.5),(float) (Math.cos(i)*radius+2.5) - NotPinball.cameraPos);
			path.lineTo((float) (Math.sin(i+0.05)*radius-2.5),(float) (Math.cos(i+0.05)*radius-2.5) - NotPinball.cameraPos);
		}
		
		path.lineTo(x,y - NotPinball.cameraPos);
		path.close();
		
		canvas.drawPath(path,p1);
	}
}
