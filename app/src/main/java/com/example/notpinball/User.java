package com.example.notpinball;

import java.util.logging.Level;

public class User
{
	public String name;
	public int level, highScore;
	
	User(String Name, int Level, int HighScore)
	{
		name = Name;
		level = Level;
		highScore = HighScore;
	}

	public void updateLevel(int newLevel)
	{
		level = newLevel;
	}

	public void updateHighscore(int newScore)
	{
		if(newScore > highScore)
			highScore = newScore;
	}
	
	public String print()
	{
		return (name + "," + level + "," + highScore + "\n");
	}
}
