package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.List;

public class ObstacleMoving extends npbObject
{
	ObstacleMoving(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.moving_sprite), radius * 2, radius * 2, true);
		thisType = type.ObstacleMoving;
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (Math.abs(y - other.getY()) < (radius * 4))
			{
				if (x != other.x && y != other.y && other.thisType != type.nonColliding)
				{
					float dist = (float) Math.hypot(Math.abs(x - other.x), Math.abs(y - other.x));
					
					if (dist <= radius + sprites.get(i).getRadius())
					{
						float nX = (other.getX() - x) / dist, nY = (other.getY() - y) / dist;
						float tX = -nY, tY = nX;
						float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
						
						dX = tX * dpTan + nX * -dpNorm;
						dY = tY * dpTan + nY * -dpNorm;
					}
				}
			}
		}
		super.update(sprites);
	}
}
