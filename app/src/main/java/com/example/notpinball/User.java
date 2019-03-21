package com.example.notpinball;

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
	
	public String print()
	{
		return (name + "," + level + "," + highScore + "\n");
	}
}
