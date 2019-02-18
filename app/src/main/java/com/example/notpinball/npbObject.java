package com.example.notpinball;

import android.graphics.Canvas;
import android.util.Log;

import java.util.List;

public abstract class npbObject {
    protected float x, y, dX, dY, accdX, accdY;
    protected int radius;
    protected boolean dead = false;

    public npbObject(float X, float Y, int Radius) {
        radius = Radius;
        x = X;
        y = Y;
    }

    public boolean update(List<npbObject> sprites) {
        if (((x + dX + radius) < NotPinball.screenWidth) && (x + dX - radius > 0))
            x += dX;
        else {
            dX = -dX;
            x += dX;
        }

        if ((y + dY + radius) < NotPinball.gameHeight)
            y += dY;
        else {
            dY = -dY;
            y += dY;
        }

        if (y + dY - (radius + (NotPinball.radiusTarget * 0.75)) <= 0) {
            dead = true;

            y += dY;
            dY = 0;
            dX = 0;
            Log.d("NPB", "player Died");
        }
        return false;
    }

    public abstract void draw(Canvas canvas);

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setdX(float DX) {
        dX = DX;
    }

    public void setdY(float DY) {
        dY = DY;
    }

    public float getdX() {
        return dX;
    }

    public float getdY() {
        return dY;
    }

    public void setaccdX(float accDX) {
        accdX = accDX;
    }

    public void setaccdY(float accDY) {
        accdY = accDY;
    }

    public int getRadius() {
        return radius;
    }
}

