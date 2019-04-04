package com.example.notpinball;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

public class UserManagement
{
	private Context context;
	
	private List<User> users = new LinkedList<>();
	
	public UserManagement(Context c)
	{
		context = c;
		this.load();
	}
	
	public void load()
	{
		users.clear();
		try
		{
			File yourFile = context.getFileStreamPath("highscores.csv");
			if (yourFile.exists())
			{
				Log.d("COMPX202", "File found");
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(yourFile)));
				String line;
				
				while ((line = in.readLine()) != null)
				{
					String[] row = line.split(",");
					addUser(row[0], Integer.valueOf(row[1]), Integer.valueOf(row[2]));
				}
			} else
			{
				Log.d("COMPX202", "File not found");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		try
		{
			File saveFile = new File(context.getFilesDir(), "highscores.csv");
			FileOutputStream fOut = new FileOutputStream(saveFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			
			for (int i = 0; i < users.size(); i++)
				myOutWriter.append(users.get(i).print());
			
			myOutWriter.close();
			fOut.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getName(int index)
	{
		return users.get(index).name;
	}
	
	public int getHighScore(int index)
	{
		return users.get(index).highScore;
	}
	
	public void addUser(String Name)
	{
		addUser(Name, 1, 0);
	}
	
	private void addUser(String Name, int Level, int HighScore)
	{
		users.add(new User(Name, Level, HighScore));
	}
	
	public void removeUser(int index)
	{
		users.remove(index);
	}
	
	public void setAsCurrentUser(int index)
	{
		User tmp = users.get(0);
		users.set(0,users.get(index));
		users.set(index,tmp);
	}
	
	public int getSize()
	{
		return users.size();
	}
	
	public void updateLevel(int index, int newLevel)
	{
		users.get(index).updateLevel(newLevel);
	}
	
	public int getLevel(int index)
	{
		return users.get(index).level;
	}
	
	public void updateHighScore(int index, int newScore)
	{
		users.get(index).updateHighscore(newScore);
	}
	
	public int getScore(int index)
	{
		return users.get(index).highScore;
	}
	
	public void print()
	{
		for (int i = 0; i < users.size(); i++)
		{
			Log.d("NPB", i + "  Name: " + users.get(i).name + "       Level: " + users.get(i).level + "       HighScore: " + users.get(i).highScore);
		}
	}
}
