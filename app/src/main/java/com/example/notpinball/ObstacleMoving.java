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
		for (int i = 0; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if (other.thisType != type.nonColliding && Math.abs(y - other.y) < (other.radius * 2))
			{
				float dist;
				if (x != other.x && y != other.y)
				{
					dist = (float) Math.hypot(Math.abs(x - other.x), Math.abs(y - other.y));
					
					if (dist <= radius + sprites.get(i).getRadius())
					{
						float nX = (other.x - x) / dist, nY = (other.y - y) / dist;
						float tX = -nY, tY = nX;
						float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
						
						dX = tX * dpTan + nX * -dpNorm;
						dY = tY * dpTan + nY * -dpNorm;
					}
					if (other.renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x - other.x2), Math.abs(y - other.y));
						if (dist <= radius + other.getRadius())
						{
							float nX = (other.x2 - x) / dist, nY = (other.y - y) / dist;
							float tX = -nY, tY = nX;
							float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
							
							dX = tX * dpTan + nX * -dpNorm;
							dY = tY * dpTan + nY * -dpNorm;
						}
					} else if (renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x2 - other.x), Math.abs(y - other.y));
						if (dist <= radius + other.getRadius())
						{
							float nX = (other.x - x2) / dist, nY = (other.y - y) / dist;
							float tX = -nY, tY = nX;
							float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
							
							dX = tX * dpTan + nX * -dpNorm;
							dY = tY * dpTan + nY * -dpNorm;
						}
					}
				}
			}
		}
		super.update(sprites);
	}
	
}
