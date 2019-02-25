package com.example.notpinball;

import android.graphics.Canvas;
import android.util.Log;

import java.util.List;

public abstract class npbObject
{
	protected float x, y, dX, dY, accdX, accdY;
	protected int radius;
	protected boolean dead = false, won = false, lose = false;
	protected type thisType;
	
	public enum type
	{
		Player, ObstacleSolid, ObstacleMovable, ObstacleMoving, ObstacleTarget, ObstacleSpiked, nonColliding
	}
	
	public npbObject(float X, float Y, int Radius)
	{
		radius = Radius;
		x = X;
		y = Y;
	}
	
	public void update(List<npbObject> sprites)
	{
		if (((x + dX + radius) < NotPinball.screenWidth) && (x + dX - radius > 0))
			x += dX;
		else
		{
			dX = -dX;
			x += dX;
		}
		
		if (((y + dY + radius) < NotPinball.gameLength) || thisType == type.Player && (y + dY - radius > 0))
			y += dY;
		else
		{
			dY = -dY;
			y += dY;
		}
		
		if(x+radius<0)
			x++;
		else if(x-radius>NotPinball.screenWidth)
			x--;
	}
	
	public abstract void draw(Canvas canvas);
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setdX(float DX)
	{
		dX = DX;
	}
	
	public void setdY(float DY)
	{
		dY = DY;
	}
	
	public float getdX()
	{
		return dX;
	}
	
	public float getdY()
	{
		return dY;
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
}

