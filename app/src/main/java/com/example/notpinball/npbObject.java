package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public abstract class npbObject
{
	float x, y,x2, dX, dY, accdX, accdY;
	
	int radius;
	boolean dead = false, won = false, lose = false, dying = false, renderTwice = false;
	type thisType;
	Context context;
	Bitmap image;
	
	public enum type
	{
		Player, ObstacleSolid, ObstacleMoving, ObstacleTarget, ObstacleSpiked, nonColliding
	}
	
	public npbObject(float X, float Y, int Radius, Context context)
	{
		this.context = context;
		radius = Radius;
		x = X;
		y = Y;
	}
	
	public void update(List<npbObject> sprites)
	{
		x += dX;
		
		if (x < 0)
			x = NotPinball.screenWidth;
		else if (x > NotPinball.screenWidth)
			x = 0;
		
		if (((y + dY + radius) < NotPinball.gameLength) || thisType == type.Player && (y + dY - radius > 0))
			y += dY;
		else
		{
			dY = -dY;
			y += dY;
		}
		
		if (x + radius < 0)
			x++;
		else if (x - radius > NotPinball.screenWidth)
			x--;
		
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (Math.abs(y - other.y) < (other.radius * 2))
			{
				float dist = (float) Math.hypot(Math.abs(x - other.x), Math.abs(y - other.y));
				
				if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding && dist != 0)
				{
					float fOverlap = dist - radius - other.radius;
					x -= (fOverlap * (x - other.x) / dist) * 0.8f;
					y -= (fOverlap * (y - other.y) / dist) * 0.8f;
				}
			}
		}
		
		if (x < radius)
		{
			renderTwice = true;
			x2 = x + NotPinball.screenWidth;
		} else if (x > NotPinball.screenWidth - radius)
		{
			renderTwice = true;
			x2 = x - NotPinball.screenWidth;
		} else
			renderTwice = false;
	}
	
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
			
		canvas.drawBitmap(image, (x - radius), (y - NotPinball.cameraPos - radius), p1);
		if(renderTwice)
			canvas.drawBitmap(image, x2 - radius, (y - NotPinball.cameraPos - radius), p1);
	}
	
	public void setdX(float DX)
	{
		dX = DX;
	}
	
	public void setdY(float DY)
	{
		dY = DY;
	}
	
	public void moddX(float modDX)
	{
		if (x >= 0 && x <= NotPinball.screenWidth)
			if (Math.abs(dX + modDX) < NotPinball.maxSpeed)
				dX += modDX;
	}
	
	public void moddY(float accDY)
	{
		accdY = accDY;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	boolean onScreen()
	{
		return (y > (NotPinball.cameraPos - NotPinball.screenHeight * 0.1f) && y < (NotPinball.cameraPos + NotPinball.screenHeight * 1.1f));
	}
}

