package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.List;

public abstract class npbObject
{
	PointF P = new PointF(), P2, dP = new PointF(), ddP = new PointF();
	int radius;
	boolean dead = false, won = false, lose = false, dying = false;
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
		P.set(X,Y);
	}
	
	public void update(List<npbObject> sprites)
	{
		P.x += dP.x;
		
		if (P.x < 0)
			P.x = NotPinball.screenWidth;
		else if (P.x > NotPinball.screenWidth)
			P.x = 0;
		
		if (((P.y + dP.y + radius) < NotPinball.gameLength) || thisType == type.Player && (P.y + dP.y - radius > 0))
			P.y += dP.y;
		else
		{
			dP.y = -dP.y;
			P.y += dP.y;
		}
		
		if (P.x + radius < 0)
			P.x++;
		else if (P.x - radius > NotPinball.screenWidth)
			P.x--;
		
		
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (Math.abs(P.y - other.getY()) < (radius * 4))
			{
				float dist = (float) Math.hypot(Math.abs(P.x - other.getX()), Math.abs(P.y - other.getY()));
				
				if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding && dist != 0)
				{
					float fOverlap = dist - radius - other.radius;
					P.x -= (fOverlap * (P.x - other.P.x) / dist) * 0.8f;
					P.y -= (fOverlap * (P.y - other.P.y) / dist) * 0.8f;
				}
			}
		}
		
		if (P.x < radius)
			P2 = new PointF(P.x + NotPinball.screenWidth, P.y);
		else if (P.x > NotPinball.screenWidth - radius)
			P2 = new PointF(P.x - NotPinball.screenWidth, P.y);
		else
			P2 = null;
	}
	
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		canvas.drawBitmap(image, (P.x - radius), (P.y - NotPinball.cameraPos - radius), p1);
		if(P2 != null)
			canvas.drawBitmap(image, (P2.x - radius), (P2.y - NotPinball.cameraPos - radius), p1);
	}
	
	public float getX()
	{
		return P.x;
	}
	
	public float getY()
	{
		return P.y;
	}
	
	public void setdX(float DX)
	{
		dP.x = DX;
	}
	
	public void setdY(float DY)
	{
		dP.y = DY;
	}
	
	public float getdX()
	{
		return dP.x;
	}
	
	public float getdY()
	{
		return dP.y;
	}
	
	public void moddX(float modDX)
	{
		ddP.x += modDX;
	}
	
	public void moddY(float accDY)
	{
		ddP.y = accDY;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	boolean onScreen()
	{
		return (P.y > (NotPinball.cameraPos - NotPinball.screenHeight * 0.1f) && P.y < (NotPinball.cameraPos + NotPinball.screenHeight * 1.1f));
	}
}

