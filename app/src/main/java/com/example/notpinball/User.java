package com.example.notpinball;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class User
{
	public String name;
	int level;
	List<Integer> highScore = new LinkedList<>();
	
	User(String Name, int Level)
	{
		name = Name;
		level = Level;
	}
	
	public void updateLevel(int newLevel)
	{
		level = newLevel;
	}
	
	public void addScore(int newScore)
	{
		highScore.add(newScore);
	}
	
	public String print()
	{
		Collections.sort(highScore);
		Collections.reverse(highScore);
		StringBuilder b = new StringBuilder(name + "," + level);
		for(int i :highScore)
			b.append(","+i);
		return b.toString();
	}
}
