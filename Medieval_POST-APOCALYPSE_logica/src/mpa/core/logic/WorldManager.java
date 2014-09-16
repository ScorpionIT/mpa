package mpa.core.logic;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import mpa.core.logic.building.Headquarter;

public class WorldManager
{
	private World world;
	private Map<Integer, Point> headquartedPosition;

	public WorldManager(String mapname)
	{
		world = new World(100, 100);
		headquartedPosition = new HashMap<Integer, Point>();
		headquartedPosition.put(1, new Point ( 10, 30 ));
	}

	public Headquarter addHeadquarter(int i)
	{
		Headquarter headquarter;
		try
		{
			headquarter = new Headquarter(headquartedPosition.get(i).x,
					headquartedPosition.get(i).y, 100, 100, 0);
			world.addObject(headquarter);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
