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

class UserManagement
{
	private Context context;
	
	private List<User> users = new LinkedList<>();
	
	UserManagement(Context c)
	{
		context = c;
		this.load();
	}
	
	void load()
	{
		Log.d("NPB", "Load file");
		users.clear();
		try
		{
			File yourFile = context.getFileStreamPath("highscores.csv");
			if (yourFile.exists())
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(yourFile)));
				String line;
				User tmp;
				while ((line = in.readLine()) != null)
				{
					String[] row = line.split(",");
					tmp = new User(row[0], Integer.valueOf(row[1]));
					for (int i = 4; i < row.length;i++)
						tmp.addScore(Integer.valueOf(row[i]));
					tmp.setCurrentScore(Integer.valueOf(row[2]),Integer.valueOf(row[3]));
					users.add(tmp);
				}
			} else
			{
				Log.d("NPB", "File not found");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void save()
	{
		Log.d("NPB", "Save file");
		try
		{
			File saveFile = new File(context.getFilesDir(), "highscores.csv");
			FileOutputStream fOut = new FileOutputStream(saveFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			
			for (int i = 0; i < users.size(); i++)
				myOutWriter.append(users.get(i).print() + "\n");
			
			myOutWriter.close();
			fOut.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.print();
	}
	
	String getName(int index)
	{
		if (!users.isEmpty())
			return users.get(index).name;
		else
			return null;
	}
	
	void addUser(String Name)
	{
		addUser(Name, 1);
	}
	
	private void addUser(String Name, int Level)
	{
		users.add(new User(Name, Level));
	}
	
	void removeUser(int index)
	{
		users.remove(index);
	}
	
	void setAsCurrentUser(int index)
	{
		User tmp = users.get(0);
		users.set(0,users.get(index));
		users.set(index,tmp);
	}
	
	int getSize()
	{
		return users.size();
	}
	
	List<User> getUsers()
	{
		return users;
	}
	
	void updateLevel(int index, int newLevel)
	{
		users.get(index).updateLevel(newLevel);
	}
	
	int getLevel(int index)
	{
		if (!users.isEmpty())
			return users.get(index).level;
		else
			return -1;
	}
	
	int getHighScore(int index)
	{
		if (!users.isEmpty())
			if(users.get(index).highScore.isEmpty())
				return 0;
			else
				return users.get(index).highScore.get(0);
		else
			return -1;
	}
	
	void addScore(int index, int newScore)
	{
		users.get(index).addScore(newScore);
	}
	
	int getHighscore(int index)
	{
		if(users.get(index).highScore.isEmpty())
			return 0;
		else
			return users.get(index).highScore.get(0);
	}
	
	int getAverageScore(int index)
	{
		return users.get(index).getAverageScore();
	}
	
	int getCurrentScore(int index)
	{
		return users.get(index).getCurrentScore();
	}
	
	int getCurrentHealth(int index)
	{
		return users.get(index).getCurrentHealth();
	}
	
	void setCurrentScore(int index, int Score, int Health)
	{
		users.get(index).setCurrentScore(Score,Health);
	}
	void addCurrentScore(int index)
	{
		users.get(index).addCurrentScore();
	}
	
	void print()
	{
		for (int i = 0; i < users.size(); i++)
		{
			Log.d("NPB", i + " "+users.get(i).print() + " Average Score: " + this.getAverageScore(i));
		}
	}
}
