package com.example.notpinball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.w3c.dom.Text;

import java.util.List;

public class textShow extends npbObject
{
	private String text;
	private int color;
	private boolean fading = true;
	
	int alpha = 350;
	
	public textShow(float X, float Y, int TextSize, String Text, int Color, Context context)
	{
		super(X, Y, TextSize * 2,context);
		thisType = type.nonColliding;
		text = Text;
		color = Color;
	}
	
	public textShow(float X,float Y, int TextSize,String Text, int Color, int Fade,Context context)
	{
		super(X, Y, TextSize * 2,context);
		thisType = type.nonColliding;
		text = Text;
		color = Color;
		fading = false;
		alpha = Fade;
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
		p.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(text, P.x, P.y - NotPinball.cameraPos, p);
	}
	
	@Override
	public void update(List<npbObject> sprites)
	{
		if(fading)
		{
			if (alpha > 0)
				alpha -= 3;
			else
				dead = true;
		}
	}
}
