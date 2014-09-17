package mpa.core.logic;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MapManager
{
	private Map<Integer, Point> headquartedPosition;
	private int width;
	private int height;
	private char map [][];

	public MapManager(String path)
	{		
		String s;
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader(path));
			
			int i = 0;
			
			while( (s = reader.readLine()) != null )
			{
				if (s.charAt(0) == 'h')
				{
					String mapInformation = new String();
					
					for (int j = 2; j < s.length(); j++)
						mapInformation += s.charAt(j);
					
					this.height = Integer.parseInt(mapInformation);
				
				}
				else if (s.charAt(0) == 'w')
				{
					String mapInformation = new String();
					
					for (int j = 2; j < s.length(); j++)
						mapInformation += s.charAt(j);
					
					this.width = Integer.parseInt(mapInformation);
					
					this.map = new char [this.height][this.width];
					
					
				}
				else
				{
					
					for (int j = 0; j < s.length(); j++)
					{
						map [i][j] = s.charAt(j);
						if (map[i][j] == '6')
							headquartedPosition.put (headquartedPosition.size(),new Point (i,j));
					}
					i++;
				}
			}
			reader.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}

	public Map<Integer, Point> getHeadquartedPosition()
	{
		return headquartedPosition;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	

	public char[][] getMap()
	{
		return map;
	}

	public void printMap ()
	{
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
				System.out.print(map[i][j] + " ");
			System.out.println();
		}
	
	}
	


}
