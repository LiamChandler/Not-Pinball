package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class ObstacleSpike extends npbObject
{
	private float spikeHeight = 14;
	
	public ObstacleSpike(float X, float Y, int Radius)
	{
		super(X, Y, Radius);
		thisType = type.ObstacleSpiked;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.BLACK);
		p1.setStrokeWidth(2);
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		float xOffset = x, yOffset = y - NotPinball.cameraPos, spikeSpacing = (float) (Math.PI * 0.0625), halfSpikeSpacing;
		halfSpikeSpacing = spikeSpacing * 0.5f;
		
		path.moveTo(xOffset + radius - spikeHeight, y - NotPinball.cameraPos);
		for (float i = -halfSpikeSpacing; i < (2 * Math.PI); i += spikeSpacing)
		{
			path.lineTo((float) (Math.cos(i) * (radius)) + xOffset, (float) (Math.sin(i) * (radius)) + yOffset);
			path.lineTo((float) (Math.cos(i + halfSpikeSpacing) * (radius - spikeHeight)) + xOffset, (float) (Math.sin(i + halfSpikeSpacing) * (radius - spikeHeight)) + yOffset);
		}
		
		path.lineTo(x, y - NotPinball.cameraPos);
		path.close();
		
		canvas.drawPath(path, p1);
	}
}
