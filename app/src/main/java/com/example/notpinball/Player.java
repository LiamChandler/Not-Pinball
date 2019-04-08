package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends npbObject
{
	private Timer timer = new Timer();
	private boolean timerCoolDown = false;
	private float scrollPos = 0.15f, scrollZone = 0.3f, playerMaxPos = 0;
	
	public Player(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.Player;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		p1.setColor(Color.rgb(128, 64, 0));
		if(x<radius)
		{
			canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
			canvas.drawCircle(x+NotPinball.screenWidth, y - NotPinball.cameraPos, radius, p1);
		}
		else if (x>NotPinball.screenWidth-radius)
		{
			canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
			canvas.drawCircle(x-NotPinball.screenWidth, y - NotPinball.cameraPos, radius, p1);
		}
		else
			canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		super.update(sprites);
		
		
		if(y> NotPinball.gameLength)
			won = true;
		if(y>playerMaxPos && !won)
		{
			playerMaxPos = y;
			NotPinball.currScore = (int) ((playerMaxPos / (NotPinball.screenHeight * 0.0399f)))* NotPinball.Level;
		}
		if(dying && radius >=1)
		{
			radius--;
		}
		
		if (dY < NotPinball.maxSpeed)
			dY += 0.3;
		
		if (dX > 0.05)
			dX -= 0.05;
		else if (dX < -0.05)
			dX += 0.05;
		
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			float dist = (float) Math.hypot(Math.abs(x - other.getX()), Math.abs(y - other.getY()));
			
			if (dist <= radius + sprites.get(i).getRadius() && other.thisType != type.nonColliding)
			{
				if (!timerCoolDown)
				{
					if (other.thisType == type.ObstacleSolid || other.thisType == type.ObstacleMoving)
					{
						NotPinball.playerHealth--;
						sprites.add(new textShow(x, y, NotPinball.textSize * 2, "-1", Color.rgb(180, 0, 0)));
						bounceOffRound(other, dist);
					} else if (other.thisType == type.ObstacleSpiked)
					{
						NotPinball.playerHealth -= 5;
						sprites.add(new textShow(x, y, NotPinball.textSize * 2, "-5", Color.rgb(180, 0, 0)));
						bounceOffRound(other, dist);
					} else if (other.thisType == type.ObstacleTarget)
					{
						NotPinball.lastTargetScore += NotPinball.Level;
						NotPinball.totalScore += NotPinball.lastTargetScore;
						
						NotPinball.playerHealth ++;
						if(NotPinball.playerHealth > NotPinball.playerMaxHealth)
							NotPinball.playerHealth = NotPinball.playerMaxHealth;
						
						sprites.add(new textShow(x, y, NotPinball.textSize * 2, "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0)));
						other.dead = true;
					}
					if (NotPinball.playerHealth <= 0)
					{
						lose = true;
						NotPinball.playerHealth=0;
					}
					timerCoolDown = true;
					timer.schedule(new shortCoolDown(), (long) 150);
				}
			}
		}
		
		x += accdX;
		y += accdY;
		
		if (y - NotPinball.cameraPos < NotPinball.screenHeight * scrollPos && NotPinball.cameraPos > 0)
			NotPinball.cameraPos += y - NotPinball.cameraPos - NotPinball.screenHeight * scrollPos;
		else if (y - NotPinball.cameraPos > NotPinball.screenHeight * (scrollPos + scrollZone) && NotPinball.cameraPos < NotPinball.gameLength - (NotPinball.screenHeight * (scrollPos + scrollZone)))
			NotPinball.cameraPos += y - NotPinball.cameraPos - NotPinball.screenHeight * (scrollPos + scrollZone);
	}
	
	private void bounceOffRound(npbObject other, float dist)
	{
		float nX = (other.getX() - x) / dist, nY = (other.getY() - y) / dist;
		float tX = -nY, tY = nX;
		float dpTan = dX * tX + dY * tY, dpNorm = dX * nX + dY * nY;
		
		dX = tX * dpTan + nX * -dpNorm;
		dY = tY * dpTan + nY * -dpNorm;
	}
	
	class shortCoolDown extends TimerTask
	{
		@Override
		public void run()
		{
			timerCoolDown = false;
		}
	}
	
}