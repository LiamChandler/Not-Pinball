package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

public class ObstacleMoving extends npbObject
{
	public ObstacleMoving(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.ObstacleMoving;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		p1.setColor(Color.rgb(0, 128, 0));
		canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (x != other.x && y != other.y && other.thisType != type.nonColliding)
			{
				float dist = (float) Math.hypot(Math.abs(x - other.getX()), Math.abs(y - other.getY()));
				
				if (dist <= radius + sprites.get(i).getRadius())
				{
					dX = -dX;
					dY = -dY;
				}
			}
		}
		
		if (((x + dX + radius) < NotPinball.screenWidth) && (x + dX - radius > 0))
			x += dX;
		else
		{
			dX = -dX;
			dY = -dY;
			x += dX;
		}
		
		if (((y + dY + radius) < NotPinball.gameLength) && (y + dY - radius > 0))
			y += dY;
		else
		{
			dY = -dY;
			y += dY;
		}
	}
}
