package com.example.notpinball;

public class User
{
	protected String name;
	protected int level, highScore;
	
	User(String Name, int Level, int HighScore)
	{
		name = Name;
		level = Level;
		highScore = HighScore;
	}

	protected void updateLevel(int newLevel)
	{
		level = newLevel;
	}
	
	protected void updateHighscore(int newScore)
	{
		if(newScore > highScore)
			highScore = newScore;
	}
	
	protected String print()
	{
		return (name + "," + level + "," + highScore + "\n");
	}
}
