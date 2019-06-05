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
		
		float dist;
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (x!=other.x && y != other.y && Math.abs(y - other.y) < (other.radius * 3))
			{
				dist= (float) Math.hypot(Math.abs(x - other.x), Math.abs(y - other.y));
				if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding && dist != 0)
				{
					float fOverlap = dist - radius - other.radius;
					x -= (fOverlap * (x - other.x) / dist) * 0.8f;
					y -= (fOverlap * (y - other.y) / dist) * 0.8f;
				}
				if (other.renderTwice)
				{
					dist= (float) Math.hypot(Math.abs(x - other.x2), Math.abs(y - other.y));
					
					if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding && dist != 0)
					{
						float fOverlap = dist - radius - other.radius;
						x -= (fOverlap * (x - other.x2) / dist) * 0.8f;
						y -= (fOverlap * (y - other.y) / dist) * 0.8f;
					}
				}
				else if (renderTwice)
				{
					dist = (float) Math.hypot(Math.abs(x2 - other.x), Math.abs(y - other.y));
					
					if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding && dist != 0)
					{
						float fOverlap = dist - radius - other.radius;
						x -= (fOverlap * (x2 - other.x) / dist) * 0.8f;
						y -= (fOverlap * (y - other.y) / dist) * 0.8f;
					}
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
			
		canvas.drawBitmap(image, (x - radius-5), (y - NotPinball.cameraPos - radius-5), p1);
		if(renderTwice)
			canvas.drawBitmap(image, x2 - radius-5, (y - NotPinball.cameraPos - radius-5), p1);
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
	
	void bounceOffRound(float nY, float nX)
	{
		float tX = -nY, tY = nX, dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
		
		dX = tX * dpTan + nX * -dpNorm;
		dY = tY * dpTan + nY * -dpNorm;
		
		dX = dX < NotPinball.maxSpeed ? dX : NotPinball.maxSpeed;
		dY = dY < NotPinball.maxSpeed ? dY : NotPinball.maxSpeed;
	}
}

