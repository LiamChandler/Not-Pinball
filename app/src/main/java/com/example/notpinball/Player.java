package com.example.notpinball;

import android.content.Context;
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
	
	public Player(float X, float Y, int Radius, Context context)
	{
		super(X, Y, Radius, context);
		thisType = type.Player;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint();
		p1.setColor(Color.rgb(128, 64, 0));
		
		canvas.drawCircle(P.x, P.y - NotPinball.cameraPos, radius, p1);
		if(P2 != null)
			canvas.drawCircle(P2.x, P2.y - NotPinball.cameraPos, radius, p1);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		super.update(sprites);
		
		if (P.y > NotPinball.gameLength && !lose)
			won = true;
		else if (P.y > playerMaxPos && !lose)
		{
			playerMaxPos = P.y - NotPinball.playerStartY;
			NotPinball.currScore = (int) ((playerMaxPos / ((NotPinball.gameLength - NotPinball.playerStartY) / 100))) * NotPinball.Level;
		}
		if (dying && radius >= 1)
			radius--;
		
		if (dP.y < NotPinball.maxSpeed)
			dP.y += 0.3;
		else
			dP.y = NotPinball.maxSpeed;
		
		if (ddP.x > NotPinball.maxSpeed)
			ddP.x = NotPinball.maxSpeed;
		else if (ddP.x < -NotPinball.maxSpeed)
			ddP.x = -NotPinball.maxSpeed;
		
		if (dP.x > 0.05)
			dP.x -= 0.05;
		else if (dP.x < -0.05)
			dP.x += 0.05;
		
		
		float dist;
		for (int i = 1; i < sprites.size(); i++)
		{
			npbObject other = sprites.get(i);
			if(Math.abs(P.y - other.getY()) < (radius*4))
			{
				dist = (float) Math.hypot(Math.abs(P.x - other.getX()), Math.abs(P.y - other.getY()));
				
				if (dist <= radius + other.getRadius() && other.thisType != type.nonColliding)
				{
					if (!timerCoolDown && !lose)
					{
						if (other.thisType == type.ObstacleSolid || other.thisType == type.ObstacleMoving)
						{
							NotPinball.playerHealth--;
							sprites.add(new textShow(P.x, P.y, NotPinball.textSize * 2, "-❤", Color.rgb(180, 0, 0), context));
							bounceOffRound(other, dist);
						} else if (other.thisType == type.ObstacleSpiked)
						{
							NotPinball.playerHealth = 0;
							sprites.add(new textShow(P.x, P.y, NotPinball.textSize * 2, "☠", Color.rgb(0, 0, 0), context));
							bounceOffRound(other, dist);
						} else if (other.thisType == type.ObstacleTarget)
						{
							NotPinball.lastTargetScore += NotPinball.Level;
							NotPinball.totalScore += NotPinball.lastTargetScore;
							
							NotPinball.playerHealth++;
							if (NotPinball.playerHealth > NotPinball.playerMaxHealth)
								NotPinball.playerHealth = NotPinball.playerMaxHealth;
							
							sprites.add(new textShow(P.x, P.y - (NotPinball.textSize * 3), (int) (NotPinball.textSize * 1.5f), "+❤", Color.rgb(0, 200, 0), context));
							sprites.add(new textShow(P.x, P.y, (int) (NotPinball.textSize * 1.5f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
							other.dead = true;
						}
						if (NotPinball.playerHealth <= 0)
						{
							lose = true;
							NotPinball.playerHealth = 0;
						}
						timerCoolDown = true;
						timer.schedule(new shortCoolDown(), (long) 150);
					}
				}
			}
		}
		
		P.offset(ddP.x,ddP.y);
		
		if (P.y - NotPinball.cameraPos < NotPinball.screenHeight * scrollPos && NotPinball.cameraPos > 0)
			NotPinball.cameraPos += P.y - NotPinball.cameraPos - NotPinball.screenHeight * scrollPos;
		else if (P.y - NotPinball.cameraPos > NotPinball.screenHeight * (scrollPos + scrollZone) && NotPinball.cameraPos < NotPinball.gameLength - (NotPinball.screenHeight * (scrollPos + scrollZone)))
			NotPinball.cameraPos += P.y - NotPinball.cameraPos - NotPinball.screenHeight * (scrollPos + scrollZone);
	}
	
	private void bounceOffRound(npbObject other, float dist)
	{
		float nX = (other.getX() - P.x) / dist, nY = (other.getY() - P.y) / dist;
		float tX = -nY, tY = nX;
		float dpTan = dP.x * tX + dP.y * tY, dpNorm = dP.x * nX + dP.y * nY;
		
		dP.x = tX * dpTan + nX * -dpNorm;
		dP.x = tY * dpTan + nY * -dpNorm;
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