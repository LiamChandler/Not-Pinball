package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.List;

public class ObstacleMoving extends npbObject
{
	private float spikeHeight = 8;
	
	public ObstacleMoving(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.ObstacleMoving;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Point[] points = new Point[65];
		Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.rgb(0, 255, 0));
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		float xOffset = x, yOffset = y - NotPinball.cameraPos, spikeSpacing = (float) (Math.PI / 32f);
		
		path.moveTo(xOffset + radius - spikeHeight, y - NotPinball.cameraPos);
		for ( int i = 0; i < 65; i++)
		{
			if(i % 2 == 0)
			{
				points[i] = new Point((int) (Math.cos(i * spikeSpacing) * (radius) + xOffset), (int) ((Math.sin(i * spikeSpacing) * (radius)) + yOffset));
				path.lineTo((float) (Math.cos(i* spikeSpacing) * (radius)) + xOffset, (float) (Math.sin(i* spikeSpacing) * (radius)) + yOffset);
			}
			else
			{
				points[i] = new Point((int) (Math.cos(i * spikeSpacing) * (radius - spikeHeight) + xOffset), (int) ((Math.sin(i * spikeSpacing) * (radius - spikeHeight)) + yOffset));
				path.lineTo((float) (Math.cos(i* spikeSpacing) * (radius-spikeHeight)) + xOffset, (float) (Math.sin(i* spikeSpacing) * (radius-spikeHeight)) + yOffset);
			}
		}
		path.close();
		canvas.drawPath(path, p1);
		
		p1.setColor(Color.rgb(0, 0, 0));
		p1.setStrokeWidth(3);
		for ( int i = 0; i < 65; i++)
		{
			if(i != 64)
				canvas.drawLine(points[i].x,points[i].y,points[i+1].x,points[i+1].y,p1);
			else
				canvas.drawLine(points[64].x,points[64].y,points[0].x,points[0].y,p1);
		}
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
