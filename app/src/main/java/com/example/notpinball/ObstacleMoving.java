package com.example.notpinball;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.List;

public class ObstacleMoving extends npbObject
{
	ObstacleMoving(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		image = Bitmap.createScaledBitmap(NotPinball.spriteMoving, radius*2+10,radius*2+10, true);
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
						bounceOffRound(nY, nX);
					}
					if (other.renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x - other.x2), Math.abs(y - other.y));
						if (dist <= radius + other.getRadius())
						{
							float nX = (other.x2 - x) / dist, nY = (other.y - y) / dist;
							bounceOffRound(nY, nX);
						}
					} else if (renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x2 - other.x), Math.abs(y - other.y));
						if (dist <= radius + other.getRadius())
						{
							float nY = (other.y - y) / dist, nX = (other.x - x2) / dist;
							bounceOffRound(nY, nX);
						}
					}
				}
			}
		}
		super.update(sprites);
	}
	
}
