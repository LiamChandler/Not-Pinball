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
			if(Math.abs(P.y - other.getY()) < (radius*4))
			{
				if (!P.equals(other.P) && other.thisType != type.nonColliding && other.thisType != type.ObstacleTarget)
				{
					float dist = (float) Math.hypot(Math.abs(P.x - other.getX()), Math.abs(P.y - other.getY()));
					
					if (dist <= radius + sprites.get(i).getRadius())
					{
						float nX = (other.getX() - P.x) / dist, nY = (other.getY() - P.y) / dist;
						float tX = -nY, tY = nX;
						float dpTan = dP.x * tX + dP.y * tY, dpNorm = dP.x * nX + dP.y * nY;
						
						dP.x = tX * dpTan + nX * -dpNorm;
						dP.y = tY * dpTan + nY * -dpNorm;
					}
				}
			}
		}
		super.update(sprites);
	}
}
