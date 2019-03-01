package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

public class textShow extends npbObject
{
	private String text;
	private int color;
	
	int alpha = 350;
	
	public textShow(float X,float Y, int TextSize,String Text, int Color)
	{
		super(X, Y, TextSize * 2);
		thisType = type.nonColliding;
		text = Text;
		color = Color;
	}
	
	public textShow(float X, float Y, int TextSize, String Text)
	{
		super(X, Y, TextSize*2);
		thisType = type.nonColliding;
		text = Text;
		color = Color.rgb(0,0,0);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int tmpAlpha = alpha;
		if(tmpAlpha < 0)
			tmpAlpha = 0;
		else if (tmpAlpha > 255)
			tmpAlpha = 255;
		
		Paint p = new Paint();
		p.setColor(color);
		p.setAlpha(tmpAlpha);
		p.setTextSize(radius);
		canvas.drawText(text, x, y - NotPinball.cameraPos, p);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		if(alpha > 0)
			alpha -= 3;
		else
			dead = true;
	}
}
