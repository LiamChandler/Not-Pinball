package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

public class Player extends npbObject
{
	private float scrollPos = 0.15f, scrollZone = 0.3f;
	
	public Player(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.Player;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		p1.setColor(Color.rgb(128,64,0));
		canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
	}
	
	@Override
	public boolean update(List<npbObject> sprites)
	{
		super.update(sprites);
		
		if(dY < NotPinball.maxSpeed)
			dY+= 0.2;
		
		if(dX > 0.2)
			dX -= 0.2;
		else if(dX < -0.2)
			dX += 0.2;
		
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			float dist = (float) Math.hypot(Math.abs(x - other.getX()), Math.abs(y - other.getY()));
			
			if (dist <= radius + sprites.get(i).getRadius())
			{
				if(other.thisType == type.ObstacleSolid || other.thisType == type.ObstacleMoving)
				{
					NotPinball.playerHealth--;
					float nX = (other.getX() - x) / dist, nY = (other.getY() - y) / dist;
					float tX = -nY, tY = nX;
					float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
					
					dX = tX * dpTan + nX * -dpNorm;
					dY = tY * dpTan + nY * -dpNorm;
				}
				
				Log.d("NPB", "Player Hit object Health = " + NotPinball.playerHealth);
			}
		}
		
		x += accdX;
		y += accdY;
		
		if (y - NotPinball.cameraPos < NotPinball.screenHeight * scrollPos && NotPinball.cameraPos > 0)
			NotPinball.cameraPos += y - NotPinball.cameraPos - NotPinball.screenHeight * scrollPos;
		else if (y - NotPinball.cameraPos > NotPinball.screenHeight * (scrollPos + scrollZone))
			NotPinball.cameraPos += y - NotPinball.cameraPos - NotPinball.screenHeight * (scrollPos + scrollZone);
		
		return false;
	}
	
}