package mpa.core.logic;

import java.util.HashMap;
import java.util.Map;

public class WorldManager
{
	private World world;
	private Map<Integer, int[]> headquartedPosition;

	public WorldManager(String mapname)
	{
		world = new World(100, 100);
		headquartedPosition = new HashMap<Integer, int[]>();
		headquartedPosition.put(1, new int[] { 10, 30 });
	}

	public Headquarter addHeadquarter(int i)
	{
		Headquarter headquarter;
		try
		{
			headquarter = new Headquarter(headquartedPosition.get(1)[0],
					headquartedPosition.get(1)[1], 100, 100, 0);
			world.addObject(headquarter);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
