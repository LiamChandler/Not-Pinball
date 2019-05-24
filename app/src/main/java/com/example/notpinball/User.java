package com.example.notpinball;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class User
{
	public String name;
	int level;
	private int currentScore = 0, currentHealth = NotPinball.playerMaxHealth;
	List<Integer> highScore = new LinkedList<>();
	
	User(String Name, int Level)
	{
		name = Name;
		level = Level;
	}
	
	void updateLevel(int newLevel)
	{
		level = newLevel;
	}
	
	void addScore(int newScore)
	{
		highScore.add(newScore);
		currentScore = 0;
	}
	
	int getCurrentScore()
	{
		return currentScore;
	}
	
	int getCurrentHealth()
	{
		return currentHealth;
	}
	
	void setCurrentScore(int score, int Health)
	{
		currentScore = score;
		currentHealth = Health;
	}
	void addCurrentScore()
	{
		addScore(currentScore);
		currentScore = 0;
		currentHealth = NotPinball.playerMaxHealth;
	}
	
	String print()
	{
		Collections.sort(highScore);
		Collections.reverse(highScore);
		StringBuilder b = new StringBuilder(name + "," + level + "," + currentScore + "," + currentHealth);
		for(int i :highScore)
			b.append(","+i);
		return b.toString();
	}
}
