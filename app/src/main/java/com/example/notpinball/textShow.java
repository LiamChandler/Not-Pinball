package com.example.notpinball;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public class textShow extends npbObject
{
	private String text;
	
	int alpha = 350;
	
	public textShow(float X, float Y, int TextSize, String Text)
	{
		super(X, Y, TextSize*2);
		thisType = type.nonColliding;
		text = Text;
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
		p.setARGB(tmpAlpha, 0, 0, 0);
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
