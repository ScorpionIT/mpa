package mpa.core.logic;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Wood;

public class MapManager
{
	private Map<Integer, Point> headquartedPosition;
	private int width;
	private int height;
	private char map[][];

	public MapManager(String path)
	{
		String s;
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader(path));

			int i = 0;

			while ((s = reader.readLine()) != null)
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

					this.map = new char[this.height][this.width];
				}
				else
				{

					for (int j = 0; j < s.length(); j++)
						map[i][j] = s.charAt(j);
					i++;
				}
			}
			reader.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * public Map<Integer, Point> getHeadquartedPosition() { return headquartedPosition; }
	 */

	public World decode() throws Exception
	{
		World world = new World(this.width, this.height);

		System.out.println("ho creato il mondo");

		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				switch (map[i][j])
				{
					case 'H':
						// headquartedPosition.put(headquartedPosition.size(), new Point(i, j));
						break;

					case 'F':
						world.addObject(new Field(i, j, null));
						break;

					case 'T':
						world.addObject(new Wood(i, j, null));
						break;

					case 'C':
						world.addObject(new Cave(i, j, null));
						break;

				// case '0':
				// world.addObject(new EmptyObject(i, j));
				// break;

				// case 'S':
				// world.addObject(new FictiveSpace (i,j));
				// break;
				}
			}
		}
		// world.setHeadquartedPosition(headquartedPosition);
		System.out.println("ho terminato il mio lavoro");
		return world;
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

	public void printCodedMap()
	{
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
				System.out.print(map[i][j] + " ");
			System.out.println();
		}
	}
}
