package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.List;

public class ObstacleMoving extends npbObject
{
	public ObstacleMoving(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.moving_sprite),radius*2,radius*2,true);
		thisType = type.ObstacleMoving;
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (x != other.x && y != other.y && other.thisType != type.nonColliding && other.thisType != type.ObstacleTarget)
			{
				float dist = (float) Math.hypot(Math.abs(x - other.getX()), Math.abs(y - other.getY()));
				
				if (dist <= radius + sprites.get(i).getRadius())
				{
					dX = -dX;
					dY = -dY;
				}
			}
		}
		super.update(sprites);
	}
}
