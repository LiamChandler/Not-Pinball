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
		
		canvas.drawCircle(x, y - NotPinball.cameraPos, radius, p1);
		if(renderTwice)
			canvas.drawCircle(x2, y - NotPinball.cameraPos, radius, p1);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		super.update(sprites);
		if (y > NotPinball.gameLength && !lose && !won)
		{
			won = true;
			playerMaxPos = y - NotPinball.playerStartY;
			NotPinball.currScore = (int) ((playerMaxPos / ((NotPinball.gameLength - NotPinball.playerStartY) / 100))) * NotPinball.Level;
		}
		else if (y - NotPinball.playerStartY > playerMaxPos && !lose && !won)
		{
			playerMaxPos = y - NotPinball.playerStartY;
			NotPinball.currScore = (int) ((playerMaxPos / ((NotPinball.gameLength - NotPinball.playerStartY) / 100))) * NotPinball.Level;
		}

		if (dying && radius >= 1)
			radius--;
		
		if (dY < NotPinball.maxSpeed)
			dY += 0.25;
		else
			dY = NotPinball.maxSpeed;
		
		if (dX > 0.05)
			dX -= 0.05;
		else if (dX < -0.05)
			dX += 0.05;
		
		if (!timerCoolDown && !lose)
		{
			for (int i = 1; i < sprites.size(); i++)
			{
				npbObject other = sprites.get(i);
				if (Math.abs(y - other.y) < (radius * 3) && other.thisType != type.nonColliding)
				{
					float dist = (float) Math.hypot(Math.abs(x - other.x), Math.abs(y - other.y));
					
					if (dist <= radius + other.getRadius())
					{
						if (other.thisType == type.ObstacleTarget)
						{
							NotPinball.lastTargetScore += NotPinball.Level;
							if(NotPinball.playerHealth == 5)
								sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 2f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
							else
							{
								NotPinball.playerHealth++;
								
								sprites.add(new textShow(x, y - (NotPinball.textSize * 3), (int) (NotPinball.textSize * 1.5f), "+❤", Color.rgb(0, 200, 0), context));
								sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 1.5f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
							}
							other.dead = true;
						}
						else if (other.thisType == type.ObstacleSpiked)
						{
							NotPinball.playerHealth = 0;
							sprites.add(new textShow(x, y, NotPinball.textSize * 2, "☠", Color.rgb(0, 0, 0), context));
							float nX = (other.x - x) / dist, nY = (other.y - y) / dist;
							bounceOffRound(nY, nX);
						}
						else
						{
							NotPinball.lastTargetScore = 0;
							NotPinball.playerHealth--;
							sprites.add(new textShow(x, y, NotPinball.textSize * 2, "-❤", Color.rgb(255, 0, 0), context));
							float nX = (other.x - x) / dist, nY = (other.y - y) / dist;
							bounceOffRound(nY, nX);
						}
						
						if (NotPinball.playerHealth <= 0)
							lose = true;
						timerCoolDown = true;
						timer.schedule(new shortCoolDown(), (long) 150);
					}
					if (other.renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x - other.x2), Math.abs(y - other.y));
						
						if (dist <= radius + other.getRadius())
						{
							if (other.thisType == type.ObstacleTarget)
							{
								NotPinball.lastTargetScore += NotPinball.Level;
								if(NotPinball.playerHealth == 5)
									sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 2f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
								else
								{
									NotPinball.playerHealth++;
									
									sprites.add(new textShow(x, y - (NotPinball.textSize * 3), (int) (NotPinball.textSize * 1.5f), "+❤", Color.rgb(0, 200, 0), context));
									sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 1.5f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
								}
								other.dead = true;
							}
							else if (other.thisType == type.ObstacleSpiked)
							{
								NotPinball.playerHealth = 0;
								sprites.add(new textShow(x, y, NotPinball.textSize * 2, "☠", Color.rgb(0, 0, 0), context));
								float nX = (other.x2 - x) / dist, nY = (other.y - y) / dist;
								bounceOffRound(nY, nX);
							}
							else
							{
								NotPinball.lastTargetScore = 0;
								NotPinball.playerHealth--;
								sprites.add(new textShow(x, y, NotPinball.textSize * 2, "-❤", Color.rgb(255, 0, 0), context));
								float nX = (other.x2 - x) / dist, nY = (other.y - y) / dist;
								bounceOffRound(nY, nX);
							}
							
							if (NotPinball.playerHealth <= 0)
								lose = true;
							timerCoolDown = true;
							timer.schedule(new shortCoolDown(), (long) 150);
						}
					}
					else if (renderTwice)
					{
						dist = (float) Math.hypot(Math.abs(x2 - other.x), Math.abs(y - other.y));
						
						if (dist <= radius + other.getRadius())
						{
							Log.d("NPB", "this second render and other render collision");
							if (other.thisType == type.ObstacleTarget)
							{
								NotPinball.lastTargetScore += NotPinball.Level;
								if(NotPinball.playerHealth == 5)
									sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 2f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
								else
								{
									NotPinball.playerHealth++;
									
									sprites.add(new textShow(x, y - (NotPinball.textSize * 3), (int) (NotPinball.textSize * 1.5f), "+❤", Color.rgb(0, 200, 0), context));
									sprites.add(new textShow(x, y, (int) (NotPinball.textSize * 1.5f), "+" + NotPinball.lastTargetScore, Color.rgb(0, 200, 0), context));
								}
								other.dead = true;
							}
							else if (other.thisType == type.ObstacleSpiked)
							{
								NotPinball.playerHealth = 0;
								sprites.add(new textShow(x2, y, NotPinball.textSize * 2, "☠", Color.rgb(0, 0, 0), context));
								float nY = (other.y - y) / dist, nX = (other.x - x2) / dist;
								bounceOffRound(nY, nX);
							}
							else
							{
								NotPinball.lastTargetScore = 0;
								NotPinball.playerHealth--;
								sprites.add(new textShow(x2, y, NotPinball.textSize * 2, "-❤", Color.rgb(255, 0, 0), context));
								float nY = (other.y - y) / dist, nX = (other.x - x2) / dist;
								bounceOffRound(nY, nX);
							}
							
							if (NotPinball.playerHealth <= 0)
								lose = true;
							timerCoolDown = true;
							timer.schedule(new shortCoolDown(), (long) 150);
						}
					}
					
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
	
	class shortCoolDown extends TimerTask
	{
		@Override
		public void run()
		{
			timerCoolDown = false;
		}
	}
}